package com.example.keepnotes.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.activity.CreatePasswordActivity
import com.example.keepnotes.activity.InputPasswordActivity
import com.example.keepnotes.adapter.NotesAdapter
import com.example.keepnotes.adapter.iNotes
import com.example.keepnotes.databinding.FragmentNotesBinding
import com.example.keepnotes.mode.Notes
import com.example.keepnotes.viewmodel.NotesViewModel
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType
import kotlin.math.abs


class NotesFragment : Fragment(), iNotes, GestureDetector.OnGestureListener {

    private lateinit var binding: FragmentNotesBinding
    lateinit var viewModel: NotesViewModel
    private val TAG = "Gestures"
    lateinit var gestureDetector: GestureDetector
    var x1: Float = 0.0f
    var x2: Float = 0.0f
    var y1: Float = 0.0f
    var y2: Float = 0.0f

    companion object {
        const val MIN_DISTANCE = 150
    }

    @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        binding.floatingActionButton.setOnClickListener {
            val fr: FragmentTransaction = requireFragmentManager().beginTransaction()
            fr.replace(R.id.frameLayout, EditNoteFragment())
            fr.commit()
        }
        val wmbPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isFirstRunFragment = wmbPreference.getBoolean("isFirstRunFragment", true)
        val editor = wmbPreference.edit()


           if(isFirstRunFragment) {
               editor.putBoolean("isFirstRunFragment", false)
               editor.apply()
               GuideView.Builder(requireContext())
                   .setTitle("Add Notes")
                   .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                   .setTargetView(binding.floatingActionButton)
                   .setGravity(Gravity.auto)
                   .setPointerType(PointerType.arrow)
                   .setContentTextSize(12) //optional
                   .setContentTypeFace(Typeface.DEFAULT_BOLD)//optional
                   .setTitleTypeFace(Typeface.MONOSPACE)//optional
                   .setTitleTextSize(14) //optional
                   .build()
                   .show()
           }



        gestureDetector = GestureDetector(requireContext(), this)
        binding.topConstraint.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event!!)
            when (event.action) {
                0 -> {
                    x1 = event.x
                    y1 = event.y
                }

                1 -> {
                    x2 = event.x
                    y2 = event.y

                    val valueX: Float = x2 - x1
                    val valueY: Float = y2 - y1

                    if (abs(valueY) > MIN_DISTANCE) {
                        if (y2 > y1) {

                            openActivity()
                        } else {
                            Toast.makeText(requireContext(),"SCroll", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
            true
        }


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(NotesViewModel::class.java)

        binding.allNotesRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = NotesAdapter(requireActivity(), this)
        binding.allNotesRv.adapter = adapter


        viewModel.allNotes.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateList(it as ArrayList<Notes>)
            }
        }

        return binding.root
    }

    private fun openActivity() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("PREFS", 0)
        val password = sharedPreferences.getString("password", "0")

        if (password == "0") {
            // Intent to navigate to Create Password Screen
            val intent = Intent(requireActivity(), CreatePasswordActivity::class.java)
            startActivity(intent)

        } else {
            // Intent to navigate to Input Password Screen
            val intent = Intent(requireActivity(), InputPasswordActivity::class.java)
            startActivity(intent)

        }
    }


    override fun onItemDelClick(todo: Notes) {
            viewModel.delNote(todo)
    }

    override fun onItemEditClick(todo: Notes) {
        val bundle = Bundle()
        bundle.putString("title", todo.title)
        bundle.putString("des", todo.des)
        bundle.putInt("id", todo.id)
        bundle.putBoolean("isFromNotesFragment", true)

        val fragment2 = EditNoteFragment()
        fragment2.setArguments(bundle)

        fragmentManager?.beginTransaction()?.replace(R.id.frameLayout, fragment2)?.commit()

    }

    override fun onDown(p0: MotionEvent): Boolean {
        return false
    }

     override fun onShowPress(p0: MotionEvent) {
         null
     }


     override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

     override fun onLongPress(p0: MotionEvent) {

     }


     override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }


}