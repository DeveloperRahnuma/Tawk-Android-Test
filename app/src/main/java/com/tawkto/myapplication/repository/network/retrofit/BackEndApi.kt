package com.tawkto.myapplication.repository.network.retrofit

import com.tawkto.myapplication.model.AllUserData
import retrofit2.Call
import retrofit2.http.*

interface BackEndApi {

    @GET("users")
    fun getUserFromServer(): Call<AllUserData>

}