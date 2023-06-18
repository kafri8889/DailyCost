package com.dcns.dailycost.domain.di

import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import com.dcns.dailycost.domain.repository.IShoppingRepository
import com.dcns.dailycost.domain.use_case.balance.BalanceUseCases
import com.dcns.dailycost.domain.use_case.balance.GetBalanceUseCase
import com.dcns.dailycost.domain.use_case.login_register.LoginRegisterUseCases
import com.dcns.dailycost.domain.use_case.login_register.UserLoginUseCase
import com.dcns.dailycost.domain.use_case.login_register.UserRegisterUseCase
import com.dcns.dailycost.domain.use_case.shopping.PostShoppingUseCase
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
        userRegisterUseCase = UserRegisterUseCase(
            loginRegisterRepository = loginRegisterRepository
        ),
        userLoginUseCase = UserLoginUseCase(
            loginRegisterRepository = loginRegisterRepository
        )
    )

    @Provides
    @Singleton
    fun provideShoppingUseCases(
        shoppingRepository: IShoppingRepository
    ): ShoppingUseCases = ShoppingUseCases(
        postShoppingUseCase = PostShoppingUseCase(
            shoppingRepository = shoppingRepository
        )
    )

    @Provides
    @Singleton
    fun provideBalanceUseCases(
        balanceRepository: IBalanceRepository
    ): BalanceUseCases = BalanceUseCases(
        getBalanceUseCase = GetBalanceUseCase(balanceRepository)
    )

}