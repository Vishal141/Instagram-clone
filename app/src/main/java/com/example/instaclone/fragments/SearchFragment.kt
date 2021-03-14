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
import com.example.instaclone.Models.User
import com.example.instaclone.R

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
        var u1 = User("1","vishal1","v1@gmal.com","vishal","","")
        var u2 = User("2","vishal2","v1@gmal.com","vishal ahirwar","","")
        var u3 = User("3","vishal3","v1@gmal.com","ashok ahirwar","","")
        var u4 = User("4","vishal4","v1@gmal.com","Naman","","")
        var u5 = User("5","vishal5","v1@gmal.com","shreyansh","","")
        userList!!.add(u1)
        userList!!.add(u2)
        userList!!.add(u3)
        userList!!.add(u4)
        userList!!.add(u5)

        recyclerView = root.findViewById(R.id.search_recyclerView)
        searchBar = root.findViewById(R.id.search_bar)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        adapter = SearchAdapter(this.context,userList)
        recyclerView!!.adapter = adapter

        searchBar!!.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var users = searchUser(s.toString())
                adapter = SearchAdapter(context,users)
                recyclerView!!.adapter = adapter
            }

        })

        return root
    }

    fun searchUser(sequence:String):ArrayList<User> {
        var users = ArrayList<User>()
        for (u:User in userList!!){
            if(u.fullName.contains(sequence) || u.username.contains(sequence))
                users.add(u)
        }
        return users
    }

}

