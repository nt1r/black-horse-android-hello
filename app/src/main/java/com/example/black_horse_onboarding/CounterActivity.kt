package com.example.black_horse_onboarding

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable

const val TIMER_UPDATE: Int = 1

class CounterActivity : AppCompatActivity() {
    private lateinit var counterButton: AppCompatButton
    private lateinit var counters: MutableList<Int>
    private final val maxSecond = 10
    private final val msgWhatTextChanged = 1
    private final val msgWhatEnabledChanged = 2
    private final val TAG: String = "Coroutines"
    private lateinit var timerThread: Thread

    private val timerHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == msgWhatTextChanged) {
                updateButtonText(msg.arg1)
            } else if (msg.what == msgWhatEnabledChanged) {
                setButtonEnabled(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_layout)

        initThread()
        initComponents()
    }

    private fun initThread() {
        counters = mutableListOf()
        for (index in 1..maxSecond) {
            counters.add(index)
        }

        timerThread = Thread {
            kotlin.run {
                counters.toObservable()
                    .subscribeBy(
                        onNext = {
                            Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
                            SystemClock.sleep(1000L)
                            val msg = Message()
                            msg.what = msgWhatTextChanged
                            msg.arg1 = it
                            timerHandler.sendMessage(msg)
                        },
                        onError = { it.printStackTrace() },
                        onComplete = {

                        }
                    )
            }
        }
    }

    private fun initComponents() {
        counterButton = findViewById(R.id.counter_button)
        counterButton.text = generateButtonText(0)

        counterButton.setOnClickListener {
            Log.d(TAG, "Main Thread: ${Thread.currentThread().name}")
            counterButton.isEnabled = false
            timerThread.start()
        }
    }

    private fun generateButtonText(counter: Int): String {
        return "${getString(R.string.counter_button_text)} $counter"
    }

    private fun updateButtonText(counter: Int) {
        counterButton.text = generateButtonText(counter)
    }

    private fun setButtonEnabled(enabled: Boolean) {
        counterButton.isEnabled = true
    }
}