package com.tawkto.myapplication.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey

//-------------------------------------------------------------------------
//room database table here you can make
//as much as table you want
//each table will be data class and dataclass varibale
//is take as column name
//------------------------------------------------------------------------


//=========================================================
// Table name is whole_user_detail
//=========================================================
@Entity(tableName = "whole_user_detail")
data class userDetail(
    @PrimaryKey
     val login : String,
     val id : String,
     val bitmap : String,
    val note: Boolean
)



//=========================================================
// Table name is one_user_detail
//=========================================================
@Entity(tableName = "one_user_detail")
data class oneuserDetail(
    @PrimaryKey
    val login : String,
    val id : String,
    val bitmap : String,
    val username : String,
    val followers : String,
    val following : String,
    val company : String,
    val blog : String,
    var note : String,
)