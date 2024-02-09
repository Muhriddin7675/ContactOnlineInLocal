package com.example.mycontactonlyan_3.presenter.screens.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.databinding.ScreenLoginBinding
import com.example.mycontactonlyan_3.utils.showToast
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChangeEvents
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        combine(
            binding.editPhone.textChanges().map { it.length == 13 && it.startsWith("+998") },
            binding.editPassword.textChanges().map { it.length in 8..20 },
            transform = { email, password -> email && password }
        ).onEach { binding.btnLogin.isEnabled = it }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.progressLiveData.onEach {
            if (it) {
                binding.btnLogin.visibility = View.INVISIBLE
                binding.frameLoading.isVisible = true
                binding.progress.show()
            } else {
                binding.btnLogin.visibility = View.VISIBLE
                binding.frameLoading.isVisible = false
                binding.progress.hide()
            }
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)


        viewModel.errorMessage.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)

        viewModel.refreshInternetConnection.onEach {
            binding.noConnection.isVisible = it
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.networkConnect()

        binding.btnLogin.setOnClickListener {
            viewModel.networkConnect()
            viewModel.login(binding.editPhone.text(), binding.editPassword.text())
        }
        binding.btnSignUp.setOnClickListener {
            viewModel.openRegisterScreen()
        }
        binding.refreshBtn.setOnClickListener {
            viewModel.networkConnect()
        }
    }

}