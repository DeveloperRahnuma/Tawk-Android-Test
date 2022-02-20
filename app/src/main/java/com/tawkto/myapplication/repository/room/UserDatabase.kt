package com.tawkto.myapplication.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tawkto.myapplication.model.UserActionDao

//-------------------------------------------------------------
//room databse as its requirment
//----------------------------------------------------------------


//version number need to increased for new version of apps or
//when u changed something under database
@Database(entities = [userDetail::class, oneuserDetail::class], version = 3)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao() : UserActionDao
}