package com.terabyte.realmnotes.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.application.MyApplication
import com.terabyte.realmnotes.databinding.ActivityCategoryDetailsBinding
import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.ui.dialog.ChangeColorDialog
import com.terabyte.realmnotes.ui.viewmodel.CategoryDetailsState
import com.terabyte.realmnotes.ui.viewmodel.CategoryDetailsViewModel
import kotlinx.coroutines.launch

class CategoryDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryDetailsBinding

    private val viewModel: CategoryDetailsViewModel by lazy {
        val noteRepository = (application as MyApplication).noteRepository
        val factory = CategoryDetailsViewModel.Factory(noteRepository)
        ViewModelProvider(this, factory)[CategoryDetailsViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(INTENT_KEY_CATEGORY, Category::class.java)
        } else {
            intent.getSerializableExtra(INTENT_KEY_CATEGORY) as Category
        }
        category?.let {
            if (viewModel.stateFlowCategoryDetails.value == CategoryDetailsState.ADD_CATEGORY) {
                viewModel.setStateUpdate(category)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowCategoryDetails.collect { state ->
                    binding.toolbar.title = when (state) {
                        CategoryDetailsState.ADD_CATEGORY -> {
                            getString(R.string.category_details_header_insert)
                        }

                        CategoryDetailsState.UPDATE_CATEGORY -> {
                            getString(R.string.category_details_header_update)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowCategory.collect { category ->
                    binding.editCategoryName.setText(category.name)
                    binding.imageCategoryIcon.imageTintList = ColorStateList.valueOf(category.color)
                }
            }
        }

        setSupportActionBar(binding.toolbar)
        configureOnBackPressed()
    }

    override fun onStart() {
        super.onStart()

        binding.toolbar.setNavigationOnClickListener {
            viewModel.saveCategory {
                startActivity(MainActivity.newIntent(this))
            }
        }

        binding.buttonChangeColor.setOnClickListener {
            val dialog = ChangeColorDialog.newInstance(viewModel.stateFlowCategory.value.color)
            dialog.show(supportFragmentManager, DIALOG_TAG_CHANGE_COLOR)
        }

        binding.editCategoryName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.updateCategoryName(s.toString())
            }
        })

        supportFragmentManager.setFragmentResultListener(
            ChangeColorDialog.REQUEST_KEY_RESULT_COLOR,
            this
        ) { _, bundle ->
            if (bundle.containsKey(ChangeColorDialog.BUNDLE_KEY_RESULT_COLOR)) {
                val resultColor = bundle.getInt(ChangeColorDialog.BUNDLE_KEY_RESULT_COLOR)
                viewModel.updateCategoryColor(resultColor)
                binding.imageCategoryIcon.imageTintList = ColorStateList.valueOf(resultColor)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (viewModel.stateFlowCategoryDetails.value == CategoryDetailsState.UPDATE_CATEGORY) {
            menuInflater.inflate(R.menu.menu_category_details_toolbar, menu)
            return true
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_delete_category) {
            viewModel.deleteCategory {
                startActivity(MainActivity.newIntent(this))
            }
            return true
        }

        return false
    }

    private fun configureOnBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.saveCategory {
                    startActivity(MainActivity.newIntent(this@CategoryDetailsActivity))
                }
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    companion object {
        const val DIALOG_TAG_CHANGE_COLOR = "ChangeColorDialog"

        const val INTENT_KEY_CATEGORY = "intentKeyCategory"

        fun newIntent(context: Context): Intent {
            return Intent(context, CategoryDetailsActivity::class.java)
        }

        fun newIntent(context: Context, category: Category): Intent {
            return Intent(context, CategoryDetailsActivity::class.java).apply {
                putExtra(INTENT_KEY_CATEGORY, category)
            }
        }
    }
}