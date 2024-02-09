package com.example.mycontactonlyan_3.di

import com.example.mycontactonlyan_3.navigatiion.AppNavigationDispatcher
import com.example.mycontactonlyan_3.navigatiion.AppNavigationHandler
import com.example.mycontactonlyan_3.navigatiion.AppNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigateModule {
    @Binds
    fun bindAppNavigator(impl: AppNavigationDispatcher): AppNavigator

    @Binds
    fun bindAppNavHandler(impl: AppNavigationDispatcher): AppNavigationHandler

}