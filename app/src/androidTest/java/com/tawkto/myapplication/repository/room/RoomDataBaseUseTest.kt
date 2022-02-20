package com.tawkto.myapplication.repository.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.tawkto.myapplication.model.UserActionDao
import junit.framework.TestCase
import org.junit.After
import org.junit.Test

//-------------------------------------------------------------
//room databse test class
//----------------------------------------------------------------

class RoomDataBaseUseTest : TestCase() {
    private lateinit var db : UserDatabase
    private lateinit var dao : UserActionDao

    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        dao = db.userDao()
    }

    @After
    public override fun tearDown() {
        db.close()
    }


    @Test
    fun testInsert() {
       val userDetail =  userDetail("login1", "1","someurl", false)
        dao.insertUser(userDetail)
        val userDetails = dao.getWholeUser()
        assert(userDetails.value?.contains(userDetail)!!)
    }

    @Test
    fun testTestInsert() {
        val userDetail =  oneuserDetail("login1", "1","someurl", "rahnuma",
            "21","21", "company","blog","abc")
        dao.insertOneUser(userDetail)
        val userDetails = dao.getOneUser("0")
        assert(userDetails.value == userDetail)
    }

    fun testUpdate() {
        val userDetail =  userDetail("login1", "1","someurl", false)
        dao.insertUser(userDetail)
        val userDetails = dao.getWholeUser()
        assert(userDetails.value?.contains(userDetail)!!)
    }


    fun testGet() {
        val userDetail =  userDetail("login1", "1","someurl", false)
        val userDetails = dao.getWholeUser()
        assert(userDetails.value!!.contains(userDetail))
    }

}