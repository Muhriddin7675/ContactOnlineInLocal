package com.example.mycontactonlyan_3.di

import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.domain.ContactRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun getContactRepository(impl: ContactRepositoryImpl) : ContactRepository
}