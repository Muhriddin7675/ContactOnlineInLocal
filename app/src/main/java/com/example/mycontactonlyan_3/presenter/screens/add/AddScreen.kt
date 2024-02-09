package com.example.mycontactonlyan_3.presenter.screens.add

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.databinding.ScreenContactAddBinding
import com.example.mycontactonlyan_3.utils.showToast
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class AddScreen : Fragment(R.layout.screen_contact_add) {
    private val binding by viewBinding(ScreenContactAddBinding::bind)
    private val viewModel by viewModels<AddContactViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonBack.setOnClickListener {
            viewModel.closeScreen()
        }
        combine(
            binding.editInputFirstName.textChanges().map { it.length > 3 },
            binding.editInputLastName.textChanges().map { it.length > 3 },
            binding.editInputPhone.textChanges().map { it.length == 13 && it.startsWith("+998") },
            transform = { firstName, lastName, phone -> firstName && lastName && phone }
        ).onEach { binding.buttonAdd.isEnabled = it }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        binding.buttonAdd.setOnClickListener {
            viewModel.addContact(
                binding.editInputFirstName.text(),
                binding.editInputLastName.text(),
                binding.editInputPhone.text()
            )
        }
        viewModel.progressLiveData.onEach {
            if (it) {
                binding.buttonAdd.isVisible = false
                binding.frameLoading.isVisible = true
                binding.progress.show()
            } else {
                binding.buttonAdd.isVisible = true
                binding.frameLoading.isVisible = false
                binding.progress.hide()
            }
        }.launchIn(lifecycleScope)

        viewModel.errorMessageLiveData.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)
    }
}