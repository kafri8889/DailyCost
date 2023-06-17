package com.dcns.dailycost.domain.di

import com.dcns.dailycost.data.repository.LoginRegisterRepository
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCase
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
        loginRegisterRepository: LoginRegisterRepository
    ): LoginRegisterUseCase = LoginRegisterUseCase(
        loginRegisterRepository = loginRegisterRepository
    )

}