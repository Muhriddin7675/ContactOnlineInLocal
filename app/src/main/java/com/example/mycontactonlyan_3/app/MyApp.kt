package com.example.mycontactonlyan_3.app

import android.app.Application
import com.example.mycontactonlyan_3.data.model.MyShared
import com.example.mycontactonlyan_3.data.sourse.local.ContactDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        MyShared.init(this)
//        ApiClient.init(this)
//        ContactDatabase.init(this)
//        ContactRepositoryImpl.init()
    }
}