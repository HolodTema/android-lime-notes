package com.terabyte.realmnotes.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.application.MyApplication
import com.terabyte.realmnotes.databinding.ActivityMainBinding
import com.terabyte.realmnotes.ui.fragment.CategoryListFragment
import com.terabyte.realmnotes.ui.fragment.NoteListFragment
import com.terabyte.realmnotes.ui.fragment.SettingsFragment
import com.terabyte.realmnotes.ui.viewmodel.MainFragmentState
import com.terabyte.realmnotes.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        val noteRepository = (application as MyApplication).noteRepository
        val dataStoreRepository = (application as MyApplication).dataStoreRepository
        val factory = MainViewModel.Factory(noteRepository, dataStoreRepository)
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowMainFragment.collect {
                    setFragment(it)
                    setToolbarHeader(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        configureNavigationView()
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerMain.openDrawer(GravityCompat.START)
        }
    }

    private fun setFragment(mainFragmentState: MainFragmentState) {
        val isFragmentAlreadySet = supportFragmentManager
            .findFragmentById(R.id.frame_main_fragment_container) != null

        val fragment = when (mainFragmentState) {
            MainFragmentState.FRAGMENT_NOTE_LIST -> {
                NoteListFragment.newInstance()
            }

            MainFragmentState.FRAGMENT_CATEGORY_LIST -> {
                CategoryListFragment.newInstance()
            }

            MainFragmentState.FRAGMENT_SETTINGS -> {
                SettingsFragment.newInstance()
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        if (isFragmentAlreadySet) {
            transaction.replace(R.id.frame_main_fragment_container, fragment)
        } else {
            transaction.add(R.id.frame_main_fragment_container, fragment)
        }
        transaction.commit()
    }

    private fun setToolbarHeader(mainFragmentState: MainFragmentState) {
        binding.toolbar.title = when (mainFragmentState) {
            MainFragmentState.FRAGMENT_NOTE_LIST -> {
                getString(R.string.notes)
            }

            MainFragmentState.FRAGMENT_CATEGORY_LIST -> {
                getString(R.string.categories)
            }

            MainFragmentState.FRAGMENT_SETTINGS -> {
                getString(R.string.settings)
            }
        }
    }

    private fun configureNavigationView() {
        val currentNavigationMenuItemId = when(viewModel.stateFlowMainFragment.value) {
            MainFragmentState.FRAGMENT_SETTINGS -> {
                R.id.menu_item_settings
            }
            MainFragmentState.FRAGMENT_NOTE_LIST -> {
                R.id.menu_item_note_list
            }
            MainFragmentState.FRAGMENT_CATEGORY_LIST -> {
                R.id.menu_item_category_list
            }
        }
        binding.navigationViewMain.setCheckedItem(currentNavigationMenuItemId)
        binding.navigationViewMain.setNavigationItemSelectedListener { menuItem ->
            val mainFragmentState = when (menuItem.itemId) {
                R.id.menu_item_note_list -> {
                    MainFragmentState.FRAGMENT_NOTE_LIST
                }

                R.id.menu_item_category_list -> {
                    MainFragmentState.FRAGMENT_CATEGORY_LIST
                }

                R.id.menu_item_settings -> {
                    MainFragmentState.FRAGMENT_SETTINGS
                }

                else -> {
                    MainFragmentState.FRAGMENT_NOTE_LIST
                }
            }
            viewModel.setMainFragmentState(mainFragmentState)
            binding.drawerMain.closeDrawer(GravityCompat.START)
            true
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}