package com.example.mycontactonlyan_3.di

import android.content.Context
import android.content.Entity
import androidx.room.Room
import com.example.mycontactonlyan_3.data.sourse.local.ContactDatabase
import com.example.mycontactonlyan_3.data.sourse.local.dao.ContactDao
import com.example.mycontactonlyan_3.presenter.screens.contact.ContactScreenDirections
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalSourceModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ContactDatabase =
        Room.databaseBuilder(context, ContactDatabase::class.java, "MyContact.dp")
            .allowMainThreadQueries().build()

    @[Provides Singleton]
    fun provideContactDao(database: ContactDatabase): ContactDao = database.getContactDao()
}