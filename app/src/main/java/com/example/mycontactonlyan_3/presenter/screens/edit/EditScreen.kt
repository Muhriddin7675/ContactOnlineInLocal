package com.example.mycontactonlyan_3.presenter.screens.edit

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import androidx.navigation.fragment.navArgs
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.data.model.StatusEnum
import com.example.mycontactonlyan_3.databinding.ScreenContactEditBinding
import com.example.mycontactonlyan_3.utils.showToast
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditScreen : Fragment(R.layout.screen_contact_edit) {
    private val binding by viewBinding(ScreenContactEditBinding::bind)
    private val viewModel by viewModels<EditContactModelImpl> ()
    private val navArgs: EditScreenArgs by navArgs()
    private var firstNameBool = true
    private var lastNameBool = true
    private var phoneBool = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.closeScreenLiveData.observe(this, closeScreenObserver)
        viewModel.errorMessageLiveData.observe(this, errorMessageObserver)
        viewModel.messageLiveData.observe(this, errorMessageObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.editInputFirstName.setText(navArgs.firstName)
        binding.editInputLastName.setText(navArgs.lastName)
        binding.editInputPhone.setText(navArgs.phone)
        check()
        binding.editInputFirstName.addTextChangedListener {
            firstNameBool = it!!.length > 3
            check()
        }
        binding.editInputLastName.addTextChangedListener {
            lastNameBool = it!!.length > 3
            check()
        }
        binding.editInputPhone.addTextChangedListener {
            phoneBool = it!!.startsWith("+998") && it.length == 13
            check()
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonEdit.setOnClickListener {
            viewModel.editContact(
                ContactUIData(
                    navArgs.id,
                    binding.editInputFirstName.text(),
                    binding.editInputLastName.text(),
                    binding.editInputPhone.text(),
                    StatusEnum.EDIT
                )
            )
            viewModel.closeScreen()
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
    }

    private fun check() {
        binding.buttonEdit.isEnabled = phoneBool && lastNameBool && firstNameBool
    }

    private val closeScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }
    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }
    private val progressObserver = Observer<Boolean> {
        if (it) binding.progress.show()
        else binding.progress.hide()
    }


}