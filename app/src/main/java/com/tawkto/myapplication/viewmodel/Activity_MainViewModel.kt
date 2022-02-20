package com.tawkto.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawkto.myapplication.repository.network.Repo
import com.tawkto.myapplication.repository.room.userDetail

class Activity_MainViewModel : ViewModel() {
    var userDate = MutableLiveData<List<userDetail>>()

    //send request to server for data of selected hospital
    fun initilise(context: Context,id : String) {
        Repo.getInstance(context).loadData(userDate,context,id)
    }

    //data send towards the activity from repository
    fun getdata() : MutableLiveData<List<userDetail>> {
        return userDate
    }

}