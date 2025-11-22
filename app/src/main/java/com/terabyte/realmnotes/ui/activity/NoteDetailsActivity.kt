package com.terabyte.realmnotes.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.application.MyApplication
import com.terabyte.realmnotes.databinding.ActivityNoteDetailsBinding
import com.terabyte.realmnotes.domain.model.Note
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowNoteDetails.collect { state ->
                    binding.toolbar.title = when (state) {
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
                    binding.toolbar.subtitle =
                        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                            .format(note.date)
                }
            }
        }

        configureOnBackPressed()
    }

    override fun onStart() {
        super.onStart()
        binding.toolbar.setNavigationOnClickListener {
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
            ) { }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.butt
    }

    private fun configureOnBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.saveNote {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    companion object {
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