package com.example.black_horse_onboarding.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface TWCService {
    @GET("fake-data/data/default.json")
    fun getFakeData(): Call<JsonObject>
}