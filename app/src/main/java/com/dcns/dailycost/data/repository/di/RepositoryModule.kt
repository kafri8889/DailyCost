package com.dcns.dailycost.data.repository.di

import androidx.datastore.core.DataStore
import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.ProtoUserPreference
import com.dcns.dailycost.data.datasource.local.dao.NoteDao
import com.dcns.dailycost.data.datasource.remote.handlers.DepoHandler
import com.dcns.dailycost.data.datasource.remote.handlers.IncomeHandler
import com.dcns.dailycost.data.datasource.remote.handlers.LoginRegisterHandler
import com.dcns.dailycost.data.datasource.remote.handlers.NoteHandler
import com.dcns.dailycost.data.datasource.remote.handlers.ShoppingHandler
import com.dcns.dailycost.data.repository.BalanceRepository
import com.dcns.dailycost.data.repository.DepoRepository
import com.dcns.dailycost.data.repository.IncomeRepository
import com.dcns.dailycost.data.repository.LoginRegisterRepository
import com.dcns.dailycost.data.repository.NoteRepository
import com.dcns.dailycost.data.repository.ShoppingRepository
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.data.repository.UserPreferenceRepository
import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.IDepoRepository
import com.dcns.dailycost.domain.repository.IIncomeRepository
import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.repository.IShoppingRepository
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.repository.IUserPreferenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserCredentialRepository(
        userCredentialDataStore: DataStore<ProtoUserCredential>
    ): IUserCredentialRepository = UserCredentialRepository(
        userCredentialDataStore = userCredentialDataStore
    )

    @Provides
    @Singleton
    fun provideUserPreferenceRepository(
        userPreferenceDataStore: DataStore<ProtoUserPreference>
    ): IUserPreferenceRepository = UserPreferenceRepository(
        userPreferenceDataStore = userPreferenceDataStore
    )

    @Provides
    @Singleton
    fun provideLoginRegisterRepository(
        loginRegisterHandler: LoginRegisterHandler
    ): ILoginRegisterRepository = LoginRegisterRepository(
        loginRegisterHandler = loginRegisterHandler
    )

    @Provides
    @Singleton
    fun provideShoppingRepository(
        shoppingHandler: ShoppingHandler
    ): IShoppingRepository = ShoppingRepository(
        shoppingHandler = shoppingHandler
    )

    @Provides
    @Singleton
    fun provideBalanceRepository(
        depoHandler: DepoHandler,
        balanceDataStore: DataStore<ProtoUserBalance>
    ): IBalanceRepository = BalanceRepository(
        depoHandler = depoHandler,
        balanceDataStore = balanceDataStore
    )

    @Provides
    @Singleton
    fun provideIncomeRepository(
        incomeHandler: IncomeHandler
    ): IIncomeRepository = IncomeRepository(
        incomeHandler = incomeHandler
    )

    @Provides
    @Singleton
    fun provideDepoRepository(
        depoHandler: DepoHandler
    ): IDepoRepository = DepoRepository(
        depoHandler = depoHandler
    )

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteHandler: NoteHandler,
        noteDao: NoteDao
    ): INoteRepository = NoteRepository(
        noteHandler = noteHandler,
        noteDao = noteDao
    )

}