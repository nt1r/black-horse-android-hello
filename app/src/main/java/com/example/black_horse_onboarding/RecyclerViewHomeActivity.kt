package com.example.black_horse_onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class RecyclerViewHomeActivity : AppCompatActivity() {
    private lateinit var button: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_home_layout)

        button = findViewById(R.id.recycler_view_home_button)
        button.setOnClickListener {
            val recyclerViewDetailActivityIntent = Intent(this, ListActivity::class.java)
            startActivity(recyclerViewDetailActivityIntent)
        }
    }
}