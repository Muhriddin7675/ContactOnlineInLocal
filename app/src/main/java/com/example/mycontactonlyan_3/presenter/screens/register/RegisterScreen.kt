package com.example.mycontactonlyan_3.presenter.screens.register

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.databinding.ScreenRegisterBinding
import com.example.mycontactonlyan_3.utils.showToast
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {
    private val binding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel by viewModels<RegisterViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.noInternetConnection.observe(this, noInternetConnectionObserver)
        viewModel.openVerifyScreen.observe(this, openVerifyScreenObserver)
        viewModel.messageError.observe(this, messageErrorObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.internetConnection()
        binding.next.setOnClickListener {
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

    private val openVerifyScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_registerScreen_to_verifyScreen)
    }
    private val messageErrorObserver = Observer<String> {
        showToast(it)
    }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.next.visibility = View.INVISIBLE
            binding.frameLoading.isVisible = true
            binding.progress.show()
        } else {
            binding.next.visibility = View.VISIBLE
            binding.frameLoading.isVisible = false
            binding.progress.hide()
        }
    }
    private val noInternetConnectionObserver = Observer<Boolean> {
        binding.noConnection.isVisible = it
    }
}