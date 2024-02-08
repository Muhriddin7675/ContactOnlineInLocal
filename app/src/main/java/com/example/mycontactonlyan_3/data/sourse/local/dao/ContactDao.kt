package com.example.mycontactonlyan_3.data.sourse.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mycontactonlyan_3.data.sourse.local.entity.ContactEntity

@Dao
interface ContactDao {

    @Query("SELECT * FROM contactentity")
    fun getAllContactLocal(): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addContact(data: ContactEntity)

    @Query("DELETE FROM contactentity WHERE  id =:idContact ")
    fun deleteContact(idContact: Int)


}