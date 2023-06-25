package com.dcns.dailycost.domain.di

import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.ICategoryRepository
import com.dcns.dailycost.domain.repository.IDepoRepository
import com.dcns.dailycost.domain.repository.IIncomeRepository
import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.repository.IShoppingRepository
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCases
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.domain.use_case.ShoppingUseCases
import com.dcns.dailycost.domain.use_case.category.GetLocalCategoryUseCase
import com.dcns.dailycost.domain.use_case.category.InputLocalCategoryUseCase
import com.dcns.dailycost.domain.use_case.depo.AddDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.EditDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.GetLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.depo.GetRemoteBalanceUseCase
import com.dcns.dailycost.domain.use_case.depo.TopUpDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.UpdateLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.income.AddRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.GetRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.login_register.UserLoginUseCase
import com.dcns.dailycost.domain.use_case.login_register.UserRegisterUseCase
import com.dcns.dailycost.domain.use_case.note.AddRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetLocalNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.SyncLocalWithRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.UpsertLocalNoteUseCase
import com.dcns.dailycost.domain.use_case.shopping.PostShoppingUseCase
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
    fun provideShoppingUseCases(
        shoppingRepository: IShoppingRepository
    ): ShoppingUseCases = ShoppingUseCases(
        postShoppingUseCase = PostShoppingUseCase(shoppingRepository)
    )

    @Provides
    @Singleton
    fun provideDepoUseCases(
        depoRepository: IDepoRepository,
        balanceRepository: IBalanceRepository
    ): DepoUseCases = DepoUseCases(
        editDepoUseCase = EditDepoUseCase(depoRepository),
        addDepoUseCase = AddDepoUseCase(depoRepository),
        topUpDepoUseCase = TopUpDepoUseCase(depoRepository),
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
    fun provideIncomeUseCases(
        incomeRepository: IIncomeRepository
    ): IncomeUseCases = IncomeUseCases(
        addRemoteIncomeUseCase = AddRemoteIncomeUseCase(incomeRepository),
        getRemoteIncomeUseCase = GetRemoteIncomeUseCase(incomeRepository)
    )

}