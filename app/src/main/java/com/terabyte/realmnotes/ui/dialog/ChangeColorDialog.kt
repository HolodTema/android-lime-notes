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

class ChangeColorDialog : DialogFragment() {
    private lateinit var binding: DialogChangeColorBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogChangeColorBinding.inflate(layoutInflater)

        val startColor = arguments?.getInt(BUNDLE_KEY_START_COLOR) ?: R.color.gray

        val indicators = arrayOf(
            binding.imageGray,
            binding.imageRed,
            binding.imageOrange,
            binding.imageYellow,
            binding.imageGreen,
            binding.imageLightBlue,
            binding.imageBlue,
            binding.imageViolet,
            binding.imagePink,
            binding.imageDarkGreen,
            binding.imageBrown,
            binding.imageCherry,
        )

        indicators.forEach { indicator ->
            indicator.setOnClickListener { clickedIndicator ->
                val color = getBackgroundTintColor(clickedIndicator)
                if (color != null) {
                    setFragmentResult(color)
                }

                indicators.forEach { indicator ->
                    setIndicatorSelection(indicator, clickedIndicator)
                }
            }

            setIndicatorSelection(indicator, startColor)
        }

        binding.textDone.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(true)
            .create()
    }

    private fun getBackgroundTintColor(view: View): Int? {
        return view.backgroundTintList?.defaultColor
    }

    private fun setIndicatorSelection(indicator: View, selectedIndicator: View) {
        if (indicator.id == selectedIndicator.id) {
            indicator.foreground = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.background_color_indicator
            )
        }
        else {
            indicator.foreground = Color.TRANSPARENT.toDrawable()
        }
    }

    private fun setIndicatorSelection(indicator: View, selectedIndicatorColor: Int) {
        if (getBackgroundTintColor(indicator) == selectedIndicatorColor) {
            indicator.foreground = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.background_color_indicator
            )
        }
        else {
            indicator.foreground = Color.TRANSPARENT.toDrawable()
        }
    }

    private fun setFragmentResult(color: Int) {
        val bundle = Bundle().apply {
            putInt(BUNDLE_KEY_RESULT_COLOR, color)
        }
        parentFragmentManager.setFragmentResult(REQUEST_KEY_RESULT_COLOR, bundle)
    }

    companion object {
        const val REQUEST_KEY_RESULT_COLOR = "requestKeyResultColor"
        const val BUNDLE_KEY_RESULT_COLOR = "bundleKeyResultColor"

        private const val BUNDLE_KEY_START_COLOR = "bundleKeyStartColor"

        fun newInstance(startColor: Int): ChangeColorDialog {
            val bundle = Bundle()
            bundle.putInt(BUNDLE_KEY_START_COLOR, startColor)

            return ChangeColorDialog().apply {
                arguments = bundle
            }
        }
    }
}