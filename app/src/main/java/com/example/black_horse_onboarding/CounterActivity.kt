package com.example.black_horse_onboarding

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toFlowable
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

const val TIMER_UPDATE: Int = 1

@ExperimentalCoroutinesApi
class CounterActivity : AppCompatActivity() {
    private val SPACE_FLOW_OBSERVABLE_COROUTINES_DELAY: Int = 1
    private val SPACE_FLOW_FLOWABLE_COROUTINES_DELAY: Int = 2
    private val SPACE_FLOW_THREAD_SLEEP: Int = 3
    private val TIME_FLOW_INTERVAL: Int = 4
    private val method: Int = SPACE_FLOW_FLOWABLE_COROUTINES_DELAY

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

        initComponents()
    }

    private fun initComponents() {
        counterButton = findViewById(R.id.counter_button)
        counterButton.text = generateButtonText(0)

        counterButton.setOnClickListener {
            Log.d(TAG, "Main Thread: ${Thread.currentThread().name}")
            counterButton.isEnabled = false

            when (method) {
                SPACE_FLOW_OBSERVABLE_COROUTINES_DELAY -> {
                    spaceFlowObservableCoroutinesDelay()
                }
                SPACE_FLOW_FLOWABLE_COROUTINES_DELAY -> {
                    spaceFlowFlowableCoroutinesDelay()
                }
                SPACE_FLOW_THREAD_SLEEP -> {
                    spaceFlowThreadSleep()
                }
                TIME_FLOW_INTERVAL -> {
                    timeFlowInterval()
                }
            }
        }
    }

    private fun spaceFlowFlowableCoroutinesDelay() {
        GlobalScope.launch {
            counters.toFlowable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { counter ->
                        runBlocking {
                            Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
                            delay(1000L)
                            updateButtonTextWithRunOnUiThread(counter)
                        }
                    },
                    onError = { it.printStackTrace() },
                    onComplete = {
                        updateButtonEnabledWithRunOnUiThread()
                    }
                )
        }
    }

    private fun timeFlowInterval() {
        currentCount = 0
        subscription = Observable.interval(1000L, TimeUnit.MILLISECONDS)
            .timeInterval()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                updateButtonTextWithRunOnUiThread(++currentCount)
                if (currentCount >= 10) {
                    updateButtonEnabledWithRunOnUiThread()
                    subscription.dispose()
                }
            }
    }

    private fun spaceFlowThreadSleep() {
        timerThread = Thread {
            counters.toObservable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { counter ->
                        Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
                        SystemClock.sleep(1000L)
                        updateButtonTextWithRunOnUiThread(counter)
                    },
                    onError = { it.printStackTrace() },
                    onComplete = {
                        updateButtonEnabledWithRunOnUiThread()
                    }
                )
        }
        timerThread.start()
    }

    private fun spaceFlowObservableCoroutinesDelay() {
        GlobalScope.launch(Dispatchers.IO) {
            counters.toObservable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { count ->
                        runBlocking {
                            Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
                            delay(1000L)

                            updateButtonTextWithHandler(count)
                        }
                    },
                    onError = { it.printStackTrace() },
                    onComplete = {
                        updateButtonEnabledWithHandler()
                    }
                )
        }
    }

    private fun updateButtonEnabledWithHandler() {
        timerHandler.sendEmptyMessage(msgWhatEnabledChanged)
    }

    private fun updateButtonTextWithHandler(count: Int) {
        val msg = Message().apply {
            this.what = msgWhatTextChanged
            this.arg1 = count
        }
        timerHandler.sendMessage(msg)
    }

    private fun updateButtonTextWithRunOnUiThread(count: Int) {
        runOnUiThread {
            updateButtonText(count)
        }
    }

    private fun updateButtonEnabledWithRunOnUiThread() {
        runOnUiThread {
            setButtonEnabled(true)
        }
    }

    private fun generateButtonText(counter: Int): String {
        return "${getString(R.string.counter_button_text)} $counter"
    }

    private fun updateButtonText(counter: Int) {
        counterButton.text = generateButtonText(counter)
    }

    private fun setButtonEnabled(enabled: Boolean) {
        counterButton.isEnabled = enabled
    }
}