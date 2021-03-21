package com.example.instaclone.login

import android.app.ProgressDialog
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.instaclone.MainActivity
import com.example.instaclone.Models.Global
import com.example.instaclone.Models.User
import com.example.instaclone.PostActivity
import com.example.instaclone.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    var email:EditText?=null
    var password:EditText?=null

    var mAuth:FirebaseAuth?=null
    var reference:DatabaseReference?=null

    var pd:ProgressDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        mAuth = FirebaseAuth.getInstance()

    }

    fun signIn(view:View){
        var email_str:String = email!!.text.toString()
        var pass_str:String = password!!.text.toString()

        if(TextUtils.isEmpty(email_str) || TextUtils.isEmpty(email_str)){
            Toast.makeText(applicationContext,"all fields are mandatory",Toast.LENGTH_LONG).show()
        }else{
            pd = ProgressDialog(LoginActivity@this)
            pd!!.setMessage("please wait..")
            pd!!.show()

            login(email_str,pass_str)

        }

    }

    private fun login(em:String,pass:String){
        mAuth!!.signInWithEmailAndPassword(em,pass).addOnCompleteListener(OnCompleteListener {
            if(it.isSuccessful) {
                Global.currentUserId = mAuth!!.currentUser.uid
                pd!!.dismiss()
                var intent = Intent(applicationContext,PostActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

            }else{
                pd!!.dismiss()
                Toast.makeText(applicationContext,"Invalid credentials",Toast.LENGTH_LONG).show()
            }
        })
    }

    fun signUp(view:View){
        var intent:Intent = Intent(applicationContext,RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}