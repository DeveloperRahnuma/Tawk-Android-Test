package com.tawkto.myapplication.view.userprofile

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tawkto.myapplication.databinding.UserProfileBinding
import com.tawkto.myapplication.repository.room.RoomDataBaseUse
import com.tawkto.myapplication.repository.room.oneuserDetail
import com.tawkto.myapplication.repository.room.userDetail
import com.tawkto.myapplication.model.DataLoadStatus
import com.tawkto.myapplication.utills.Internet
import com.tawkto.myapplication.viewmodel.UserProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserProfile : AppCompatActivity(), DataLoadStatus {
    //for view binding of this activity
    lateinit var binding: UserProfileBinding
    //view model instance
    lateinit var userProfileViewModel : UserProfileViewModel

    var username = ""

    //used when make new object for oneusedata class for update notes
    //so we need to get data into these variable
    var oneuserDetail_obj : oneuserDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding is enbled here so assing it to variable viewBinding
        binding = UserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getting data from intent
        val extras = intent.extras
        if (extras != null) {
            username = extras.getString("username").toString()
        }

        //make loading wheel visible so user can understand data is loading now
        binding.progressBar.visibility = View.VISIBLE

        //get instance of viewmodel of Activity_MainViewModel
        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        //using coroutine to make sure run all long running operation not into
        //main thread

        //Dispatchers.Unconfined will run this opration on its parent thread
        GlobalScope.launch(Dispatchers.Unconfined) {
            RoomDataBaseUse.get(this@UserProfile,username)?.observe(this@UserProfile, Observer { oneuserDetail ->
                if(oneuserDetail == null){
                    if(Internet.checkConnection(this@UserProfile)){
                        //if no data available for selected user then go and bring data back
                        //once get data saved into room database
                        userProfileViewModel.initilise(this@UserProfile,username)
                    }else{
                        Internet.showNoInternetDiologe(this@UserProfile)
                    }
                }else{
                    oneuserDetail_obj = oneuserDetail
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.userName.text = oneuserDetail.username
                    binding.displyName.text = oneuserDetail.username
                    binding.followerDisplay.text = oneuserDetail.followers
                    binding.followingDisplay.text = oneuserDetail.following
                    binding.displyCompany.text = oneuserDetail.company
                    binding.displyBlog.text = oneuserDetail.blog
                    binding.noteDisplay.setText(oneuserDetail.note)

                    //set the bitmap into image view
                    com.tawkto.myapplication.utills.Bitmap.bitmapSet(oneuserDetail.bitmap,binding.userProfile,false)
                }
            })
        }


        //for update note into user database
        binding.save.setOnClickListener {
            if(binding.note.text.isEmpty()){
                Toast.makeText(applicationContext,"please enter note", Toast.LENGTH_SHORT).show()
            }else{
                val userDetail = oneuserDetail_obj?.let { it1 ->
                    oneuserDetail_obj?.id?.let { it2 ->
                        oneuserDetail_obj?.bitmap?.let { it3 ->
                            userDetail(
                                id = it2,
                                bitmap = it3,
                                login = it1?.login,
                                note = true
                            )
                        }
                    }
                }

                //adding note into room database
                oneuserDetail_obj?.note = binding.noteDisplay.text.toString()

                //uses kotlin coroutine for run long time running operation not into
                //main thread
                GlobalScope.launch(Dispatchers.IO) {
                    oneuserDetail_obj?.let { it1 -> RoomDataBaseUse.update(this@UserProfile, it1) }
                    RoomDataBaseUse.update(this@UserProfile, userDetail)
                }
            }
        }
    }

    override fun onSucess() {
        //make loading wheel Invisible so user can understand data is loading now
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onFailed(str: String) {

    }
}