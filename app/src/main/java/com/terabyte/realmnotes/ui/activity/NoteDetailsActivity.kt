package com.terabyte.realmnotes.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.application.MyApplication
import com.terabyte.realmnotes.databinding.ActivityNoteDetailsBinding
import com.terabyte.realmnotes.domain.model.Note
import com.terabyte.realmnotes.ui.dialog.DeleteNoteDialog
import com.terabyte.realmnotes.ui.spinner.SpinnerCategoryAdapter
import com.terabyte.realmnotes.ui.viewmodel.NoteDetailsState
import com.terabyte.realmnotes.ui.viewmodel.NoteDetailsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class NoteDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteDetailsBinding

    private val viewModel: NoteDetailsViewModel by lazy {
        val noteRepository = (application as MyApplication).noteRepository
        val factory = NoteDetailsViewModel.Factory(noteRepository)
        ViewModelProvider(this, factory)[NoteDetailsViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(INTENT_KEY_NOTE, Note::class.java)
        } else {
            intent.getSerializableExtra(INTENT_KEY_NOTE) as Note?
        }
        note?.let {
            if (viewModel.stateFlowNoteDetails.value == NoteDetailsState.ADD_NOTE) {
                viewModel.setStateUpdate(it)
            }
        }

        setSupportActionBar(binding.toolbar)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowNoteDetails.collect { state ->
                    supportActionBar?.title = when (state) {
                        NoteDetailsState.ADD_NOTE -> {
                            getString(R.string.note_details_header_insert)
                        }

                        NoteDetailsState.UPDATE_NOTE -> {
                            getString(R.string.note_details_header_update)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowNote.collect { note ->
                    binding.editNoteText.setText(note.text)

                    val dateText =
                        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(note.date)
                    binding.toolbar.subtitle = getString(R.string.created_at, dateText)

                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowCategoryList.collect { categories ->
                    binding.spinnerNoteCategory.adapter =
                        SpinnerCategoryAdapter(this@NoteDetailsActivity, layoutInflater, categories)
                    val categorySelected =
                        categories.find { it.id == viewModel.stateFlowNote.value.categoryId }
                    if (categorySelected == null) {
                        binding.spinnerNoteCategory.setSelection(0)
                    } else {
                        val positionSelected = categories.indexOf(categorySelected)
                        binding.spinnerNoteCategory.setSelection(positionSelected)
                    }
                }
            }
        }

        binding.spinnerNoteCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val category = viewModel.stateFlowCategoryList.value.getOrNull(position)
                    viewModel.updateNoteCategoryId(category?.id)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.updateNoteCategoryId(null)
                }
            }

        configureOnBackPressed()
    }

    override fun onStart() {
        super.onStart()
        binding.toolbar.setNavigationOnClickListener {
            it.isEnabled = false
            viewModel.saveNote {
                startActivity(MainActivity.newIntent(this))
            }
        }

        binding.editNoteText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.updateNoteText(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.buttonShare.setOnClickListener {
            if (viewModel.stateFlowNote.value.text.isNotBlank()) {
                shareNoteText()
            }
        }

        binding.buttonCopyNoteText.setOnClickListener {
            if (viewModel.stateFlowNote.value.text.isNotBlank()) {
                copyNoteTextToClipboard()
                Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show()
            }
        }

        supportFragmentManager.setFragmentResultListener(
            DeleteNoteDialog.REQUEST_KEY_DELETE_NOTE,
            this
        ) { _, _ ->
            viewModel.deleteNote {
                startActivity(MainActivity.newIntent(this))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (viewModel.stateFlowNoteDetails.value == NoteDetailsState.UPDATE_NOTE) {
            menuInflater.inflate(R.menu.menu_note_details_toolbar, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_delete_note) {
            showDeleteNoteDialog()
        }
        return true
    }

    private fun configureOnBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.saveNote {
                    startActivity(MainActivity.newIntent(this@NoteDetailsActivity))
                }
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun copyNoteTextToClipboard() {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val label = getString(R.string.clipboard_note_text_label)
        val clip = ClipData.newPlainText(label, viewModel.stateFlowNote.value.text)
        clipboardManager.setPrimaryClip(clip)
    }

    private fun shareNoteText() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, viewModel.stateFlowNote.value.text)

        val intentChooser = Intent.createChooser(intent, getString(R.string.share_note_text_title))
        startActivity(intentChooser)
    }

    private fun showDeleteNoteDialog() {
        val dialog = DeleteNoteDialog.newInstance()
        dialog.show(supportFragmentManager, DIALOG_TAG_DELETE_NOTE)
    }

    companion object {
        private const val DIALOG_TAG_DELETE_NOTE = "dialogTagDeleteNote"

        const val INTENT_KEY_NOTE = "intentKeyNote"

        fun newIntent(context: Context, note: Note): Intent {
            return Intent(context, NoteDetailsActivity::class.java).apply {
                putExtra(INTENT_KEY_NOTE, note)
            }
        }

        fun newIntent(context: Context): Intent {
            return Intent(context, NoteDetailsActivity::class.java)
        }
    }

}