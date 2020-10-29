package com.example.black_horse_onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

const val TIMER_UPDATE: Int = 1

class CounterActivity : AppCompatActivity() {
    private lateinit var counterButton: AppCompatButton
    private lateinit var counters: MutableList<Int>
    private var currentCount: Int = 0
    private final val maxSecond = 10
    private final val msgWhatTextChanged = 1
    private final val msgWhatEnabledChanged = 2
    private final val TAG: String = "Coroutines"
    private lateinit var timerThread: Thread
    private lateinit var subscription: Disposable

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

        counters = mutableListOf()
        for (index in 1..maxSecond) {
            counters.add(index)
        }

        timerThread = initThread()
        initComponents()
    }

    private fun initThread(): Thread {
        return Thread {
            /*counters.toObservable()
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
                        timerHandler.sendEmptyMessage(msgWhatEnabledChanged)
                    }
                )*/

            currentCount = 0
            subscription = Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .timeInterval()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    timerHandler.sendMessage(Message().apply {
                        this.what = msgWhatTextChanged
                        this.arg1 = ++currentCount
                    })
                    if (currentCount >= 10) {
                        timerHandler.sendMessage(Message().apply {
                            this.what = msgWhatEnabledChanged
                        })
                        subscription.dispose()
                    }
                }

        }
    }

    private fun initComponents() {
        counterButton = findViewById(R.id.counter_button)
        counterButton.text = generateButtonText(0)

        counterButton.setOnClickListener {
            Log.d(TAG, "Main Thread: ${Thread.currentThread().name}")
            counterButton.isEnabled = false
            timerThread = initThread()
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