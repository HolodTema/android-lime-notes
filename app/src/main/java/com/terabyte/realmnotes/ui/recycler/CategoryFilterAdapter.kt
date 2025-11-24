package com.terabyte.realmnotes.ui.recycler

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.realmnotes.databinding.ListItemCategoryFilterBinding
import com.terabyte.realmnotes.domain.model.Category


class CategoryFilterHolder(
    private val binding: ListItemCategoryFilterBinding,
    private val adapter: CategoryFilterAdapter,
    private val isDarkMode: Boolean,
    private val categorySelectedListener: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private val defaultCardColor = binding.root.cardBackgroundColor
    private val defaultText = binding.textCategoryFilterName.text.toString()

    private val selectedColor = if (isDarkMode) {
        "#004B56".toColorInt()
    } else {
        "#3CEBFF".toColorInt()
    }

    fun bind(category: Category, position: Int) {
        if (category.id == null) {
            binding.imageCategoryIcon.visibility = View.GONE
            binding.textCategoryFilterName.text = defaultText
        } else {
            binding.imageCategoryIcon.visibility = View.VISIBLE
            binding.imageCategoryIcon.imageTintList = ColorStateList.valueOf(category.color)
            binding.textCategoryFilterName.text = category.name
        }

        if (position == adapter.selectedPosition) {
            binding.root.setCardBackgroundColor(selectedColor)
        } else {
            binding.root.setCardBackgroundColor(defaultCardColor)
        }

        binding.root.setOnClickListener {
            adapter.notifyItemChanged(adapter.selectedPosition)
            adapter.selectedPosition = position
            binding.root.setCardBackgroundColor(selectedColor)
            categorySelectedListener(position)
        }
    }

}

class CategoryFilterAdapter(
    private val inflater: LayoutInflater,
    private var categories: List<Category>,
    var selectedPosition: Int,
    private val isDarkMode: Boolean,
    private val categorySelectedListener: (Int) -> Unit
) : RecyclerView.Adapter<CategoryFilterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFilterHolder {
        val binding = ListItemCategoryFilterBinding.inflate(inflater, parent, false)
        return CategoryFilterHolder(binding, this, isDarkMode, categorySelectedListener)
    }

    override fun onBindViewHolder(holder: CategoryFilterHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, position)
    }

    override fun getItemCount() = categories.size

    fun resetSelectedPositionToZero() {
        notifyItemChanged(selectedPosition)
        selectedPosition = 0
        notifyItemChanged(selectedPosition)
    }
}