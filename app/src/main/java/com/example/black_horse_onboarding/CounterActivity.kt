package com.example.black_horse_onboarding

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import kotlinx.coroutines.*

const val TIMER_UPDATE: Int = 1

class CounterActivity : AppCompatActivity() {
    private lateinit var counterButton: AppCompatButton
    private var counter: Int = 0
    private final val maxSecond = 10

    private val timerHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            updateButtonText()
            if (counter == 10) {
                counterButton.isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_layout)

        counterButton = findViewById(R.id.counter_button)
        counterButton.text = generateButtonText()

        // 抽象出一个方法
        counterButton.setOnClickListener {
            counter = 0
            counterButton.isEnabled = false

            GlobalScope.launch(Dispatchers.Main) {
                repeat(maxSecond) {
                    delay(1000L)
                    counter++
                    timerHandler.sendEmptyMessage(TIMER_UPDATE)
                }
            }
        }
    }

    private fun generateButtonText(): String {
        return "${getString(R.string.counter_button_text)} $counter"
    }

    private fun updateButtonText() {
        counterButton.text = generateButtonText()
    }
}