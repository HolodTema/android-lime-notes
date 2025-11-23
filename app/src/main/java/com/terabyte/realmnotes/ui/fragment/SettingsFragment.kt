package com.terabyte.realmnotes.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.application.MyApplication
import com.terabyte.realmnotes.databinding.FragmentSettingsBinding
import com.terabyte.realmnotes.ui.viewmodel.MainViewModel
import com.terabyte.realmnotes.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by lazy {
        val application = (requireContext().applicationContext) as MyApplication
        val factory = SettingsViewModel.Factory(application.dataStoreRepository)
        ViewModelProvider(this, factory)[SettingsViewModel::class]
    }

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowIsDarkTheme.collect {
                    binding.switchDarkTheme.isChecked = it
                }
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsDarkTheme(isChecked)
        }

        binding.buttonDeleteAllNotes.setOnClickListener {
            showDeleteAllNotesDialog()
        }

        binding.buttonDeleteAllCategories.setOnClickListener {
            showDeleteAllCategoriesDialog()
        }
    }

    private fun showDeleteAllNotesDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_all_notes_title))
            .setMessage(getString(R.string.dialog_delete_all_notes_msg))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.dialog_button_delete)) { _, _ ->
                mainViewModel.deleteAllNotes()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun showDeleteAllCategoriesDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_all_categories_title))
            .setMessage(getString(R.string.dialog_delete_all_categories_msg))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.dialog_button_delete)) { _, _ ->
                mainViewModel.deleteAllCategories()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}