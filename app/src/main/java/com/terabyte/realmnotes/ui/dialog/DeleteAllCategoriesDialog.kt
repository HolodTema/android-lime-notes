package com.terabyte.realmnotes.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.databinding.DialogChangeColorBinding
import com.terabyte.realmnotes.databinding.DialogDeleteAllCategoriesBinding
import com.terabyte.realmnotes.databinding.DialogDeleteAllNotesBinding

class DeleteAllCategoriesDialog : DialogFragment() {
    private lateinit var binding: DialogDeleteAllCategoriesBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteAllCategoriesBinding.inflate(layoutInflater)

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonDelete.setOnClickListener {
            parentFragmentManager.setFragmentResult(REQUEST_KEY_DELETE_ALL_CATEGORIES, Bundle())
            dismiss()
        }

        return AlertDialog.Builder(requireContext(), R.style.style_transparent_alert_dialog)
            .setView(binding.root)
            .setCancelable(true)
            .create()
    }

    companion object {
        const val REQUEST_KEY_DELETE_ALL_CATEGORIES = "requestKeyDeleteAllNotes"
        fun newInstance(): DeleteAllCategoriesDialog {
            return DeleteAllCategoriesDialog()
        }
    }
}