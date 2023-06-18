package com.dcns.dailycost.domain.di

import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import com.dcns.dailycost.domain.repository.IShoppingRepository
import com.dcns.dailycost.domain.use_case.login_register.FetchAPILoginRegisterUseCase
import com.dcns.dailycost.domain.use_case.login_register.LoginRegisterUseCases
import com.dcns.dailycost.domain.use_case.shopping.FetchAPIShoppingUseCase
import com.dcns.dailycost.domain.use_case.shopping.ShoppingUseCases
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
        fetchAPILoginRegisterUseCase = FetchAPILoginRegisterUseCase(
            loginRegisterRepository = loginRegisterRepository
        )
    )

    @Provides
    @Singleton
    fun provideShoppingUseCases(
        shoppingRepository: IShoppingRepository
    ): ShoppingUseCases = ShoppingUseCases(
        fetchAPIShoppingUseCase = FetchAPIShoppingUseCase(
            shoppingRepository = shoppingRepository
        )
    )

}