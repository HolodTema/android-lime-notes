package com.terabyte.realmnotes.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.realmnotes.databinding.ListItemCategoryBinding
import com.terabyte.realmnotes.domain.model.Category


class CategoryDiffUtilItemCallback : DiffUtil.ItemCallback<Category>() {
    override fun areContentsTheSame(
        oldItem: Category,
        newItem: Category
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: Category,
        newItem: Category
    ): Boolean {
        return oldItem.id == newItem.id
    }
}

class CategoryHolder(
    private val binding: ListItemCategoryBinding,
    private val categorySelectedListener: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(category: Category) {
        binding.textCategoryName.text = category.name
//        binding.imageCategoryIcon.tint

        binding.root.setOnClickListener {
            categorySelectedListener(category)
        }
    }

}

class CategoryAdapter(
    private val inflater: LayoutInflater,
    private val categorySelectedListener: (Category) -> Unit
) :
    ListAdapter<Category, CategoryHolder>(CategoryDiffUtilItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryHolder {
        val binding = ListItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryHolder(binding, categorySelectedListener)
    }

    override fun onBindViewHolder(
        holder: CategoryHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}