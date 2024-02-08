package com.example.mycontactonlyan_3.presenter.screens.add

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.databinding.ScreenContactAddBinding
import com.example.mycontactonlyan_3.utils.showToast
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScreen : Fragment(R.layout.screen_contact_add) {
    private val binding by viewBinding(ScreenContactAddBinding::bind)
    private val viewModel by viewModels<AddContactViewModelImpl>()

    private var firstNameBool = false
    private var lastNameBool = false
    private var phoneBool = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.errorMessageLiveData.observe(this, errorMessageObserver)
        viewModel.closeScreenLiveData.observe(this, closeScreenObserver)
        viewModel.messageLiveData.observe(this, messageObserver)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonAdd.setOnClickListener {
            viewModel.addContact(
                binding.editInputFirstName.text(),
                binding.editInputLastName.text(),
                binding.editInputPhone.text()
            )
        }
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
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
    }

    private fun check() {
        binding.buttonAdd.isEnabled = firstNameBool && lastNameBool && phoneBool
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }
    private val closeScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }
    private val messageObserver = Observer<String> {
        showToast(it)
    }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.buttonAdd.isVisible = false
            binding.frameLoading.isVisible = true
            binding.progress.show()
        } else {
            binding.buttonAdd.isVisible = true
            binding.frameLoading.isVisible = false
            binding.progress.hide()
        }
    }
}