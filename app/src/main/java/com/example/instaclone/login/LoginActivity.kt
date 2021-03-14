package com.example.instaclone.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.instaclone.PostActivity
import com.example.instaclone.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun signIn(view:View){
        var intent = Intent(applicationContext,PostActivity::class.java);
        startActivity(intent);
    }
}