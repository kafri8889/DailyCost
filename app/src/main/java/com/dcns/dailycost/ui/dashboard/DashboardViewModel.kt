package com.dcns.dailycost.ui.dashboard

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.anafthdev.datemodule.infix_function.inSameMonth
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Balance
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.CommonDateFormatter
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.SharedUiEvent
import com.dcns.dailycost.foundation.common.SortableByDate
import com.dcns.dailycost.foundation.extension.enqueue
import com.dcns.dailycost.foundation.worker.Workers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
	private val userCredentialUseCases: UserCredentialUseCases,
	private val userPreferenceUseCases: UserPreferenceUseCases,
	private val connectivityManager: ConnectivityManager,
	private val expenseUseCases: ExpenseUseCases,
	private val incomeUseCases: IncomeUseCases,
	private val sharedUiEvent: SharedUiEvent,
	private val depoUseCases: DepoUseCases,
	private val workManager: WorkManager
): BaseViewModel<DashboardState, DashboardAction>() {

	private val _currentSyncWorkId = MutableStateFlow<UUID?>(null)
	private val currentSyncWorkId: StateFlow<UUID?> = _currentSyncWorkId

	init {
		viewModelScope.launch {
			sharedUiEvent.uiEvent.filterNotNull().collect { event ->
				when (event) {
					is DashboardUiEvent.TopUpSuccess -> sendEvent(event)
				}
			}
		}

		viewModelScope.launch {
			connectivityManager.isNetworkAvailable.asFlow().collect { have ->
				Timber.i("have internet: $have")

				updateState {
					copy(
						internetConnectionAvailable = have == true
					)
				}

				// Kalo ga ada koneksi internet, show snackbar
				if (have == false) {
					sendEvent(DashboardUiEvent.NoInternetConnection())
				}
			}
		}

		viewModelScope.launch {
			userCredentialUseCases.getUserCredentialUseCase().collect { cred ->
				updateState {
					copy(
						credential = cred
					)
				}
			}
		}

		viewModelScope.launch {
			userPreferenceUseCases.getUserPreferenceUseCase().collect { pref ->
				updateState {
					copy(
						initialBalanceVisibility = pref.defaultBalanceVisibility
					)
				}
			}
		}

		viewModelScope.launch {
			combine(
				depoUseCases.getLocalBalanceUseCase(),
				incomeUseCases.getLocalIncomeUseCase(),
				expenseUseCases.getLocalExpenseUseCase(),
			) { userBalance, incomes, expenses ->
				Triple(userBalance, incomes, expenses)
			}.collect { (userBalance, incomes, expenses) ->
				val sorted = ((incomes as List<SortableByDate>) + (expenses as List<SortableByDate>)).sortedByDescending { it.date }

				Timber.i("sorted: ${sorted.map { CommonDateFormatter.api.format(it.date) }}")

				val cashMonthlyExpenses = expenses.asSequence() // Convert ke sequence
					// Filter jika payment == cash dan tanggal berada di bulan yg sama
					.filter { it.payment == WalletType.Cash && it.date inSameMonth System.currentTimeMillis() }
					.sumOf { it.amount }

				val ewalletMonthlyExpenses = expenses.asSequence() // Convert ke sequence
					// Filter jika payment == e-wallet dan tanggal berada di bulan yg sama
					.filter { it.payment == WalletType.EWallet && it.date inSameMonth System.currentTimeMillis() }
					.sumOf { it.amount }

				val bankAccountMonthlyExpenses = expenses.asSequence() // Convert ke sequence
					// Filter jika payment == e-wallet dan tanggal berada di bulan yg sama
					.filter { it.payment == WalletType.BankAccount && it.date inSameMonth System.currentTimeMillis() }
					.sumOf { it.amount }

				val balances = listOf(
					Balance(
						amount = userBalance.cash,
						walletType = WalletType.Cash,
						monthlyExpense = cashMonthlyExpenses
					),
					Balance(
						amount = userBalance.eWallet,
						walletType = WalletType.EWallet,
						monthlyExpense = ewalletMonthlyExpenses
					),
					Balance(
						amount = userBalance.bankAccount,
						walletType = WalletType.BankAccount,
						monthlyExpense = bankAccountMonthlyExpenses
					)
				)

				updateState {
					copy(
						recentlyActivity = sorted.take(5),
						expenses = expenses,
						balance = balances,
						incomes = incomes
					)
				}
			}
		}

		viewModelScope.launch {
			currentSyncWorkId.flatMapLatest { uuid ->
				if (uuid != null) {
					workManager.getWorkInfoByIdLiveData(uuid).asFlow()
				} else flowOf(null)
			}.filterNotNull().collect { workInfo ->
				when (workInfo.state) {
					WorkInfo.State.ENQUEUED -> {
						updateState {
							copy(
								isRefreshing = true
							)
						}
					}

					WorkInfo.State.SUCCEEDED -> {
						updateState {
							copy(
								isRefreshing = false
							)
						}
					}

					WorkInfo.State.FAILED -> {
						workInfo.outputData.getString(Workers.ARG_WORKER_MESSAGE_KEY)
							?.let { message ->
								sendEvent(DashboardUiEvent.GetRemoteFailed(message))
							}

						updateState {
							copy(
								isRefreshing = false
							)
						}
					}

					else -> {}
				}
			}
		}
	}

	override fun defaultState(): DashboardState = DashboardState()

	override fun onAction(action: DashboardAction) {
		when (action) {
			is DashboardAction.Refresh -> viewModelScope.launch {
				// Kalo ga ada koneksi internet, show snackbar
				if (!state.value.internetConnectionAvailable) {
					sendEvent(DashboardUiEvent.NoInternetConnection())
					return@launch
				}

				Workers.syncWorker().also {
					_currentSyncWorkId.emit(it.id)
				}.enqueue(action.context)
			}
		}
	}
}