package com.example.penguinpay.api

import com.example.penguinpay.data.ExchangeRates
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateService {
    @GET("latest.json")
    suspend fun getLatestExchangeRates(@Query("app_id") appId: String, @Query("symbols") symbols: String) : ExchangeRates

    object ExchangeRateNetwork {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder().addInterceptor(logger).build()
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val exchangeRates = retrofit.create(ExchangeRateService::class.java)
    }
}