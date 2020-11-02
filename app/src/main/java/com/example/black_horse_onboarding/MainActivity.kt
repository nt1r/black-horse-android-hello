package com.example.black_horse_onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout
    private val BUTTON_COUNT: Int = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayout = findViewById(R.id.linear_layout)

        for (index in 1..BUTTON_COUNT + 1) {
            val view: View = layoutInflater.inflate(R.layout.button_linear_layout, null)
            val button: MaterialButton = view as MaterialButton
            button.text = String.format(getString(R.string.linear_layout_button), index)
            linearLayout.addView(button)

            if (index == 1) {
                button.setOnClickListener {
                    val constraintActivityIntent = Intent(this, ConstraintActivity::class.java)
                    startActivity(constraintActivityIntent)
                }
            }

            if (index == 2) {
                button.setOnClickListener {
                    val loginActivityIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginActivityIntent)
                }
            }

            if (index == 3) {
                button.setOnClickListener {
                    val contactsHomeActivityIntent = Intent(this, ContactsHomeActivity::class.java)
                    startActivity(contactsHomeActivityIntent)
                }
            }

            if (index == 4) {
                button.setOnClickListener {
                    val fragmentActivityIntent = Intent(this, LanguageActivity::class.java)
                    startActivity(fragmentActivityIntent)
                }
            }

            if (index == 5) {
                button.setOnClickListener {
                    val recyclerViewHomeActivityIntent = Intent(this, RecyclerViewHomeActivity::class.java)
                    startActivity(recyclerViewHomeActivityIntent)
                }
            }

            if (index == 6) {
                button.setOnClickListener {
                    startActivity(Intent(this, CounterActivity::class.java))
                }
            }

            if (index == 7) {
                button.setOnClickListener {
                    startActivity(Intent(this, SPActivity::class.java))
                }
            }

            if (index == 8) {
                button.setOnClickListener {
                    startActivity(Intent(this, SubmitActivity::class.java))
                }
            }

            if (index == 9) {
                button.setOnClickListener {
                    startActivity(Intent(this, ViewActivity::class.java))
                }
            }

            if (index == 10) {
                button.setOnClickListener {
                    startActivity(Intent(this, NetworkActivity::class.java))
                }
            }

            if (index == 11) {
                button.setOnClickListener {
                    startActivity(Intent(this, ArchActivity::class.java))
                }
            }
        }
    }
}