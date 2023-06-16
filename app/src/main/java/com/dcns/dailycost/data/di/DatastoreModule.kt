package com.dcns.dailycost.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.Constant
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.data.serializer.UserCredentialSerializer
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
        @ApplicationContext context: Context
    ): DataStore<ProtoUserCredential> = DataStoreFactory.create(
        serializer = UserCredentialSerializer,
        corruptionHandler = UserCredentialRepository.corruptionHandler,
        produceFile = { context.dataStoreFile(Constant.USER_CREDENTIAL) }
    )

}