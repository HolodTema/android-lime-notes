package com.terabyte.realmnotes.ui.recycler

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.realmnotes.databinding.ListItemCategoryFilterBinding
import com.terabyte.realmnotes.domain.model.Category


class CategoryFilterHolder(private val binding: ListItemCategoryFilterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(category: Category) {
        if (category.id == null) {
            binding.textCategoryFilterName.setCompoundDrawables(null, null, null, null)
        } else {
            binding.textCategoryFilterName.text = category.name
            binding.textCategoryFilterName.compoundDrawableTintList =
                ColorStateList.valueOf(category.color)
        }
    }

}

class CategoryFilterAdapter(private val inflater: LayoutInflater) :
    ListAdapter<Category, CategoryFilterHolder>(CategoryDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFilterHolder {
        val binding = ListItemCategoryFilterBinding.inflate(inflater, parent, false)
        return CategoryFilterHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryFilterHolder, position: Int) {
        holder.bind(getItem(position))
    }

}