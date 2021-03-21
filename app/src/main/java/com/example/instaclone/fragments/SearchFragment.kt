package com.example.instaclone.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Adapters.SearchAdapter
import com.example.instaclone.Models.Global
import com.example.instaclone.Models.User
import com.example.instaclone.R
import com.google.firebase.database.*
import java.lang.Exception

class SearchFragment : Fragment() {
    var recyclerView:RecyclerView?=null
    var adapter:SearchAdapter?=null

    var searchBar:EditText?=null

    var userList:ArrayList<User>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root:View = inflater.inflate(R.layout.fragment_search, container, false)

        userList = ArrayList<User>()

        recyclerView = root.findViewById(R.id.search_recyclerView)
        searchBar = root.findViewById(R.id.search_bar)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        adapter = SearchAdapter(this.context,userList)
        recyclerView!!.adapter = adapter

        readUser()

        searchBar!!.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUser(s.toString())
            }

        })

        return root
    }

    fun searchUser(sequence:String) {
        var users:ArrayList<User> = ArrayList()
        for(usr:User in userList!!){
            if(usr.username.contains(sequence) || usr.email.contains(sequence) || usr.fullname.contains(sequence))
                users.add(usr)
        }

        adapter = SearchAdapter(activity,users)
        recyclerView!!.adapter = adapter
    }

    fun readUser(){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList!!.clear()
                for(dataSnapshot:DataSnapshot in snapshot.children){
                    try {
                        var user:User? = dataSnapshot.getValue(User::class.java)
                        if(!user!!.id.equals(Global.currentUserId)){
                            userList!!.add(user)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                adapter!!.notifyDataSetChanged()
            }

        })
    }

}

