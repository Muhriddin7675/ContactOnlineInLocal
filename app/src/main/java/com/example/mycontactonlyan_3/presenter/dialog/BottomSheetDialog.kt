package com.example.mycontactonlyan_3.presenter.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.databinding.DialogBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog() : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogBottomSheetBinding::bind)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom_sheet, container, false)
    }

    private lateinit var clickDelete: () -> Unit
    private lateinit var clickEdit: () -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.delete.setOnClickListener {
            clickDelete.invoke()
            dismiss()
        }
        binding.edit.setOnClickListener {
            clickEdit.invoke()
            dismiss()
        }
    }
    fun setOnClickDelete(block: () -> Unit) {
        clickDelete = block

    }

    fun setOnClickEdit(block: () -> Unit) {
        clickEdit = block

    }

}