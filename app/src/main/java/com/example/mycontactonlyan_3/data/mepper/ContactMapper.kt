package com.example.mycontactonlyan_3.data.mepper

import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.data.model.StatusEnum
import com.example.mycontactonlyan_3.data.model.toStatusEnum
import com.example.mycontactonlyan_3.data.sourse.local.entity.ContactEntity
import com.example.mycontactonlyan_3.data.sourse.remote.response.ContactResponse

object ContactMapper {

    fun ContactResponse.toUIData() : ContactUIData =
        ContactUIData(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = StatusEnum.SYNC
        )

    fun ContactEntity.toUIData(id : Int) : ContactUIData =
        ContactUIData(
            id = id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = this.statusCode.toStatusEnum()
        )
}
