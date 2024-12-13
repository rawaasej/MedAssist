package com.example.rawaaproject.network

import com.example.rawaaproject.network.PatientService.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Create the logging interceptor to log request and response bodies
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log full request and response details
    }

    // Set up the OkHttpClient with the logging interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)  // Add the logging interceptor to OkHttpClient
        .build()

    // Create Retrofit instance using the custom OkHttpClient
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.20:8080/") // Replace with your base URL
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)  // Use the OkHttpClient with logging
        .build()

    // Create the ApiService from Retrofit
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
