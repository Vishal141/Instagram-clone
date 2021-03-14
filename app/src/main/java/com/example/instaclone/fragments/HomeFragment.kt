package com.example.instaclone.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Adapters.PostItemAdapter
import com.example.instaclone.Models.Post
import com.example.instaclone.R
import kotlinx.android.synthetic.main.post_item.*

class HomeFragment : Fragment() {

    var recyclerView:RecyclerView?=null
    var adapter:PostItemAdapter?=null

    var postList:ArrayList<Post>?=null

    var comment_image:ImageView?=null
    var frameLayout:FrameLayout?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var root:View = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = root.findViewById(R.id.home_recyclerView)

        postList = ArrayList<Post>()

        var p1 = Post()
        p1.username = "vishal"
        var p2 = Post()
        p2.username = "shreyansh"
        var p3 = Post()
        p3.username = "Rahul"
        var p4 = Post()
        p4.username = "Naman"
        var p5 = Post()
        p5.username = "Chetan"

        postList!!.add(p1)
        postList!!.add(p2)
        postList!!.add(p3)
        postList!!.add(p4)
        postList!!.add(p5)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter = PostItemAdapter(activity,postList)

        recyclerView!!.adapter = adapter

        return root
}
}