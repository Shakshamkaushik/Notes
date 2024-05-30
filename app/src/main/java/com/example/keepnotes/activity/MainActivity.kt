package com.example.keepnotes.activity

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivityMainBinding
import com.example.keepnotes.fragment.NotesFragment
import com.example.keepnotes.fragment.TodoFragment
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment(NotesFragment())
        val wmbPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true)
        val editor = wmbPreference.edit()
        Log.d("TAG", "ISFIRSTRun Outside $isFirstRun")
        if (isFirstRun) {
            editor.putBoolean("FIRSTRUN", false)
            editor.apply()
            Log.d("TAG", "ISFIRSTRun Inside $isFirstRun")
            GuideView.Builder(this)
                .setTitle("Regular Todo Task ")
                .setContentText("Write your task and schedule them")
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(binding.ivTodoNotes)
                .setGravity(Gravity.auto)
                .setPointerType(PointerType.arrow)
                .setContentTextSize(12) //optional
                .setContentTypeFace(Typeface.DEFAULT_BOLD)//optional
                .setTitleTypeFace(Typeface.MONOSPACE)//optional
                .setTitleTextSize(14) //optional
                .setGuideListener {
                    GuideView.Builder(this)
                        .setTitle("Secret Notes")
                        .setContentText("Open Your Secret Notes \n on Scroll Upside Side from\n from Top of the Screen")
                        .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                        .setTargetView(binding.imageView3)
                        .setGravity(Gravity.auto)
                        .setPointerType(PointerType.arrow)
                        .setContentTextSize(12) //optional
                        .setContentTypeFace(Typeface.DEFAULT_BOLD)//optional
                        .setTitleTypeFace(Typeface.MONOSPACE)//optional
                        .setTitleTextSize(14) //optional
                        .build()
                        .show()
                }
                .build()
                .show()
        }


        binding.ivTodoNotes.setOnClickListener {
            openFragment(TodoFragment())
            binding.ivTodoNotes.setImageResource(R.drawable.tick_yellow)
            binding.ivNotes.setImageResource(R.drawable.white_book)
        }
        binding.ivNotes.setOnClickListener {
            openFragment(NotesFragment())
            binding.ivNotes.setImageResource(R.drawable.yellow_book)
            binding.ivTodoNotes.setImageResource(R.drawable.tick_white)
        }

    }

    private fun openFragment(frag: Fragment) {
        val fg = supportFragmentManager
        val fgt = fg.beginTransaction()
        fgt.replace(R.id.frameLayout, frag)
        fgt.commit()
    }

}