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
    private val THREAD_METHOD: Int = 5
    private val COROUTINES_METHOD: Int = 6
    private val REACTIVEX_METHOD: Int = 7
    private final val method: Int = REACTIVEX_METHOD

    private lateinit var counterButton: AppCompatButton
    private lateinit var counters: MutableList<Int>
    private var currentCount: Int = 0
    private final val maxSecond = 10
    private final val msgWhatTextChanged = 1
    private final val msgWhatEnabledChanged = 2
    private final val TAG: String = "Coroutines"
    private lateinit var timerThread: Thread
    private lateinit var timerJob: Job
    private lateinit var timerDisposable: Disposable

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

    override fun onDestroy() {
        try {
            releaseThread()
            releaseCoroutines()
            releaseReactiveX()
        } catch (exception: InterruptedException) {
            // handler exceptions here
        }
        super.onDestroy()
    }

    private fun releaseReactiveX() {
        if (!timerDisposable.isDisposed) {
            timerDisposable.dispose()
            Log.d(TAG, "timerDisposable disposed")
        }
    }

    private fun releaseCoroutines() {
        if (timerJob.isActive) {
            timerJob.cancel()
            Log.d(TAG, "timerJob cancel")
        }
    }

    private fun releaseThread() {
        if (timerThread.isAlive) {
            timerThread.interrupt()
            Log.d(TAG, "timerThread interrupt")
        }
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
                THREAD_METHOD -> {
                    threadMethod()
                }
                COROUTINES_METHOD -> {
                    coroutinesMethod()
                }
                REACTIVEX_METHOD -> {
                    reactivexMethod()
                }
            }
        }

        timerThread = Thread {}
        timerJob = GlobalScope.launch { }
        timerDisposable = counters.toFlowable().subscribeBy { }
    }

    private fun reactivexMethod() {
        timerDisposable = counters.toFlowable()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { counter ->
                    try {
                        runBlocking {
                            Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
                            delay(1000L)
//                        updateButtonText(counter)
                            updateButtonTextWithRunOnUiThread(counter)
                        }
                    } catch (exception: InterruptedException) { }
                },
                onError = { it.printStackTrace() },
                onComplete = {
//                    setButtonEnabled(true)
                    updateButtonEnabledWithRunOnUiThread()
                }
            )
    }

    private fun coroutinesMethod() {
        timerJob = GlobalScope.launch(Dispatchers.IO) {
            currentCount = 0
            repeat(maxSecond) {
                Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
                delay(1000L)
                currentCount++
                updateButtonTextWithRunOnUiThread(currentCount)
            }
            updateButtonEnabledWithRunOnUiThread()
        }
    }

    private fun threadMethod() {
        timerThread = Thread {
            currentCount = 0
            var isInterrupted = false
            repeat(maxSecond) {
                if (isInterrupted) {
                    return@repeat
                }

                try {
                    Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
                    Thread.sleep(1000L)
                    currentCount++
                    updateButtonTextWithRunOnUiThread(currentCount)
                } catch (exception: InterruptedException) {
                    // handler exceptions here
                    Log.d(TAG, "timerThread interrupt caught")
                    timerThread.interrupt()
                    isInterrupted = true
                }
            }
            updateButtonEnabledWithRunOnUiThread()
        }
        timerThread.start()
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
        timerDisposable = Observable.interval(1L, TimeUnit.SECONDS)
            .timeInterval()
            .takeWhile {
                it.value() < maxSecond
            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
//            .subscribe {
//                Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
////                Log.d(TAG, "it: ${it.value()}")
//                updateButtonTextWithRunOnUiThread((it.value() + 1).toInt())
//            }
            .subscribeBy(
                onNext = { counter ->
                    Log.d(TAG, "Loop Thread: ${Thread.currentThread().name}")
                    updateButtonTextWithRunOnUiThread((counter.value() + 1).toInt())
                },
                onError = { it.printStackTrace() },
                onComplete = {
                    updateButtonEnabledWithRunOnUiThread()
                }
            )
        // subscribeOn和observeOn的含义
        // 单独用Thread、Coroutines和Rx实现一遍
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