package com.terabyte.realmnotes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.terabyte.realmnotes.databinding.FragmentCategoryListBinding

class CategoryListFragment: Fragment() {
    private lateinit var binding: FragmentCategoryListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {

        fun newInstance(): CategoryListFragment {
            return CategoryListFragment()
        }
    }
}