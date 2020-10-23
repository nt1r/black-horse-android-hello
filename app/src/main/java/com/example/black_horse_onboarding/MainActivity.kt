package com.example.black_horse_onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout
    private val BUTTON_COUNT: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayout = findViewById(R.id.linear_layout)

        for (index in 1..BUTTON_COUNT + 1) {
            val view: View = layoutInflater.inflate(R.layout.button_linear_layout, null)
            val button: MaterialButton = view as MaterialButton
            button.text = String.format(getString(R.string.linear_layout_button), index)
            linearLayout.addView(button)
        }
    }
}