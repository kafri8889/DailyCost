package com.dcns.dailycost.domain.di

import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.IDepoRepository
import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.repository.IShoppingRepository
import com.dcns.dailycost.domain.use_case.BalanceUseCases
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCases
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.domain.use_case.ShoppingUseCases
import com.dcns.dailycost.domain.use_case.balance.GetLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.balance.GetRemoteBalanceUseCase
import com.dcns.dailycost.domain.use_case.balance.UpdateLocalBalanceUseCase
import com.dcns.dailycost.domain.use_case.depo.AddDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.EditDepoUseCase
import com.dcns.dailycost.domain.use_case.depo.TopUpDepoUseCase
import com.dcns.dailycost.domain.use_case.login_register.UserLoginUseCase
import com.dcns.dailycost.domain.use_case.login_register.UserRegisterUseCase
import com.dcns.dailycost.domain.use_case.note.AddRemoteNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetLocalNoteUseCase
import com.dcns.dailycost.domain.use_case.note.GetRemoteNoteUseCase
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
    fun provideBalanceUseCases(
        balanceRepository: IBalanceRepository
    ): BalanceUseCases = BalanceUseCases(
        updateLocalBalanceUseCase = UpdateLocalBalanceUseCase(balanceRepository),
        getRemoteBalanceUseCase = GetRemoteBalanceUseCase(balanceRepository),
        getLocalBalanceUseCase = GetLocalBalanceUseCase(balanceRepository)
    )

    @Provides
    @Singleton
    fun provideDepoUseCases(
        depoRepository: IDepoRepository
    ): DepoUseCases = DepoUseCases(
        editDepoUseCase = EditDepoUseCase(depoRepository),
        addDepoUseCase = AddDepoUseCase(depoRepository),
        topUpDepoUseCase = TopUpDepoUseCase(depoRepository)
    )

    @Provides
    @Singleton
    fun provideNoteUseCases(
        noteRepository: INoteRepository
    ): NoteUseCases = NoteUseCases(
        getRemoteNoteUseCase = GetRemoteNoteUseCase(noteRepository),
        getLocalNoteUseCase = GetLocalNoteUseCase(noteRepository),
        addRemoteNoteUseCase = AddRemoteNoteUseCase(noteRepository),
        upsertLocalNoteUseCase = UpsertLocalNoteUseCase(noteRepository)
    )

}