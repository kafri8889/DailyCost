package com.dcns.dailycost.ui.dashboard

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.dcns.dailycost.domain.use_case.CommonUseCases
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.NotificationUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.domain.util.GetNotificationBy
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.SharedUiEvent
import com.dcns.dailycost.foundation.worker.Workers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
	private val notificationUseCases: NotificationUseCases,
	private val connectivityManager: ConnectivityManager,
	private val commonUseCases: CommonUseCases,
	private val expenseUseCases: ExpenseUseCases,
	private val incomeUseCases: IncomeUseCases,
	private val sharedUiEvent: SharedUiEvent,
	private val workManager: WorkManager
): BaseViewModel<DashboardState, DashboardAction>() {

	private val _currentSyncWorkId = MutableStateFlow<UUID?>(null)
	private val currentSyncWorkId: StateFlow<UUID?> = _currentSyncWorkId

	init {
		viewModelScope.launch {
			sharedUiEvent.uiEvent.filterNotNull().collectLatest { event ->
				when (event) {
					is DashboardUiEvent.TopUpSuccess -> sendEvent(event)
				}
			}
		}

		viewModelScope.launch {
			connectivityManager.isNetworkAvailable.asFlow().collectLatest { have ->
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
			userCredentialUseCases.getUserCredentialUseCase().collectLatest { cred ->
				updateState {
					copy(
						credential = cred
					)
				}
			}
		}

		viewModelScope.launch {
			userPreferenceUseCases.getUserPreferenceUseCase().collectLatest { pref ->
				updateState {
					copy(
						initialBalanceVisibility = pref.defaultBalanceVisibility
					)
				}
			}
		}

		viewModelScope.launch {
			notificationUseCases.getLocalNotificationUseCase(GetNotificationBy.Unread).collectLatest { notifications ->
				updateState {
					copy(
						unreadNotificationCount = notifications.size
					)
				}
			}
		}

		viewModelScope.launch {
			commonUseCases.getBalanceUseCase().collectLatest { balances ->
				updateState {
					copy(
						balances = balances
					)
				}
			}
		}

		viewModelScope.launch {
			commonUseCases.getRecentActivityUseCase().collectLatest { list ->
				updateState {
					copy(
						recentActivity = list
					)
				}
			}
		}

		viewModelScope.launch {
			expenseUseCases.getLocalExpenseUseCase().collectLatest { expenses ->
				updateState {
					copy(
						expenses = expenses
					)
				}
			}
		}

		viewModelScope.launch {
			incomeUseCases.getLocalIncomeUseCase().collectLatest { incomes ->
				updateState {
					copy(
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
			}.filterNotNull().collectLatest { workInfo ->
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

				workManager.enqueue(
					Workers.syncWorker().also {
						_currentSyncWorkId.emit(it.id)
					}
				)
			}
		}
	}
}