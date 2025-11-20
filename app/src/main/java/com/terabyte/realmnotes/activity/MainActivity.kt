package com.terabyte.realmnotes.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.databinding.ActivityMainBinding
import com.terabyte.realmnotes.fragment.CategoryListFragment
import com.terabyte.realmnotes.fragment.NoteListFragment
import com.terabyte.realmnotes.fragment.SettingsFragment
import com.terabyte.realmnotes.util.makeShortToast
import com.terabyte.realmnotes.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        binding.navigationViewMain.setNavigationItemSelectedListener { menuItem ->
            viewModel.setFragmentMenuItemId(menuItem.itemId)
            viewModel.setNavViewExpanded(false)
            true
        }


        binding.toolbar.setNavigationOnClickListener {
            val isExpanded = viewModel.liveDataNavViewExpanded.value ?: false
            viewModel.setNavViewExpanded(!isExpanded)
        }

        viewModel.liveDataFragmentMenuItemId.observe(this) { menuItemId ->
            setFragment(menuItemId)
            setToolbarHeader(menuItemId)
        }

        viewModel.liveDataNavViewExpanded.observe(this) { isExpanded ->
            if (isExpanded) {
                binding.drawerMain.openDrawer(GravityCompat.START)
            }
            else {
                binding.drawerMain.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun setFragment(menuItemId: Int) {
        val isFragmentAlreadySet = supportFragmentManager
            .findFragmentById(R.id.frame_main_fragment_container) != null

        val fragment = when(menuItemId) {
            R.id.menu_item_note_list -> {
                NoteListFragment.newInstance()
            }
            R.id.menu_item_category_list -> {
                CategoryListFragment.newInstance()
            }
            R.id.menu_item_settings -> {
                SettingsFragment.newInstance()
            }
            else -> {
                NoteListFragment.newInstance()
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        if (isFragmentAlreadySet) {
            transaction.replace(R.id.frame_main_fragment_container, fragment)
        }
        else {
            transaction.add(R.id.frame_main_fragment_container, fragment)
        }
        transaction.commit()
    }

    private fun setToolbarHeader(menuItemId: Int) {
        binding.toolbar.title = when(menuItemId) {
            R.id.menu_item_note_list -> {
                getString(R.string.notes)
            }
            R.id.menu_item_category_list -> {
                getString(R.string.categories)
            }
            R.id.menu_item_settings -> {
                getString(R.string.settings)
            }
            else -> {
                getString(R.string.notes)
            }
        }
    }
}