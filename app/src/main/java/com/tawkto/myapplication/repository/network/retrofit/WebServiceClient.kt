package com.tawkto.myapplication.repository.network.retrofit


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WebServiceClient {
    private lateinit var okHttpClient: OkHttpClient
    private var retrofit: Retrofit? = null
    val client: Retrofit
        get() {
            okHttpClient = OkHttpClient.Builder().build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit!!
        }

}