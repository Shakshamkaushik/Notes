package com.example.keepnotes.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.keepnotes.R
import com.example.keepnotes.databinding.FragmentEditSecretNoteBinding
import com.example.keepnotes.model.SecretNotes
import com.example.keepnotes.viewmodel.NotesViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EditSecretNoteFragment : Fragment() {

    lateinit var binding: FragmentEditSecretNoteBinding
    lateinit var viewModel: NotesViewModel
    var isFromSecretNotesFragment: Boolean = false
    var secretTitle: String? = null
    var secretDes: String? = null
    var secretNotesId: Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditSecretNoteBinding.inflate(layoutInflater, container, false)


        val bundle = this.arguments

        if (bundle != null) {
            secretTitle = bundle.getString("title")
            secretDes = bundle.getString("des")
            secretNotesId = bundle.getInt("id")
            isFromSecretNotesFragment = bundle.getBoolean("isFromSecretNotesFragment")
        }


        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val fr: FragmentTransaction = fragmentManager!!.beginTransaction()
                    fr.replace(R.id.secretFrameLayout, SecretNotesFragment())
                    fr.commit()
                }
            })

        if (isFromSecretNotesFragment) {
            binding.floatingActionButtonSecret.visibility = View.GONE
            binding.floatingActionButton3Secret.visibility = View.VISIBLE
        } else {
            binding.floatingActionButton3Secret.visibility = View.GONE
            binding.floatingActionButtonSecret.visibility = View.VISIBLE
        }


        if (isFromSecretNotesFragment) {
            binding.etSecretTitle.setText(secretTitle)
            if (secretDes == "No Text") {
                binding.etSecretDescription.setText("")
            }else {
                binding.etSecretDescription.setText(secretDes)
            }
            binding.floatingActionButton3Secret.setOnClickListener {

                val et1 = binding.etSecretTitle.text.toString()
                val et2 = binding.etSecretDescription.text.toString()

                val dateString = "2021-05-12T12:12:12.121Z";
                val odt = OffsetDateTime.parse(dateString);
                val dtf = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH);
                val date = dtf.format(odt)

                viewModel.updateSecretNote(
                    SecretNotes(
                        et1, et2, date, secretNotesId!!
                    )
                )
                val fr = requireFragmentManager().beginTransaction()
                fr.replace(R.id.secretFrameLayout, SecretNotesFragment())
                fr.commit()
            }
        }



        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(NotesViewModel::class.java)
        binding.floatingActionButtonSecret.setOnClickListener {
            val et1 = binding.etSecretTitle
            val et2 = binding.etSecretDescription

            val dateString = "2021-05-12T12:12:12.121Z";
            val odt = OffsetDateTime.parse(dateString);
            val dtf = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH);
            val date = dtf.format(odt)

            if (et1.text.toString().isNotEmpty() && et2.text.isEmpty()) {
                et2.setText("No Text")
                viewModel.addSecretNote(SecretNotes(et1.text.toString(), et2.text.toString(), date))
            } else if (et2.text.toString().isNotEmpty()  && et1.text.isEmpty()) {
                et1.setText("No Text")
                viewModel.addSecretNote(SecretNotes(et2.text.toString(), et1.text.toString(), date))
            } else if (et1.text.isNotEmpty() && et2.text.isNotEmpty()){
                viewModel.addSecretNote(SecretNotes(et1.text.toString(), et2.text.toString(), date))
            }

            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.secretFrameLayout, SecretNotesFragment())
            fr.commit()
        }
        return binding.root
    }


}