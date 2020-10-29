package com.example.black_horse_onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.black_horse_onboarding.model.LocalDataSource
import com.example.black_horse_onboarding.model.Person
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewActivity : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout
    private val persons: MutableList<Person> = mutableListOf()
    private val addViewMsgWhat = 1

    private val viewHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                addViewMsgWhat -> {
                    persons.toObservable()
                        .subscribeBy(
                            onNext = { person ->
                                val textView = TextView(this@ViewActivity)
                                val personStr = "Person ID: ${person.id}\n" +
                                        "name: ${person.name}\n" +
                                        "gender: ${convertGender2String(person.gender)}\n" +
                                        "age: ${person.age}\n\n"
                                textView.text = personStr
                                linearLayout.addView(textView)
                            },
                            onError = {},
                            onComplete = {}
                        )

                    /*persons.forEach { person ->
                        val textView = TextView(this@ViewActivity)
                        val personStr = "Person ID: ${person.id}\n" +
                                "name: ${person.name}\n" +
                                "gender: ${convertGender2String(person.gender)}\n" +
                                "age: ${person.age}\n\n"
                        textView.text = personStr
                        linearLayout.addView(textView)
                    }*/
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_layout)

        initComponent()
    }

    private fun initComponent() {
        linearLayout = findViewById(R.id.view_activity_linear_layout)

        LocalDataSource.setContext(applicationContext)

        GlobalScope.launch {
            LocalDataSource.getPersons().subscribeBy(
                onSuccess = {
                    it.toObservable()
                        .subscribeBy(
                            onNext = { person ->
                                persons.add(person)
                            },
                            onError = {

                            },
                            onComplete = {
                                viewHandler.sendEmptyMessage(addViewMsgWhat)
                            }
                        )
                }
            )
        }

        /*Thread {
            LocalDataSource.getPersons().subscribeBy(
                onSuccess = {
                    it.toObservable()
                        .subscribeBy(
                            onNext = { person ->
                                persons.add(person)
                            },
                            onError = {

                            },
                            onComplete = {
                                viewHandler.sendEmptyMessage(addViewMsgWhat)
                            }
                        )
                }
            )
        }.start()*/
    }

    private fun convertGender2String(gender: Int?): String {
        return when (gender) {
            Person.MALE -> "male"
            Person.FEMALE -> "female"
            else -> "unknown"
        }
    }
}