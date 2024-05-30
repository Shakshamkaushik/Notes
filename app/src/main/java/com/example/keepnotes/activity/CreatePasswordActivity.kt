package com.example.keepnotes.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.PatternLockView.Dot
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.example.keepnotes.R


class CreatePasswordActivity : AppCompatActivity() {
    lateinit var mPatternLockView: PatternLockView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_password)


        mPatternLockView = findViewById<View>(R.id.pattern_lock_view) as PatternLockView
        mPatternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onStarted() {
            }

            override fun onProgress(progressPattern: List<Dot>) {
            }

            override fun onComplete(pattern: List<Dot>) {
                // Shared Preferences to save state
                val sharedPreferences = getSharedPreferences("PREFS", 0)
                val editor = sharedPreferences.edit()
                editor.putString(
                    "password",
                    PatternLockUtils.patternToString(mPatternLockView, pattern)
                )
                editor.apply()


                // Intent to navigate to home screen when password added is true
                val intent = Intent(
                    applicationContext,
                    SecretNotesActivity::class.java
                )
                startActivity(intent)
                finish()
            }

            override fun onCleared() {
            }
        })
    }
}