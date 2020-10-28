package com.example.black_horse_onboarding

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText

class SPActivity : AppCompatActivity() {
    private lateinit var button: AppCompatButton
    private lateinit var launchButton: AppCompatButton
    private lateinit var editText: TextInputEditText
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val saveFileName: String = "sp_practice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_p_layout)

        sharedPreference = getSharedPreferences(saveFileName, MODE_PRIVATE)
        editor = sharedPreference.edit()

        initComponents()
        loadDataFromSP()
    }

    private fun loadDataFromSP() {
        val savedStr = sharedPreference.getString("content", "")
        editText.setText(savedStr)
    }

    private fun initComponents() {
        button = findViewById(R.id.sp_button)
        editText = findViewById(R.id.sp_edit_text)
        launchButton = findViewById(R.id.sp_launch_button)

        button.setOnClickListener {
            editor.putString("content", editText.text.toString())
            editor.commit()
        }

        launchButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}