package com.example.black_horse_onboarding.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface TWCService {
    @GET("fake-data/data/default.json")
    fun getFakeData(): Call<PersonWrapper>

    @POST("test-api")
    fun postData(person: Person)
}