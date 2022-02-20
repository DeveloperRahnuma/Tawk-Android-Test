package com.tawkto.myapplication.utills

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tawkto.myapplication.R

//=========================================================
// This class is used for internet and network related
// work like check is internet connection availabe or not
//=========================================================

class Internet {
    companion object{
        //for check internet connection is availabe or not
        //if internet is available then return true
        //otherwise it return false
        fun checkConnection(context: Context): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connMgr != null) {
                val activeNetworkInfo = connMgr.activeNetworkInfo
                if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                        true
                    } else activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
                }
            }
            return false
        }


        //show diologe when you callled this function
        //for no internet connection you can use this function
        fun showNoInternetDiologe(activity : AppCompatActivity){
                val dialog = Dialog(activity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.nointernet_diologe)

                val clsBtn = dialog.findViewById(R.id.closeBtn) as Button
                clsBtn.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
        }
    }
}