package com.dcns.dailycost.data.repository.di

import androidx.datastore.core.DataStore
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.datasource.remote.handlers.LoginRegisterHandler
import com.dcns.dailycost.data.datasource.remote.handlers.ShoppingHandler
import com.dcns.dailycost.data.repository.LoginRegisterRepository
import com.dcns.dailycost.data.repository.ShoppingRepository
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import com.dcns.dailycost.domain.repository.IShoppingRepository
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
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

}