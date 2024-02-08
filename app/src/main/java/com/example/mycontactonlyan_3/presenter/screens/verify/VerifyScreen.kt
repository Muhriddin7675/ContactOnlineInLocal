package com.example.mycontactonlyan_3.presenter.screens.verify

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.databinding.ScreenVerifyBinding
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_verify) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel by viewModels<VerifyViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.openContactScreen.observe(this, openContactScreenObserver)
        viewModel.noConnection.observe(this, noConnectionObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.noConnection()
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)

        binding.btnSubmit.setOnClickListener {
            viewModel.verify(binding.editPhone.text(), binding.editCode.text().toInt())
        }
        binding.refreshBtn.setOnClickListener {
            viewModel.noConnection()
        }
    }

    private val openContactScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_verifyScreen_to_contactScreen)
    }
    private val noConnectionObserver = Observer<Boolean> {
        binding.noConnection.isVisible = it
    }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            if (it) {
                binding.btnSubmit.visibility = View.INVISIBLE
                binding.frameLoading.isVisible = true
                binding.progress.show()
            } else {
                binding.btnSubmit.visibility = View.VISIBLE
                binding.frameLoading.isVisible = false
                binding.progress.hide()
            }
        }
    }

}