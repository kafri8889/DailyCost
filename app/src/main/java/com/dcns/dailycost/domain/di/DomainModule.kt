package com.dcns.dailycost.domain.di

import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.ICategoryRepository
import com.dcns.dailycost.domain.repository.IDepoRepository
import com.dcns.dailycost.domain.repository.IExpenseRepository
import com.dcns.dailycost.domain.repository.IIncomeRepository
import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.repository.INotificationRepository
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.repository.IUserPreferenceRepository
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.domain.use_case.CombinedUseCases
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCases
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.domain.use_case.NotificationUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.domain.use_case.category.GetLocalCategoryUseCase
import com.dcns.dailycost.domain.use_case.category.InputLocalCategoryUseCase
import com.dcns.dailycost.domain.use_case.combined.GetBalanceUseCase
import com.dcns.dailycost.domain.use_case.combined.GetRecentActivityUseCase
import com.dcns.dailycost.domain.use_case.depo.EditDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.GetLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.depo.GetRemoteBalanceUseCase
import com.dcns.dailycost.domain.use_case.depo.UpdateLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.expense.AddRemoteExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.DeleteLocalExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.DeleteRemoteExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.GetLocalExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.GetRemoteExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.SyncLocalWithRemoteExpenseUseCase
import com.dcns.dailycost.domain.use_case.income.AddRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.DeleteLocalIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.DeleteRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.GetLocalIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.GetRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.SyncLocalWithRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.login_register.UserLoginUseCase
import com.dcns.dailycost.domain.use_case.login_register.UserRegisterUseCase
import com.dcns.dailycost.domain.use_case.note.AddRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetLocalNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.SyncLocalWithRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.UpsertLocalNoteUseCase
import com.dcns.dailycost.domain.use_case.notification.GetLocalNotificationUseCase
import com.dcns.dailycost.domain.use_case.notification.InsertLocalNotificationUseCase
import com.dcns.dailycost.domain.use_case.notification.UpdateLocalNotificationUseCase
import com.dcns.dailycost.domain.use_case.user_credential.EditUserCredentialUseCase
import com.dcns.dailycost.domain.use_case.user_credential.GetUserCredentialUseCase
import com.dcns.dailycost.domain.use_case.user_preference.EditUserPreferenceUseCase
import com.dcns.dailycost.domain.use_case.user_preference.GetUserPreferenceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

	@Provides
	@Singleton
	fun provideLoginRegisterUseCase(
		loginRegisterRepository: ILoginRegisterRepository
	): LoginRegisterUseCases = LoginRegisterUseCases(
		userRegisterUseCase = UserRegisterUseCase(loginRegisterRepository),
		userLoginUseCase = UserLoginUseCase(loginRegisterRepository)
	)

	@Provides
	@Singleton
	fun provideDepoUseCases(
		depoRepository: IDepoRepository,
		balanceRepository: IBalanceRepository
	): DepoUseCases = DepoUseCases(
		editDepoUseCase = EditDepoUseCase(depoRepository),
		updateLocalBalanceUseCase = UpdateLocalBalanceUseCase(balanceRepository),
		getRemoteBalanceUseCase = GetRemoteBalanceUseCase(balanceRepository),
		getLocalBalanceUseCase = GetLocalBalanceUseCase(balanceRepository)
	)

	@Provides
	@Singleton
	fun provideNoteUseCases(
		noteRepository: INoteRepository
	): NoteUseCases = NoteUseCases(
		getRemoteNoteUseCase = GetRemoteNoteUseCase(noteRepository),
		getLocalNoteUseCase = GetLocalNoteUseCase(noteRepository),
		addRemoteNoteUseCase = AddRemoteNoteUseCase(noteRepository),
		upsertLocalNoteUseCase = UpsertLocalNoteUseCase(noteRepository),
		syncLocalWithRemoteNoteUseCase = SyncLocalWithRemoteNoteUseCase(noteRepository)
	)

	@Provides
	@Singleton
	fun provideCategoryUseCases(
		categoryRepository: ICategoryRepository
	): CategoryUseCases = CategoryUseCases(
		getLocalCategoryUseCase = GetLocalCategoryUseCase(categoryRepository),
		inputLocalCategoryUseCase = InputLocalCategoryUseCase(categoryRepository)
	)

	@Provides
	@Singleton
	fun provideExpenseUseCases(
		expenseRepository: IExpenseRepository
	): ExpenseUseCases = ExpenseUseCases(
		addRemoteExpenseUseCase = AddRemoteExpenseUseCase(expenseRepository),
		deleteRemoteExpenseUseCase = DeleteRemoteExpenseUseCase(expenseRepository),
		deleteLocalExpenseUseCase = DeleteLocalExpenseUseCase(expenseRepository),
		getRemoteExpenseUseCase = GetRemoteExpenseUseCase(expenseRepository),
		getLocalExpenseUseCase = GetLocalExpenseUseCase(expenseRepository),
		syncLocalWithRemoteExpenseUseCase = SyncLocalWithRemoteExpenseUseCase(expenseRepository)
	)

	@Provides
	@Singleton
	fun provideIncomeUseCases(
		incomeRepository: IIncomeRepository
	): IncomeUseCases = IncomeUseCases(
		addRemoteIncomeUseCase = AddRemoteIncomeUseCase(incomeRepository),
		deleteRemoteIncomeUseCase = DeleteRemoteIncomeUseCase(incomeRepository),
		deleteLocalIncomeUseCase = DeleteLocalIncomeUseCase(incomeRepository),
		getRemoteIncomeUseCase = GetRemoteIncomeUseCase(incomeRepository),
		getLocalIncomeUseCase = GetLocalIncomeUseCase(incomeRepository),
		syncLocalWithRemoteIncomeUseCase = SyncLocalWithRemoteIncomeUseCase(incomeRepository)
	)

	@Provides
	@Singleton
	fun provideUserCredentialUseCases(
		userCredentialRepository: IUserCredentialRepository
	): UserCredentialUseCases = UserCredentialUseCases(
		editUserCredentialUseCase = EditUserCredentialUseCase(userCredentialRepository),
		getUserCredentialUseCase = GetUserCredentialUseCase(userCredentialRepository)
	)

	@Provides
	@Singleton
	fun provideUserPreferenceUseCases(
		userPreferenceRepository: IUserPreferenceRepository
	): UserPreferenceUseCases = UserPreferenceUseCases(
		editUserPreferenceUseCase = EditUserPreferenceUseCase(userPreferenceRepository),
		getUserPreferenceUseCase = GetUserPreferenceUseCase(userPreferenceRepository)
	)

	@Provides
	@Singleton
	fun provideNotificationUseCases(
		notificationRepository: INotificationRepository
	): NotificationUseCases = NotificationUseCases(
		getLocalNotificationUseCase = GetLocalNotificationUseCase(notificationRepository),
		updateLocalNotificationUseCase = UpdateLocalNotificationUseCase(notificationRepository),
		insertLocalNotificationUseCase = InsertLocalNotificationUseCase(notificationRepository)
	)

	@Provides
	@Singleton
	fun provideCombinedUseCases(
		balanceRepository: IBalanceRepository,
		expenseRepository: IExpenseRepository,
		incomeRepository: IIncomeRepository,
		noteRepository: INoteRepository
	): CombinedUseCases = CombinedUseCases(
		getRecentActivityUseCase = GetRecentActivityUseCase(
			expenseRepository = expenseRepository,
			incomeRepository = incomeRepository,
			noteRepository = noteRepository
		),
		getBalanceUseCase = GetBalanceUseCase(
			expenseRepository = expenseRepository,
			balanceRepository = balanceRepository
		)
	)

}
