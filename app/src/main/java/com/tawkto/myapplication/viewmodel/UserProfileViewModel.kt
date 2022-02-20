package com.tawkto.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawkto.myapplication.repository.network.Repo
import org.json.JSONObject

class UserProfileViewModel : ViewModel() {
    var userDate = MutableLiveData<JSONObject>()

    //send request to server for data of selected hospital
    fun initilise(context: Context, username : String) {
        Repo.getInstance(context).loadOneUserData(username,userDate,context)
    }

    //data send towards the activity from repository
    fun getdata() : MutableLiveData<JSONObject>{
        return userDate
    }
}