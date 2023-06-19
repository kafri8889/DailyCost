package com.dcns.dailycost.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.Constant
import com.dcns.dailycost.data.repository.UserBalanceRepository
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.data.serializer.UserBalanceSerializer
import com.dcns.dailycost.data.serializer.UserCredentialSerializer
import com.dcns.dailycost.foundation.common.EncryptionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {

    @Provides
    @Singleton
    fun provideUserCredentialDataStore(
        @ApplicationContext context: Context,
        encryptionManager: EncryptionManager
    ): DataStore<ProtoUserCredential> = DataStoreFactory.create(
        serializer = UserCredentialSerializer(encryptionManager),
        corruptionHandler = UserCredentialRepository.corruptionHandler,
        produceFile = { context.dataStoreFile(Constant.USER_CREDENTIAL) }
    )

    @Provides
    @Singleton
    fun provideUserBalanceDataStore(
        @ApplicationContext context: Context
    ): DataStore<ProtoUserBalance> = DataStoreFactory.create(
        serializer = UserBalanceSerializer,
        corruptionHandler = UserBalanceRepository.corruptionHandler,
        produceFile = { context.dataStoreFile(Constant.USER_BALANCE) }
    )

}