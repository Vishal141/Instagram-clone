package com.example.instaclone

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.instaclone.Adapters.SearchAdapter
import com.example.instaclone.Models.Global
import com.example.instaclone.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class PostActivity : AppCompatActivity() {

    var bottonNavigationView:BottomNavigationView?=null;
    var selectedFragment:Fragment?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()

        bottonNavigationView= findViewById(R.id.bottom_navigation)
        bottonNavigationView!!.setOnNavigationItemSelectedListener {
            //println("clicked")
            when(it.itemId){
                R.id.nav_home-> {
                    selectedFragment = HomeFragment()
                    println("home")
                }
                R.id.nav_search-> {
                    selectedFragment = SearchFragment()
                    println("search")
                }
                R.id.nav_add-> selectedFragment = AddFragment()
                R.id.nav_notification-> selectedFragment = NotificationFragment()
                R.id.nav_profile-> {
                    Global.profileUserId = Global.currentUserId
                    selectedFragment = ProfileFragment()
                }
            }

            if (selectedFragment==null)
                selectedFragment = HomeFragment()

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment!!).commit()

            return@setOnNavigationItemSelectedListener true
        }

    }

}