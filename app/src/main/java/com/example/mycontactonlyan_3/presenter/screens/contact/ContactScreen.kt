package com.example.mycontactonlyan_3.presenter.screens.contact

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mycontactonlyan_3.R
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.data.model.MyShared
import com.example.mycontactonlyan_3.databinding.ScreenContactBinding
import com.example.mycontactonlyan_3.presenter.adapter.ContactAdapter
import com.example.mycontactonlyan_3.presenter.dialog.BottomSheetDialog
import com.example.mycontactonlyan_3.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactScreen : Fragment(R.layout.screen_contact) {
    private val binding by viewBinding(ScreenContactBinding::bind)
    private val viewModel: ContactViewModel by viewModels<ContactViewModelImpl>()
    private val adapter by lazy { ContactAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadContacts()
        viewModel.openAddContactScreenLiveData.observe(this, openAddContactObserver)
        viewModel.errorMessageLiveData.observe(this, errorMessageObserver)
        viewModel.notConnectionLiveData.observe(this, notConnectionObserver)
        viewModel.openBottomSheetDialog.observe(this, openBottomSheetDialogObserver)
        viewModel.openEditContactScreenLiveData.observe(this, openEditContactScreenObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvContact.adapter = adapter
        binding.rvContact.layoutManager = LinearLayoutManager(requireContext())

        adapter.setLongItemClick {
            viewModel.openBottomSheetDialog(it)
//            Log.d("TTT", "addContact:${it.id} ${it.firstName} ${it.lastName} ${it.phone} ")

        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadContacts()
        }
        binding.buttonRefresh.setOnClickListener { viewModel.loadContacts() }

        binding.btnAdd.setOnClickListener { viewModel.openAddContactScreen() }

        binding.logAuth.setOnClickListener {
            MyShared.setToken("1")
            findNavController().navigate(R.id.action_contactScreen_to_loginScreen)
        }

        viewModel.emptyStateLiveData.observe(viewLifecycleOwner, emptyStateObserver)
        viewModel.contactsLiveData.observe(viewLifecycleOwner, contactsObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
    }

    private val openAddContactObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_contactScreen_to_addScreen)
    }
    private val openEditContactScreenObserver = Observer<ContactUIData> {
        findNavController().navigate(ContactScreenDirections.actionContactScreenToEditScreen(
                it.firstName,
                it.lastName,
                it.phone,
                it.id
            )
        )
    }
    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }
    private val notConnectionObserver = Observer<Unit> {
        showToast("Not Connection !")
    }
    private val emptyStateObserver = Observer<Unit> {
        binding.containerEmpty.isVisible = true
    }
    private val contactsObserver = Observer<List<ContactUIData>> { list ->
        adapter.submitList(list)
        binding.containerEmpty.isVisible = false
    }
    private val openBottomSheetDialogObserver = Observer<ContactUIData> { data ->
        val dialog = BottomSheetDialog()

        dialog.show(parentFragmentManager, "bottom dialog")

        dialog.setOnClickDelete {
            viewModel.deleteClick(data)
        }

        dialog.setOnClickEdit {
            viewModel.editClick(data)
        }
    }
    private val progressObserver = Observer<Boolean> {
        binding.refreshLayout.isRefreshing = it
        binding.containerEmpty.isVisible = false
    }
}