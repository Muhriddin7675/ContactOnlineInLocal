package com.example.mycontactonlyan_3.presenter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.data.model.StatusEnum
import com.example.mycontactonlyan_3.data.sourse.remote.response.ContactResponse
import com.example.mycontactonlyan_3.databinding.ItemContactBinding
import com.example.mycontactonlyan_3.databinding.ScreenContactAddBinding
import kotlin.math.abs

class ContactAdapter :
    ListAdapter<ContactUIData, ContactAdapter.ContactViewHolder>(ContactDiffUtil) {

    private lateinit var itemLongClick: (ContactUIData) -> Unit

    object ContactDiffUtil : DiffUtil.ItemCallback<ContactUIData>() {
        override fun areItemsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ContactUIData,
            newItem: ContactUIData
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        ViewHolder(binding.root) {

        init {
                binding.root.setOnLongClickListener {
                    itemLongClick.invoke(getItem(adapterPosition))
                    return@setOnLongClickListener true
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() {

            getItem(adapterPosition).apply {
                binding.textName.text = "$firstName $lastName"
                binding.textNumber.text = phone
                when (this.status) {
                    StatusEnum.SYNC -> binding.textStatus.isVisible = false
                    else -> {
                        binding.textStatus.isVisible = true
                        binding.textStatus.text = this.status.name
                    }

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) = holder.bind()
    fun setLongItemClick(block: (ContactUIData) -> Unit) {
        itemLongClick = block
    }
}