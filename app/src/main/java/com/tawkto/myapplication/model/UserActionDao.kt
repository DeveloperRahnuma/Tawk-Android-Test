package com.tawkto.myapplication.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tawkto.myapplication.repository.room.oneuserDetail
import com.tawkto.myapplication.repository.room.userDetail

@Dao
interface UserActionDao {
    @Insert
    fun insertUser(userDetail: userDetail)

    @Update
    fun UpdateUser(oneuserDetail: oneuserDetail)

    @Update
    fun UpdateUser(userDetail: userDetail)

    @Query("SELECT * FROM whole_user_detail")
    fun getWholeUser() : LiveData<List<userDetail>>

    @Insert
    fun insertOneUser(oneuserDetail: oneuserDetail)

    @Query("SELECT * FROM one_user_detail WHERE login IN (:usercode) ")
    fun getOneUser(usercode : String) : LiveData<oneuserDetail>
}