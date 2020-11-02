package com.example.black_horse_onboarding.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ArchViewModel : ViewModel() {
    // Create a LiveData with a Int
    val counter: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    private var isIncreasing = false
    private var counterJob: Job = GlobalScope.launch { }
    private var counterDisposable: Disposable = Disposable.empty()
    private final val MYTAG = "LiveData"

    fun increase() {
        if (isIncreasing) {
            return
        }

        isIncreasing = true
        counterJob = GlobalScope.launch(Dispatchers.IO) {
            counterDisposable = Observable.interval(1L, TimeUnit.SECONDS)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        counter.value = counter.value?.plus(1)
                        Log.d(MYTAG, counter.value.toString())
                    },
                    onError = {
                        it.printStackTrace()
                    },
                    onComplete = { }
                )
        }
    }

    public override fun onCleared() {
        super.onCleared()

        if (counterJob.isActive) {
            Log.d(MYTAG, "counter job canceled.")
            counterJob.cancel()
        }

        if (!counterDisposable.isDisposed) {
            Log.d(MYTAG, "counter disposable disposed.")
            counterDisposable.dispose()
        }

        counter.value = 0
        isIncreasing = false
    }
}