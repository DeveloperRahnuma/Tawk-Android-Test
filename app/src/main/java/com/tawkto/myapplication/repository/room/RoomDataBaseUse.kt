package com.tawkto.myapplication.repository.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//-----------------------------------------------------------------------
//singelton class so database have only one instance through out the class
//-----------------------------------------------------------------------------
object RoomDataBaseUse {
    private var userDatabase : UserDatabase? = null

    fun roomInitCheck(context: Context){
        if(userDatabase == null){
            initRoom(context)
        }
    }

    //for initialization
    private fun initRoom(context: Context){
        userDatabase = Room.databaseBuilder(context.applicationContext,UserDatabase::class.java,"userDb").build()
    }

    //--------------------------------------------
    //for insert the data into room database table
    //--------------------------------------------

    //which table data get enter it depend on class name
    //here class name is userDetail so data get entered
    //on that table also
    fun insert(context: Context, userDetail: userDetail){
        roomInitCheck(context)
        userDatabase?.userDao()?.insertUser(userDetail)
    }

    fun insert(context: Context, oneuserDetail: oneuserDetail){
        roomInitCheck(context)
        userDatabase?.userDao()?.insertOneUser(oneuserDetail)
    }



    //--------------------------------------------
    //for update the data into room database table
    //--------------------------------------------

    fun update(context: Context, oneuserDetail: oneuserDetail){
        roomInitCheck(context)
        userDatabase?.userDao()?.UpdateUser(oneuserDetail)
    }

    fun update(context: Context, userDetail: userDetail?){
        roomInitCheck(context)
        userDetail?.let { userDatabase?.userDao()?.UpdateUser(it) }
    }




    //--------------------------------------------
    //for get the data from room database table
    //--------------------------------------------
    fun get(context: Context) : LiveData<List<userDetail>>?{
        roomInitCheck(context)
       return userDatabase?.userDao()?.getWholeUser()
    }

    fun get(context: Context, usercode : String) : LiveData<oneuserDetail>?{
        roomInitCheck(context)
        return userDatabase?.userDao()?.getOneUser(usercode)
    }

}