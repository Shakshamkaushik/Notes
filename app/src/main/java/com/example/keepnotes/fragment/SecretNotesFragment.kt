package com.example.keepnotes.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.activity.MainActivity
import com.example.keepnotes.adapter.SecretNotesAdapter
import com.example.keepnotes.adapter.iSecretNotes
import com.example.keepnotes.databinding.FragmentSecretNotesragmentBinding
import com.example.keepnotes.model.SecretNotes
import com.example.keepnotes.viewmodel.NotesViewModel


class SecretNotesFragment : Fragment(), iSecretNotes {
    lateinit var binding: FragmentSecretNotesragmentBinding
    lateinit var viewModel: NotesViewModel
     var onStop:Boolean= false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecretNotesragmentBinding.inflate(layoutInflater, container, false)


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(NotesViewModel::class.java)

        binding.secretNotesFloatingActionButton.setOnClickListener {
            val fr: FragmentTransaction = requireFragmentManager().beginTransaction()
            fr.replace(R.id.secretFrameLayout, EditSecretNoteFragment())
            fr.commit()
        }


        binding.hiddenNotesRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SecretNotesAdapter(requireActivity(), this)
        binding.hiddenNotesRv.adapter = adapter


        viewModel.allSecretNotes.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter.updateList(it as ArrayList<SecretNotes>)
            }
        })
        return binding.root
    }

    override fun onItemDelClick(secretNotes: SecretNotes) {
        viewModel.deleteSecretNote(secretNotes)
    }

    override fun onItemEditClick(secretNotes: SecretNotes) {
        val bundle = Bundle()
        bundle.putString("title", secretNotes.title)
        bundle.putString("des", secretNotes.des)
        bundle.putInt("id", secretNotes.id)
        bundle.putBoolean("isFromSecretNotesFragment", true)

        val editSecretNoteFragment: EditSecretNoteFragment = EditSecretNoteFragment()
        editSecretNoteFragment.setArguments(bundle)

        fragmentManager?.beginTransaction()?.replace(R.id.secretFrameLayout, editSecretNoteFragment)
            ?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG","ONDESTROY VIEW")
    }

    override fun onStop() {
        super.onStop()
       onStop = true
    }

    override fun onResume() {
        super.onResume()
        if (onStop) {
            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
        }
    }
}