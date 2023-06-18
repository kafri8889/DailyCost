package com.dcns.dailycost.data.datasource.local.di

import android.content.Context
import com.dcns.dailycost.data.datasource.local.AppDatabase
import com.dcns.dailycost.data.datasource.local.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideNoteDao(
        appDatabase: AppDatabase
    ): NoteDao = appDatabase.noteDao()

}