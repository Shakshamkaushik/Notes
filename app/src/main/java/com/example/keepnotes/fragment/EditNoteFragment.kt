package com.example.keepnotes.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.keepnotes.R
import com.example.keepnotes.databinding.FragmentEditNoteBinding
import com.example.keepnotes.mode.Notes
import com.example.keepnotes.viewmodel.NotesViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class EditNoteFragment : Fragment() {

    lateinit var viewModel: NotesViewModel
    lateinit var binding: FragmentEditNoteBinding

    private lateinit var tv: TextView
    var isFromNotesFragment: Boolean = false
    var title: String? = null
    var des: String? = null
    var notesId: Int? = null
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditNoteBinding.inflate(layoutInflater, container, false)

        tv = binding.textView4

        val dateString = "2021-05-12T12:12:12.121Z";
        val odt = OffsetDateTime.parse(dateString);
        val dtf = DateTimeFormatter.ofPattern("MMM dd  | hh:mm a", Locale.ENGLISH);
        val date = dtf.format(odt)
        tv.text = date


        val bundle = this.arguments

        if (bundle != null) {
            title = bundle.getString("title")
            des = bundle.getString("des")
            notesId = bundle.getInt("id")
            isFromNotesFragment = bundle.getBoolean("isFromNotesFragment")
        }
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val fr: FragmentTransaction = fragmentManager!!.beginTransaction()
                    fr.replace(R.id.frameLayout, NotesFragment())
                    fr.commit()
                }
            })
        if (isFromNotesFragment) {
            binding.floatingActionButton2.visibility = View.GONE
            binding.floatingActionButton3.visibility = View.VISIBLE
        } else {
            binding.floatingActionButton2.visibility = View.VISIBLE
            binding.floatingActionButton3.visibility = View.GONE
        }
        Log.d("Data1", title.toString())
        Log.d("Data1", des.toString())
        Log.d("Data1", notesId.toString())
        Log.d("Data1", isFromNotesFragment.toString())

        if (isFromNotesFragment) {


            binding.etTitle.setText(title)
            if (des == "No Text") {
                binding.etDescription.setText("")
            } else {
                binding.etDescription.setText(des)
            }
            binding.floatingActionButton3.setOnClickListener {

                val et1 = binding.etTitle.text.toString()
                val et2 = binding.etDescription.text.toString()

                val dateString = "2021-05-12T12:12:12.121Z";
                val odt = OffsetDateTime.parse(dateString);
                val dtf = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH);
                val date = dtf.format(odt)

                viewModel.updateNote(
                    Notes(
                        binding.etTitle.text.toString(),
                        binding.etDescription.text.toString(), date, notesId!!
                    )
                )
                val fr = requireFragmentManager().beginTransaction()
                fr.replace(R.id.frameLayout, NotesFragment())
                fr.commit()
            }

        }
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(NotesViewModel::class.java)


        binding.floatingActionButton2.setOnClickListener {
            val et1 = binding.etTitle
            val et2 = binding.etDescription

            val dateString = "2021-05-12T12:12:12.121Z";
            val odt = OffsetDateTime.parse(dateString);
            val dtf = DateTimeFormatter.ofPattern("MMM dd ", Locale.ENGLISH);
            val date = dtf.format(odt)


            if (et1.text.toString().isNotEmpty() && et2.text.isEmpty()) {
                et2.setText("No Text")
                viewModel.addNote(Notes(et1.text.toString(), et2.text.toString(), date))
            } else if (et2.text.toString().isNotEmpty() && et1.text.isEmpty() ) {
                et1.setText("No Text")
                viewModel.addNote(Notes(et2.text.toString(), et1.text.toString(), date))
            } else if (et1.text.isNotEmpty() && et2.text.isNotEmpty()){
                viewModel.addNote(Notes(et1.text.toString(), et2.text.toString(), date))
            }

            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.frameLayout, NotesFragment())
            fr.commit()
        }
        return binding.root
    }

}