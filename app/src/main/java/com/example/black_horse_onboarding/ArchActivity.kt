package com.example.black_horse_onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import com.example.black_horse_onboarding.view_model.ArchViewModel

class ArchActivity : AppCompatActivity() {
    private lateinit var increaseButton: AppCompatButton
    private lateinit var clearButton: AppCompatButton
    private lateinit var countTextView: TextView

    private val archViewModel: ArchViewModel by viewModels()
    private lateinit var counterObserver: Observer<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arch)

        initComponents()
        initObservers()
    }

    private fun initComponents() {
        increaseButton = findViewById(R.id.arch_increase_button)
        clearButton = findViewById(R.id.arch_clear_button)
        countTextView = findViewById(R.id.arch_counter_text_view)

        increaseButton.setOnClickListener {
            archViewModel.increase()
        }
        clearButton.setOnClickListener {
            archViewModel.onCleared()
        }
    }

    private fun initObservers() {
        counterObserver = Observer { newCount ->
            countTextView.text = generateButtonText(newCount)
        }
        archViewModel.counter.observe(this, counterObserver)
    }

    private fun generateButtonText(counter: Int): String {
        return counter.toString()
    }
}