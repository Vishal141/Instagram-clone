package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.instaclone.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var runnable = object : Runnable {
            override fun run() {
                var intent = Intent(applicationContext,
                    LoginActivity::class.java)
                startActivity(intent);
            }

        }

        var handler = Handler();

        handler.postDelayed(runnable,3000);

    }
}