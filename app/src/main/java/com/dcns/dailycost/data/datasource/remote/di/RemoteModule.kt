package com.dcns.dailycost.data.datasource.remote.di

import com.dcns.dailycost.BuildConfig
import com.dcns.dailycost.data.datasource.remote.DepoHandlerImpl
import com.dcns.dailycost.data.datasource.remote.IncomeHandlerImpl
import com.dcns.dailycost.data.datasource.remote.LoginRegisterHandlerImpl
import com.dcns.dailycost.data.datasource.remote.NoteHandlerImpl
import com.dcns.dailycost.data.datasource.remote.ShoppingHandlerImpl
import com.dcns.dailycost.data.datasource.remote.handlers.DepoHandler
import com.dcns.dailycost.data.datasource.remote.handlers.IncomeHandler
import com.dcns.dailycost.data.datasource.remote.handlers.LoginRegisterHandler
import com.dcns.dailycost.data.datasource.remote.handlers.NoteHandler
import com.dcns.dailycost.data.datasource.remote.handlers.ShoppingHandler
import com.dcns.dailycost.data.datasource.remote.services.DepoService
import com.dcns.dailycost.data.datasource.remote.services.IncomeService
import com.dcns.dailycost.data.datasource.remote.services.LoginRegisterService
import com.dcns.dailycost.data.datasource.remote.services.NoteService
import com.dcns.dailycost.data.datasource.remote.services.ShoppingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    fun provideBaseUrl(): String = BuildConfig.API_BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }

            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl :String
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginRegisterService(
        retrofit: Retrofit
    ): LoginRegisterService = retrofit.create(LoginRegisterService::class.java)

    @Provides
    @Singleton
    fun provideShoppingService(
        retrofit: Retrofit
    ): ShoppingService = retrofit.create(ShoppingService::class.java)

    @Provides
    @Singleton
    fun provideDepoService(
        retrofit: Retrofit
    ): DepoService = retrofit.create(DepoService::class.java)

    @Provides
    @Singleton
    fun provideNoteService(
        retrofit: Retrofit
    ): NoteService = retrofit.create(NoteService::class.java)

    @Provides
    @Singleton
    fun provideIncomeService(
        retrofit: Retrofit
    ): IncomeService = retrofit.create(IncomeService::class.java)

    @Provides
    @Singleton
    fun provideLoginRegisterHandler(
        impl: LoginRegisterHandlerImpl
    ): LoginRegisterHandler = impl

    @Provides
    @Singleton
    fun provideShoppingHandler(
        impl: ShoppingHandlerImpl
    ): ShoppingHandler = impl

    @Provides
    @Singleton
    fun provideIncomeHandler(
        impl: IncomeHandlerImpl
    ): IncomeHandler = impl

    @Provides
    @Singleton
    fun provideDepoHandler(
        impl: DepoHandlerImpl
    ): DepoHandler = impl

    @Provides
    @Singleton
    fun provideNoteHandler(
        impl: NoteHandlerImpl
    ): NoteHandler = impl

}