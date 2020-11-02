package com.example.black_horse_onboarding

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.black_horse_onboarding.network.PersonWrapper
import com.example.black_horse_onboarding.network.TWCService
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkActivity : AppCompatActivity() {
    private lateinit var button: AppCompatButton
    private val TAG = "NETWORK TAG"
    private val hostUrl = "https://twc-android-bootcamp.github.io"
    private val fileUrl = "https://twc-android-bootcamp.github.io/fake-data/data/default.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_layout)

        initComponents()
    }

    private fun initComponents() {
        button = findViewById(R.id.retrofit_button)

        button.setOnClickListener {
            GlobalScope.launch {
                val twcService = buildTWCService()
                val call: Call<PersonWrapper> = twcService.getFakeData()
                call.enqueue(object : Callback<PersonWrapper> {
                    override fun onResponse(call: Call<PersonWrapper>, response: Response<PersonWrapper>) {
                        if (response.isSuccessful) {
                            val wrapper = response.body()
                            Log.d(TAG, wrapper!!.toString())
                            if (wrapper.data.isNotEmpty()) {
                                runOnUiThread {
                                    Toast.makeText(this@NetworkActivity, wrapper.data[0].name, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PersonWrapper>, t: Throwable) {
                        Log.d(TAG, t.stackTraceToString())
                        runOnUiThread {
                            Toast.makeText(this@NetworkActivity, "Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }
    }

    // 点击按钮后的网络请求需要一定耗时（3 second），场景：
    // 1 second之后跳转到新的activity，能否正常工作
    // onDestroy

    private fun buildTWCService(): TWCService {
        val retrofit = Retrofit.Builder()
            .baseUrl(hostUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(TWCService::class.java)
    }
}