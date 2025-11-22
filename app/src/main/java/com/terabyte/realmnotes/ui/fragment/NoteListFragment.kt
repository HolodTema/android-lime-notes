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
import com.terabyte.realmnotes.R
import com.terabyte.realmnotes.databinding.FragmentNoteListBinding
import com.terabyte.realmnotes.ui.activity.NoteDetailsActivity
import com.terabyte.realmnotes.ui.recycler.NoteAdapter
import com.terabyte.realmnotes.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class NoteListFragment: Fragment() {
    private lateinit var binding: FragmentNoteListBinding

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)

        adapter = NoteAdapter(layoutInflater) { note ->
            startActivity(NoteDetailsActivity.newIntent(requireActivity(), note))
        }

        binding.recyclerNotes.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowNoteList.collect {
                    adapter.submitList(it)
                    binding.textAmountNotes.text = getString(R.string.amount_notes, it.size)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.buttonAddNote.setOnClickListener {
            startActivity(NoteDetailsActivity.newIntent(requireActivity()))
        }
    }

    companion object {

        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }

    }
}