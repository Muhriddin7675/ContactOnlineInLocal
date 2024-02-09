package com.example.mycontactonlyan_3.presenter.screens.contact

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.data.model.MyShared
import com.example.mycontactonlyan_3.databinding.ScreenContactBinding
import com.example.mycontactonlyan_3.presenter.adapter.ContactAdapter
import com.example.mycontactonlyan_3.presenter.dialog.BottomSheetDialog
import com.example.mycontactonlyan_3.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ContactScreen : Fragment(R.layout.screen_contact) {
    private val binding by viewBinding(ScreenContactBinding::bind)
    private val viewModel: ContactViewModel by viewModels<ContactViewModelImpl>()
    private val adapter by lazy { ContactAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadContacts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvContact.adapter = adapter
        binding.rvContact.layoutManager = LinearLayoutManager(requireContext())

        adapter.setLongItemClick{
            viewModel.openBottomSheetDialog(it)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadContacts()
        }
        binding.buttonRefresh.setOnClickListener { viewModel.loadContacts() }
        binding.btnAdd.setOnClickListener { viewModel.openAddContactScreen() }
        binding.logAuth.setOnClickListener { MyShared.setToken("1")
            viewModel.openLoginScreen() }

        viewModel.contactsStateFlow.onEach {
            adapter.submitList(it)
            binding.containerEmpty.isVisible = false
        }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)


        viewModel.emptyStateStateFlow.onEach { binding.containerEmpty.isVisible = true }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.errorMessageLiveData
            .onEach { showToast(it) }.launchIn(lifecycleScope)

        viewModel.progressLiveData.onEach {
            binding.refreshLayout.isRefreshing = it
            binding.containerEmpty.isVisible = false
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.openBottomSheetDialog =  {
            val dialog = BottomSheetDialog()
            dialog.show(parentFragmentManager, "bottom dialog")

            dialog.setOnClickDelete {
                viewModel.deleteClick(it)
            }

            dialog.setOnClickEdit {
                viewModel.openEditContactScreen(it)
            }
        }

    }
}