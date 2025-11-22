package com.terabyte.realmnotes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.terabyte.realmnotes.databinding.FragmentCategoryListBinding
import com.terabyte.realmnotes.ui.recycler.CategoryAdapter
import com.terabyte.realmnotes.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class CategoryListFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentCategoryListBinding

    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false)

        adapter = CategoryAdapter(layoutInflater) {

        }
        binding.recyclerCategories.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowCategoryList.collect { categories ->
                    adapter.submitList(categories)
                    if (categories.isEmpty()) {
                        binding.recyclerCategories.visibility = View.GONE
                        binding.textNoCategories.visibility = View.VISIBLE
                    }
                    else {
                        binding.recyclerCategories.visibility = View.VISIBLE
                        binding.textNoCategories.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.buttonAddCategory.setOnClickListener {
//            startActivity()
        }
    }

    companion object {

        fun newInstance(): CategoryListFragment {
            return CategoryListFragment()
        }
    }
}