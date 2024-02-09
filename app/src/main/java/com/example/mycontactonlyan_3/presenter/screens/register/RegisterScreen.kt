package com.example.mycontactonlyan_3.presenter.screens.register

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.databinding.ScreenRegisterBinding
import com.example.mycontactonlyan_3.utils.showToast
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {
    private val binding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel by viewModels<RegisterViewModelImpl>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        combine(
            binding.editFirstName.textChanges().map { it.length > 3 },
            binding.editLastName.textChanges().map { it.length > 3 },
            binding.editPassword.textChanges().map { it.length in 8..20 },
            binding.editPhone.textChanges().map { it.length == 13 && it.startsWith("+998") },
            transform = { firstName, lastName, phone, password -> firstName && lastName && phone && password }
        ).onEach { binding.nextBtn.isEnabled = it }
//            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.progressLiveData.onEach {
            if (it) {
                binding.nextBtn.visibility = View.INVISIBLE
                binding.frameLoading.isVisible = true
                binding.progress.show()
            } else {
                binding.nextBtn.visibility = View.VISIBLE
                binding.frameLoading.isVisible = false
                binding.progress.hide()
            }
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.messageError.onEach { showToast(it) }
            .launchIn(lifecycleScope)

        viewModel.noInternetConnection.onEach {
            binding.noConnection.isVisible = it
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.internetConnection()
        binding.nextBtn.setOnClickListener {
            viewModel.internetConnection()
            viewModel.register(
                binding.editFirstName.text(),
                binding.editLastName.text(),
                binding.editPhone.text(),
                binding.editPassword.text()
            )
        }
        binding.refreshBtn.setOnClickListener {
            viewModel.internetConnection()
        }
    }
}