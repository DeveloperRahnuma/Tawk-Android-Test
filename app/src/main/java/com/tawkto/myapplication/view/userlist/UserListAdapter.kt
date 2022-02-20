package com.tawkto.myapplication.view.userlist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tawkto.myapplication.R
import com.tawkto.myapplication.repository.room.userDetail
import com.tawkto.myapplication.view.userlist.UserListAdapter.*
import com.tawkto.myapplication.view.userprofile.UserProfile
import com.tawkto.myapplication.utills.Bitmap


class UserListAdapter(val context: Context, var userList : List<userDetail>) : RecyclerView.Adapter<UserListDesign>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListDesign {
        return UserListDesign(LayoutInflater.from(context).inflate(R.layout.userlist_item, parent, false))
    }

    override fun onBindViewHolder(holder: UserListDesign, @SuppressLint("RecyclerView") position: Int) {
        holder.username.text = userList.get(position).login

        //if there is note available for user
        //then show note icon in list
        if(userList.get(position).note){
            holder.noteicon.visibility = View.VISIBLE
        }

        //for make image inverted after each 4 image in list
        var inverted = false
        if(position % 4 ==0 ){
            inverted = true
        }

        //set the bitmap into image view
        Bitmap.bitmapSet(userList.get(position).bitmap,holder.userIcon,inverted)

        holder.main.setOnClickListener {
            val intent = Intent(context, UserProfile::class.java)
            intent.putExtra("username", userList.get(position).login)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserListDesign(view : View) : RecyclerView.ViewHolder(view){
        val username = view.findViewById<TextView>(R.id.username)
        val userIcon = view.findViewById<ImageView>(R.id.userProfile)
        val main = view.findViewById<ConstraintLayout>(R.id.main)
        val noteicon = view.findViewById<ImageView>(R.id.noteicondisplay)
    }

    fun adapter_newlist(newList : ArrayList<userDetail>){
        userList = newList
    }

}