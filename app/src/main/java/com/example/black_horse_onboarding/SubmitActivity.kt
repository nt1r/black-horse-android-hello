package com.example.black_horse_onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.black_horse_onboarding.model.LocalDataSource
import com.example.black_horse_onboarding.model.Person
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SubmitActivity : AppCompatActivity() {
    private lateinit var nameEditText: TextInputEditText
    private lateinit var genderEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var submitButton: AppCompatButton

    private val showToastMsgWhat = 1

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                showToastMsgWhat -> {
                    Toast.makeText(this@SubmitActivity, "Submit Success", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_layout)

        initComponents()
    }

    private fun initComponents() {
        nameEditText = findViewById(R.id.name_edit_text)
        genderEditText = findViewById(R.id.gender_edit_text)
        ageEditText = findViewById(R.id.age_edit_text)
        submitButton = findViewById(R.id.submit_button)

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val gender = genderEditText.text.toString()
            val age = ageEditText.text.toString()

            val isNameValid = isNameValid(name)
            val isGenderValid = isGenderValid(gender)
            val isAgeValid = isAgeValid(age)
            if (isNameValid && isGenderValid && isAgeValid) {
                val person = Person(generateRandomId(), name, gender.toInt(), age.toInt())
                submitPersonForm(person)
            } else {
                Toast.makeText(this, "Submit Failed!", Toast.LENGTH_LONG).show()
            }
        }

        LocalDataSource.setContext(applicationContext)
    }

    private fun generateRandomId(): Long {
        val date: Date = Date()
        return date.time
    }

    private fun submitPersonForm(person: Person) {
        GlobalScope.launch {
            LocalDataSource.createPerson(person)
            handler.sendEmptyMessage(showToastMsgWhat)
        }
    }

    private fun isAgeValid(age: String): Boolean {
        if (age.isEmpty()) {
            ageEditText.error = getString(R.string.empty_error)
            return false
        }
        val ageInt = age.toInt()
        if (ageInt <= 0 || ageInt > 99) {
            ageEditText.error = getString(R.string.age_invalid_error)
            return false
        }
        return true
    }

    private fun isGenderValid(gender: String): Boolean {
        if (gender.isEmpty()) {
            genderEditText.error = getString(R.string.empty_error)
            return false
        }
        val genderInt = gender.toInt()
        if (genderInt != 0 && genderInt != 1) {
            genderEditText.error = getString(R.string.gender_invalid_error)
            return false
        }
        return true
    }

    private fun isNameValid(name: String): Boolean {
        if (name.isEmpty()) {
            nameEditText.error = getString(R.string.empty_error)
            return false
        }
        if (name.length > 8) {
            nameEditText.error = getString(R.string.name_invalid_error)
            return false
        }
        return true
    }
}