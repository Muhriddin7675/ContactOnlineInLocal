package com.example.mycontactonlyan_3.data.sourse.local.entity

import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactEntity (
    @PrimaryKey(autoGenerate = true) val id:Int,
    val remoteId :Int,
    val firstName :String,
    val lastName:String,
    val phone: String,
    val statusCode:Int
)