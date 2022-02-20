package com.tawkto.myapplication.view.userlist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.ferfalk.simplesearchview.SimpleSearchView
import com.tawkto.myapplication.R
import com.tawkto.myapplication.databinding.ActivityMainBinding
import com.tawkto.myapplication.model.DataLoadStatus
import com.tawkto.myapplication.repository.room.RoomDataBaseUse
import com.tawkto.myapplication.repository.room.userDetail
import com.tawkto.myapplication.utills.Internet
import com.tawkto.myapplication.viewmodel.Activity_MainViewModel


class MainActivity : AppCompatActivity(), DataLoadStatus {
    //for view binding of this activity
    lateinit var viewBinding: ActivityMainBinding

    //view model instance
    lateinit var activityMainviewmodel : Activity_MainViewModel

    //for shimmer effect
    lateinit var shimmerLoadingView : ShimmerFrameLayout

    //arraylist that contain only those data which match with search text
    var searcharraylist  = ArrayList<userDetail>()

    //global data list so it can be accesable everywhere in this class
    lateinit var userlist : List<userDetail>

    //recycle view adapter
    lateinit var adapter : UserListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding is enbled here so assing it to variable viewBinding
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //set toolbar in place of actionbar
        viewBinding.toolbar.title = "Rahnuma Test";

        //set toolbar in place of action bar and make
        //option menu work
        setSupportActionBar(viewBinding.toolbar)

        //initialize shimmer view
        shimmerLoadingView = findViewById(R.id.shimmerLoadingView);
        shimmerLoadingView.startShimmer()

        //get instance of viewmodel of Activity_MainViewModel
        activityMainviewmodel = ViewModelProvider(this@MainActivity).get(Activity_MainViewModel::class.java)


        //fetch the data from database if its availabe then no need to go through server
        RoomDataBaseUse.get(this)?.observe(this, Observer { userdetailslist ->
            if (userdetailslist.size > 0 ){
                userlist = userdetailslist

                //set adapter when get the data
                setAdapter()
            }else{
                //beore call the netword check is there internet available or not
                if(Internet.checkConnection(this)){
                    activityMainviewmodel.initilise(this,"0")
                }else{
                    Internet.showNoInternetDiologe(this)
                }
            }
        })

        //set listner in search view
        viewBinding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if(!newText.isEmpty()){
                    search(newText)
                }
                return true
            }

            override fun onQueryTextCleared(): Boolean {
                searcharraylist.clear()
                viewBinding.searchView.closeSearch(true)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if(!query.isEmpty()){
                    search(query)
                }
                return true
            }

        })
    }

    //when data load sucessfully in repo class get call back here
    override fun onSucess() {
        if(viewBinding.bottomprograssbar.visibility == View.VISIBLE){
            viewBinding.bottomprograssbar.visibility = View.INVISIBLE
        }
        //no need to get data from repository becouse data saved into room database
        //and get observe from there
        //if you want to observe from repositoy need to call get data function of viewmodel
    }

    override fun onFailed(str: String) {

    }

    fun setAdapter(){
        try {
            //stop the shimmer first
            shimmerLoadingView.stopShimmer()

            //stop the shimmer and make invisible
            shimmerLoadingView.visibility = View.GONE

            //make recycle view  visible
            viewBinding.userlistRecycleview.visibility = View.VISIBLE

            //instance of UserListAdapter created
            adapter = UserListAdapter(this, userList = userlist)

            //adding a layoutmanager we can use grid layout manage too
            val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            viewBinding.userlistRecycleview.layoutManager = layoutManager

            //adding adapter into recycle view
            viewBinding.userlistRecycleview.adapter = adapter

            //scroll listner for recycle view for support pageination
            viewBinding.userlistRecycleview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //for check is the last item of lisit visible if yes then call the server again for more data
                if(layoutManager.findLastCompletelyVisibleItemPosition() == userlist.size-1){
                    if(Internet.checkConnection(this@MainActivity)){
                        viewBinding.bottomprograssbar.visibility = View.VISIBLE
                        activityMainviewmodel.initilise(this@MainActivity,userlist.get(userlist.size-1).id)
                    }else{
                        Toast.makeText(this@MainActivity, "Internet not found",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    //store user data which match with search string
    fun search(searchText : String){
        searcharraylist.clear()
        //check user list is Initialized or not
        if(this::userlist.isInitialized) {
            for (i in 0..userlist.size - 1) {
                var id = userlist.get(i)?.id
                var login = userlist.get(i)?.login

                if ((id!!.startsWith(searchText, true)) || (login.startsWith(searchText, false))) {
                    searcharraylist.add(userlist.get(i))
                }
            }
            if (this::adapter.isInitialized && adapter != null) {
                adapter.adapter_newlist(searcharraylist)
                adapter.notifyDataSetChanged()
            }
        }
    }


    override fun onBackPressed() {
        if (viewBinding.searchView.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_search -> {
                viewBinding.searchView.showSearch(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}