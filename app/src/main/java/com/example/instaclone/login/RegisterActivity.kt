package com.example.instaclone.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.instaclone.PostActivity
import com.example.instaclone.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    var name:EditText?=null;
    var email:EditText?=null;
    var username:EditText?=null;
    var password:EditText?=null;

    var pd:ProgressDialog?=null

    var mAuth:FirebaseAuth?=null
    var reference:DatabaseReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById(R.id.name);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);

        mAuth = FirebaseAuth.getInstance()

    }

    fun signUp(view: View){
        var name_str = name!!.text.toString()
        var email_str = email!!.text.toString()
        var username_str = username!!.text.toString()
        var password_str = password!!.text.toString()

        if(TextUtils.isEmpty(name_str) || TextUtils.isEmpty(email_str) || TextUtils.isEmpty(username_str) || TextUtils.isEmpty(password_str)){
            Toast.makeText(applicationContext,"All fields are mandatory.",Toast.LENGTH_LONG).show();
        }else{
            if(password_str.length<8){
                Toast.makeText(applicationContext,"password must have altleast 8 characters.",Toast.LENGTH_LONG).show()
            }else{
                pd = ProgressDialog(RegisterActivity@this)
                pd!!.setMessage("please wait..")
                pd!!.show()

                register(email_str,password_str,username_str,name_str)

            }
        }

    }

    private fun register(em:String,pass:String,uname:String,Name:String){
        mAuth!!.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(OnCompleteListener {
            if(it.isSuccessful){
                var uid:String = mAuth!!.currentUser.uid
                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid)

                var map:HashMap<String,String> = HashMap<String,String>()
                map.put("id",uid)
                map.put("username",uname)
                map.put("email",em)
                map.put("fullname",Name)
                map.put("imgUrl","https://firebasestorage.googleapis.com/v0/b/instagram-clone-48e9f.appspot.com/o/user_icon_2.png?alt=media&token=fbca314d-b359-4b14-b03b-706486b0c547")
                map.put("bio","")

                reference!!.setValue(map).addOnCompleteListener(OnCompleteListener {
                    if(it.isSuccessful){
                        pd!!.dismiss()
                        var intent:Intent = Intent(applicationContext,PostActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        pd!!.dismiss()
                        Toast.makeText(applicationContext,"you can't sign up using this username or password",Toast.LENGTH_LONG).show()
                    }
                })
            }
        })
    }

    fun backToLogin(view:View){
        var intent:Intent = Intent(applicationContext,LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


}