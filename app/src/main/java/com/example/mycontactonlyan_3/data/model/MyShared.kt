package com.example.mycontactonlyan_3.data.model

import android.content.Context
import android.content.SharedPreferences

object MyShared {
    private lateinit var pref: SharedPreferences

    fun init(context: Context) {
        pref = context.getSharedPreferences("MyShared", Context.MODE_PRIVATE)
    }

    fun getToken(): String {
        return pref.getString("TOKEN", "1").toString()
    }

    fun setToken(token: String) {
        pref.edit().putString("TOKEN", token).apply()
    }

}