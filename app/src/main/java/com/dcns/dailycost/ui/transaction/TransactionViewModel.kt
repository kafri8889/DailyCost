package com.dcns.dailycost.ui.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.anafthdev.datemodule.extension.isToday
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.data.model.remote.request_body.expense.AddExpenseRequestBody
import com.dcns.dailycost.data.model.remote.request_body.expense.DeleteExpenseRequestBody
import com.dcns.dailycost.data.model.remote.request_body.income.AddIncomeRequestBody
import com.dcns.dailycost.data.model.remote.request_body.income.DeleteIncomeRequestBody
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.util.GetTransactionBy
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.CommonDateFormatter
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.worker.Workers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionViewModel @Inject constructor(
	private val userCredentialUseCases: UserCredentialUseCases,
	private val connectivityManager: ConnectivityManager,
	private val categoryUseCases: CategoryUseCases,
	private val expenseUseCases: ExpenseUseCases,
	private val incomeUseCases: IncomeUseCases,
	private val workManager: WorkManager,
	private val savedStateHandle: SavedStateHandle
): BaseViewModel<TransactionState, TransactionAction>(
	savedStateHandle,
	TransactionState()
) {
	private val deliveredTransactionId =
		savedStateHandle.getStateFlow<Int?>(DestinationArgument.TRANSACTION_ID, null)
	private val deliveredActionMode =
		savedStateHandle.getStateFlow<ActionMode?>(DestinationArgument.ACTION_MODE, null)
	private val deliveredTransactionType =
		savedStateHandle.getStateFlow<TransactionType?>(DestinationArgument.TRANSACTION_TYPE, null)

	private val _currentDeleteWorkId = MutableStateFlow<UUID?>(null)
	private val currentDeleteWorkId: StateFlow<UUID?> = _currentDeleteWorkId

	private val _currentSaveWorkId = MutableStateFlow<UUID?>(null)
	private val currentSaveWorkId: StateFlow<UUID?> = _currentSaveWorkId

	init {
		viewModelScope.launch(Dispatchers.IO) {
			deliveredTransactionId
				.filterNotNull()
				.combine(deliveredTransactionType.filterNotNull()) { id, type ->
					val li = when (type) {
						TransactionType.Income -> incomeUseCases.getLocalIncomeUseCase(
							GetTransactionBy.ID(id)
						).firstOrNull()

						TransactionType.Expense -> expenseUseCases.getLocalExpenseUseCase(
							GetTransactionBy.ID(id)
						).firstOrNull()
					}

					if (!li.isNullOrEmpty()) li[0] to type else null to type
				}.filterNotNull().collect { (transaction, type) ->
					Timber.i("Received transaction: $transaction")
					updateState {
						copy(
							id = transaction?.id ?: id,
							title = transaction?.name ?: title,
							amount = transaction?.amount ?: amount,
							payment = transaction?.payment ?: payment,
							date = transaction?.date ?: date,
							category = transaction?.category ?: category,
							selectedTransactionType = if (actionMode == ActionMode.New) selectedTransactionType else type
						)
					}
				}
		}

		viewModelScope.launch(Dispatchers.IO) {
			deliveredActionMode
				.filterNotNull()
				.collect { mode ->
					updateState {
						copy(
							actionMode = mode
						)
					}
				}
		}

		viewModelScope.launch(Dispatchers.IO) {
			categoryUseCases.getLocalCategoryUseCase().collect { categories ->
				updateState {
					copy(
						availableCategory = categories
					)
				}
			}
		}

		viewModelScope.launch {
			currentDeleteWorkId.flatMapMerge { uuid ->
				if (uuid != null) {
					workManager.getWorkInfoByIdLiveData(uuid).asFlow()
				} else flowOf(null)
			}.filterNotNull().collect { workInfo ->
				when (workInfo.state) {
					WorkInfo.State.ENQUEUED -> {}
					WorkInfo.State.SUCCEEDED -> {
						sendEvent(TransactionUiEvent.TransactionDeleted())
					}

					WorkInfo.State.FAILED -> {
						sendEvent(TransactionUiEvent.FailedToDelete())
					}
					else -> {}
				}
			}
		}

		viewModelScope.launch {
			currentSaveWorkId.flatMapMerge { uuid ->
				if (uuid != null) {
					workManager.getWorkInfoByIdLiveData(uuid).asFlow()
				} else flowOf(null)
			}.filterNotNull().collect { workInfo ->
				when (workInfo.state) {
					WorkInfo.State.ENQUEUED -> {}
					WorkInfo.State.SUCCEEDED -> {
						sendEvent(TransactionUiEvent.TransactionSaved())
					}

					WorkInfo.State.FAILED -> {
						sendEvent(TransactionUiEvent.FailedToSave())
					}
					else -> {}
				}
			}
		}
	}

	override fun onAction(action: TransactionAction) {
		when (action) {
			is TransactionAction.SetTransactionType -> {
				viewModelScope.launch {
					updateState {
						copy(
							selectedTransactionType = action.type
						)
					}
				}
			}

			is TransactionAction.SetAmount -> {
				viewModelScope.launch {
					updateState {
						copy(
							amount = action.amount
						)
					}
				}
			}
			is TransactionAction.SetCategory -> {
				viewModelScope.launch {
					updateState {
						copy(
							category = action.category
						)
					}
				}
			}
			is TransactionAction.SetDate -> {
				viewModelScope.launch {
					updateState {
						copy(
							date = action.date
						)
					}
				}
			}
			is TransactionAction.SetPayment -> {
				viewModelScope.launch {
					updateState {
						copy(
							payment = action.payment
						)
					}
				}
			}

			is TransactionAction.SetName -> {
				viewModelScope.launch {
					updateState {
						copy(
							title = action.name,
							titleError = action.name.isBlank()
						)
					}
				}
			}

			TransactionAction.Delete -> { // Delete transaksi ke API
				viewModelScope.launch {
					// Cek koneksi internet
					if (connectivityManager.isNetworkAvailable.value == false) {
						sendEvent(TransactionUiEvent.NoInternetConnection())
						return@launch
					}

					// Kirim event "Deleting"
					sendEvent(TransactionUiEvent.Deleting())

					// Request penghapusan, observe requestnya di atas (blok init)
					userCredentialUseCases.getUserCredentialUseCase().firstOrNull()
						?.let { credential ->
							workManager.beginWith(
								when (state.value.selectedTransactionType) {
									TransactionType.Income -> Workers.deleteIncomeWorker(
										DeleteIncomeRequestBody(
											incomeId = state.value.id,
											userId = credential.id.toInt()
										)
									).also { _currentDeleteWorkId.emit(it.id) }

									TransactionType.Expense -> Workers.deleteExpenseWorker(
										DeleteExpenseRequestBody(
											expenseId = state.value.id,
											userId = credential.id.toInt()
										)
									).also { _currentDeleteWorkId.emit(it.id) }
								}
							).then(Workers.syncWorker()).enqueue()
						}
				}
			}
			TransactionAction.Save -> {
				viewModelScope.launch(Dispatchers.IO) {
					val mState = state.value

					if (mState.title.isBlank()) {
						updateState {
							copy(
								titleError = true
							)
						}
						return@launch
					}

					// Cek koneksi internet
					if (connectivityManager.isNetworkAvailable.value == false) {
						sendEvent(TransactionUiEvent.NoInternetConnection())
						return@launch
					}

					// Kirim event "Saving"
					sendEvent(TransactionUiEvent.Saving())

					userCredentialUseCases.getUserCredentialUseCase().firstOrNull()?.let { credential ->
						val date = when {
							mState.date.isToday() -> System.currentTimeMillis()
							mState.date > System.currentTimeMillis() -> {
								Calendar.getInstance().apply {
									timeInMillis = mState.date

									set(Calendar.HOUR_OF_DAY, 0)
									set(Calendar.MINUTE, 0)
									set(Calendar.SECOND, 1)
								}.timeInMillis
							}
							mState.date < System.currentTimeMillis() -> {
								Calendar.getInstance().apply {
									timeInMillis = mState.date

									set(Calendar.HOUR_OF_DAY, 23)
									set(Calendar.MINUTE, 59)
									set(Calendar.SECOND, 59)
								}.timeInMillis
							}
							else -> System.currentTimeMillis()
						}

						// Chain works: Post -> Sync
						workManager.beginWith(
							when (mState.selectedTransactionType) {
								TransactionType.Income -> Workers.postIncomeWorker(
									AddIncomeRequestBody(
										amount = mState.amount.toInt(),
										name = mState.title,
										payment = mState.payment.apiName,
										category = mState.category.name,
										date = CommonDateFormatter.api2.format(date),
										userId = credential.id.toInt()
									)
								).also { _currentSaveWorkId.emit(it.id) }
								TransactionType.Expense -> Workers.postExpenseWorker(
									AddExpenseRequestBody(
										amount = mState.amount.toInt(),
										name = mState.title,
										payment = mState.payment.apiName,
										category = mState.category.name,
										date = CommonDateFormatter.api2.format(date),
										userId = credential.id.toInt()
									)
								).also { _currentSaveWorkId.emit(it.id) }
							}
						).then(Workers.syncWorker()).enqueue()
					}
				}
			}
		}
	}
}