package com.example.keepnotes.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.PatternLockView.Dot
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.example.keepnotes.R


class InputPasswordActivity : AppCompatActivity() {

    lateinit var mPatternLockView: PatternLockView
    var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_input_password)

        val sharedPreferences = getSharedPreferences("PREFS", 0)
        password = sharedPreferences.getString("password", "0")

        mPatternLockView = findViewById<View>(R.id.pattern_lock_view) as PatternLockView
        mPatternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onStarted() {
            }

            override fun onProgress(progressPattern: List<Dot>) {
            }

            override fun onComplete(pattern: List<Dot>) {
                // if drawn pattern is equal to created pattern you will navigate to home screen
                if (password.equals(PatternLockUtils.patternToString(mPatternLockView, pattern))) {
                    val intent = Intent(
                        applicationContext,
                        SecretNotesActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                } else {
                    // other wise you will get error wrong password
                    Toast.makeText(this@InputPasswordActivity, "Wrong Password", Toast.LENGTH_SHORT)
                        .show()
                    mPatternLockView.clearPattern()
                }
            }

            override fun onCleared() {
            }
        })
    }
}