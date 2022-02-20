package com.tawkto.myapplication.repository.network

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.tawkto.myapplication.repository.room.RoomDataBaseUse
import com.tawkto.myapplication.repository.room.oneuserDetail
import com.tawkto.myapplication.repository.room.userDetail
import com.tawkto.myapplication.view.userlist.MainActivity
import com.tawkto.myapplication.view.userprofile.UserProfile
import com.tawkto.myapplication.model.DataLoadStatus
import com.tawkto.myapplication.utills.Keys
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class Repo {
    private var mRequestQueue: RequestQueue? = null
    companion object{
        var dataLoadListner: DataLoadStatus? =  null

        var instance : Repo? =null
        lateinit var mContext: Context

        fun getInstance(context: Context) : Repo {
            mContext = context
            if(instance == null){
                instance = Repo()
            }
            if(mContext is MainActivity) {
                dataLoadListner = mContext as MainActivity
            }else{
                dataLoadListner = mContext as UserProfile
            }

            return instance as Repo
        }
    }


    fun loadData(userData : MutableLiveData<List<userDetail>>, context: Context,id : String) {
//        val userServer = WebServiceClient.client.create(BackEndApi::class.java)
//        userServer.getUserFromServer().enqueue(object : Callback<AllUserData> {
//            override fun onResponse(
//                call: Call<AllUserData>,
//                response: retrofit2.Response<AllUserData>
//            ) {
//                if (response.isSuccessful) {
//                    var a = response.body()
////                    var b = response.body()?.userName
////                    var c = response.message().toString()
//                } else {
//
//                }
//            }
//
//            override fun onFailure(call: Call<AllUserData>, t: Throwable) {
//
//            }
//
//
//        })
        var userName = ArrayList<userDetail>()
        mRequestQueue = Volley.newRequestQueue(context);
        val url = "https://api.github.com/users?since=$id"
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null ,object : Response.Listener<JSONArray> {
                override fun onResponse(response: JSONArray) {
                    for(i in 0..response.length()-1){
                        var user = response.get(i) as JSONObject
                        val username = user.getString(Keys.name)
                        val bitmap = user.getString(Keys.avatar_url)
                        val id = user.getString(Keys.id)
                        userName.add(userDetail(login = username, bitmap = bitmap, id = id, note = false))

                        //sava user informationinto room database
                        // for run this operation on background thread we are using coroutines
                        GlobalScope.launch {
                            RoomDataBaseUse.insert(context,userDetail(login =  username, bitmap = bitmap, id = id, note = false))
                        }
                    }
                    userData.value = userName
                    dataLoadListner?.onSucess()
                }
            },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        var a = error.toString()
                        dataLoadListner?.onFailed(error?.message.toString())
                    }
                }
            )
        mRequestQueue?.add(jsonObjectRequest);
    }


    fun loadOneUserData(userid : String,userData : MutableLiveData<JSONObject>, context: Context) {
        mRequestQueue = Volley.newRequestQueue(context);
        val url = "https://api.github.com/users/$userid"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null ,object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject) {
                userData.value = response

                //sava user informationinto room database
                // for run this operation on background thread we are using coroutines
                GlobalScope.launch {
                    RoomDataBaseUse.insert(context, oneuserDetail(
                        login = response.getString(Keys.login),
                        id = response.getString(Keys.id),
                        username = response.getString(Keys.name),
                        followers = response.getString(Keys.followers),
                        following = response.getString(Keys.following),
                        company = response.getString(Keys.company),
                        blog = response.getString(Keys.blog),
                        bitmap = response.getString(Keys.avatar_url),
                        note = ""
                    ))
                }
                dataLoadListner?.onSucess()
            }
        },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    var a = error.toString()
                    dataLoadListner?.onFailed(error?.message.toString())
                }
            }
        )
        mRequestQueue?.add(jsonObjectRequest);
    }
}
