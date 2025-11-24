package com.terabyte.realmnotes.ui.fragment

import android.app.AlertDialog
import android.content.pm.PackageManager
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
import com.terabyte.realmnotes.ui.dialog.DeleteAllCategoriesDialog
import com.terabyte.realmnotes.ui.dialog.DeleteAllNotesDialog
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

        configureAppVersionText()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsDarkTheme(isChecked)
            mainViewModel.setUITheme()
        }

        binding.buttonDeleteAllNotes.setOnClickListener {
            showDeleteAllNotesDialog()
        }

        binding.buttonDeleteAllCategories.setOnClickListener {
            showDeleteAllCategoriesDialog()
        }

        parentFragmentManager.setFragmentResultListener(
            DeleteAllCategoriesDialog.REQUEST_KEY_DELETE_ALL_CATEGORIES,
            viewLifecycleOwner
        ) { _, _ ->
            mainViewModel.deleteAllCategories()
        }
        parentFragmentManager.setFragmentResultListener(
            DeleteAllNotesDialog.REQUEST_KEY_DELETE_ALL_NOTES,
            viewLifecycleOwner
        ) { _, _ ->
            mainViewModel.deleteAllNotes()
        }
    }

    private fun showDeleteAllNotesDialog() {
        val dialog = DeleteAllNotesDialog.newInstance()
        dialog.show(parentFragmentManager, DIALOG_TAG_DELETE_ALL_NOTES)
    }

    private fun showDeleteAllCategoriesDialog() {
        val dialog = DeleteAllCategoriesDialog.newInstance()
        dialog.show(parentFragmentManager, DIALOG_TAG_DELETE_ALL_CATEGORIES)
    }

    private fun configureAppVersionText() {
        try {
            val packageInfo = requireActivity().packageManager
                .getPackageInfo(requireContext().packageName, 0)

            val version = packageInfo.versionName

            binding.textAppVersion.text = getString(R.string.app_version, version)
        } catch (e: PackageManager.NameNotFoundException) {
            binding.textAppVersion.visibility = View.GONE
        }
    }

    companion object {
        private const val DIALOG_TAG_DELETE_ALL_NOTES = "dialogTagDeleteAllNotes"
        private const val DIALOG_TAG_DELETE_ALL_CATEGORIES = "dialogTagDeleteAllCategories"

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}