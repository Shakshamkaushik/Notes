package com.example.keepnotes.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.keepnotes.R
import com.example.keepnotes.databinding.ActivitySecretNotesBinding
import com.example.keepnotes.fragment.SecretNotesFragment

class SecretNotesActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecretNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecretNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        openFragment(SecretNotesFragment())

    }

    private fun openFragment(frag: Fragment) {
        val fg = supportFragmentManager
        val fgt = fg.beginTransaction()
        fgt.replace(R.id.secretFrameLayout, frag)
        fgt.commit()
    }
}