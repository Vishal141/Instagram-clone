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
import com.example.instaclone.Models.Global
import com.example.instaclone.Models.Post
import com.example.instaclone.Models.User
import com.example.instaclone.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.post_item.*

class HomeFragment : Fragment() {

    var recyclerView:RecyclerView?=null
    var adapter:PostItemAdapter?=null

    var postList:ArrayList<Post>?=null
    var followingList:ArrayList<String>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var root:View = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = root.findViewById(R.id.home_recyclerView)

        postList = ArrayList<Post>()
        followingList = ArrayList<String>()

        var layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = layoutManager
        adapter = PostItemAdapter(activity,postList)

        recyclerView!!.adapter = adapter

        getFollowings()

        return root
    }

    private fun getFollowings(){
        var databaseReference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("follow")
            .child(Global.currentUserId).child("following")
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                followingList!!.clear()
                for(dataSnapShot:DataSnapshot in snapshot.children){
                    followingList!!.add(dataSnapShot.key!!)
                }

                getPosts()
            }

        })
    }

    private fun getPosts(){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post");
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postList!!.clear()
                for(dataSnapshot:DataSnapshot in snapshot.children){
                    var post:Post? = dataSnapshot.getValue(Post::class.java)
                    if(followingList!!.contains(post!!.publisher))
                        postList!!.add(post)
                }
                adapter!!.notifyDataSetChanged()
            }

        })
    }

}