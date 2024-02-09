package com.example.mycontactonlyan_3.presenter.screens.edit

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChanges


@AndroidEntryPoint
class EditScreen : Fragment(R.layout.screen_contact_edit) {
    private val binding by viewBinding(ScreenContactEditBinding::bind)
    private val viewModel by viewModels<EditContactModelImpl>()
    private val navArgs: EditScreenArgs by navArgs()
    private var firstNameBool = true
    private var lastNameBool = true
    private var phoneBool = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.editInputFirstName.setText(navArgs.firstName)
        binding.editInputLastName.setText(navArgs.lastName)
        binding.editInputPhone.setText(navArgs.phone)
//        check()
//        binding.editInputFirstName.addTextChangedListener {
//            firstNameBool = it!!.length > 3
//            check()
//        }
//        binding.editInputLastName.addTextChangedListener {
//            lastNameBool = it!!.length > 3
//            check()
//        }
//        binding.editInputPhone.addTextChangedListener {
//            phoneBool = it!!.startsWith("+998") && it.length == 13
//            check()
//        }

        combine(
            binding.editInputFirstName.textChanges().map { it.length > 3 },
            binding.editInputLastName.textChanges().map { it.length > 3 },
            binding.editInputPhone.textChanges().map { it.length == 13 && it.startsWith("+998") },
            transform = { firstName, lastName, phone -> firstName && lastName && phone }
        ).onEach {
            binding.buttonEdit.isEnabled = it }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
        binding.buttonBack.setOnClickListener {
            viewModel.closeScreen()
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

        viewModel.progressLiveData.onEach {
            if (it) binding.progress.show()
            else binding.progress.hide()
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.errorMessageLiveData.onEach { showToast(it) }.launchIn(lifecycleScope)
    }

//    private fun check() {
//        binding.buttonEdit.isEnabled = phoneBool && lastNameBool && firstNameBool
//    }
}