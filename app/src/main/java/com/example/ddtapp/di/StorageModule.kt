package com.example.ddtapp.di

import android.content.Context
import androidx.room.Room
import com.example.ddtapp.database.Database
import com.example.ddtapp.utils.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule constructor(private val context: Context) {

    @Provides
    @Singleton
    internal fun provideDatabase() = Room.databaseBuilder(
        context,
        Database::class.java,
        Constants.DATABASE
    )
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

}