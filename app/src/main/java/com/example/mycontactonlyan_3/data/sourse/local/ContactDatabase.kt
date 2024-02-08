package com.example.mycontactonlyan_3.data.sourse.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mycontactonlyan_3.data.sourse.local.dao.ContactDao
import com.example.mycontactonlyan_3.data.sourse.local.entity.ContactEntity
import dagger.hilt.android.lifecycle.HiltViewModel

@Database(entities = [ContactEntity::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun getContactDao(): ContactDao
}