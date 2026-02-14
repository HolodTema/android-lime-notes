package com.terabyte.realmnotes.ui.spinner

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.databinding.SpinnerItemCategoryBinding
import com.terabyte.realmnotes.domain.model.Category

class SpinnerCategoryAdapter(
    private val context: Context,
    private val categories: List<Category>,
) : ArrayAdapter<Category>(context, 0, categories) {

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return initView(position, parent)
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SpinnerItemCategoryBinding.inflate(layoutInflater, parent, false)
        val category = categories[position]

        if (category.id == null) {
            binding.textCategoryName.text = context.getString(R.string.no_category)
            binding.textCategoryName.setCompoundDrawables(null, null, null, null)
        } else {
            binding.textCategoryName.text = category.name
            binding.textCategoryName.compoundDrawableTintList =
                ColorStateList.valueOf(category.color)
        }
        return binding.root
    }
}