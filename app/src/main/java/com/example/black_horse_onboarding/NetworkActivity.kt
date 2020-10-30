package com.example.black_horse_onboarding

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
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
                val call: Call<JsonObject> = twcService.getFakeData()
                call.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            Log.d(TAG, response.body()!!.toString())
                            runOnUiThread {
                                Toast.makeText(this@NetworkActivity, "Success", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.d(TAG, t.stackTraceToString())
                        runOnUiThread {
                            Toast.makeText(this@NetworkActivity, "Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }
    }

    private fun buildTWCService(): TWCService {
        val retrofit = Retrofit.Builder()
            .baseUrl(hostUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(TWCService::class.java)
    }
}