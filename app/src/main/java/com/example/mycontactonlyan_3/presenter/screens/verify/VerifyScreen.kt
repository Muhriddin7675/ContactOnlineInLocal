package com.example.mycontactonlyan_3.presenter.screens.verify

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.databinding.ScreenVerifyBinding
import com.example.mycontactonlyan_3.utils.showToast
import com.example.mycontactonlyan_3.utils.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_verify) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyViewModel by viewModels<VerifyViewModelImpl>()
    private val navArgs:VerifyScreenArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        combine(
            binding.editCode.textChanges().map { it.length == 6 },
            transform = {smsCode -> smsCode}
        ).onEach {
            binding.btnSubmit.isEnabled = it[0]
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)


        viewModel.errorMessage.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)


        viewModel.noConnection.onEach {
            binding.noConnection.isVisible = it
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)


        viewModel.progressLiveData.onEach {
            if (it) {
                binding.btnSubmit.visibility = View.INVISIBLE
                binding.frameLoading.isVisible = true
                binding.progress.show()
            } else {
                binding.btnSubmit.visibility = View.VISIBLE
                binding.frameLoading.isVisible = false
                binding.progress.hide()
            }
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)



        viewModel.noConnection()
        binding.btnSubmit.setOnClickListener {
            viewModel.noConnection()
            viewModel.verify(navArgs.phone, binding.editCode.text().toInt())
        }
        binding.refreshBtn.setOnClickListener {
            viewModel.noConnection()
        }
    }
}