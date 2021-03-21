package com.example.instaclone.fragments

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.Adapters.MyPostAdapter
import com.example.instaclone.Models.Global
import com.example.instaclone.Models.Post
import com.example.instaclone.Models.User
import com.example.instaclone.R
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class ProfileFragment : Fragment() {

    var image_profile:CircleImageView?=null
    var posts:TextView?=null
    var followers:TextView?=null
    var following:TextView?=null
    var username:TextView?=null
    var name:TextView?=null
    var bio:TextView?=null
    var addBioBtn: Button?=null
    var post_view:ImageView?=null
    var save_post_view:ImageView?=null

    var recyclerview:RecyclerView?=null
    var adapter:MyPostAdapter?=null

    var postList:ArrayList<Post>?=null
    var savedPostList:ArrayList<Post>?=null
    var savedPostIds:ArrayList<String>?=null

    var isSavedClicked:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root:View = inflater.inflate(R.layout.fragment_profile, container, false)

        image_profile = root.findViewById(R.id.image_profile)
        posts = root.findViewById(R.id.posts)
        followers = root.findViewById(R.id.followers)
        following = root.findViewById(R.id.following)
        username = root.findViewById(R.id.username)
        name = root.findViewById(R.id.name)
        bio = root.findViewById(R.id.bio)
        addBioBtn = root.findViewById(R.id.add_bio_btn)
        post_view = root.findViewById(R.id.post_view)
        save_post_view = root.findViewById(R.id.save_post_view)

        recyclerview = root.findViewById(R.id.profile_post_recyclerView);

        postList = ArrayList<Post>()
        savedPostIds = ArrayList()
        savedPostList = ArrayList<Post>()

        isSavedClicked = false

        setUserInfo(image_profile!!,username!!,name!!,bio!!)
        if(!Global.profileUserId.equals(Global.currentUserId)){
            isFollowing(addBioBtn!!)
            save_post_view!!.visibility = View.GONE
        }
        else{
            addBioBtn!!.setText("Add Bio")
            save_post_view!!.visibility = View.VISIBLE
        }

        setPostCount(posts!!)
        setFollowersCount(followers!!)
        setFollowingsCount(following!!)

        addBioBtn!!.setOnClickListener(View.OnClickListener {
            if(Global.currentUserId.equals(Global.profileUserId)){

            }else{
                follow(addBioBtn!!)
            }
        })

        adapter = MyPostAdapter(activity,postList)

        var layoutManager:LinearLayoutManager = GridLayoutManager(activity,3)
        recyclerview!!.setHasFixedSize(true)
        recyclerview!!.layoutManager = layoutManager
        recyclerview!!.adapter = adapter

        getPosts()
        getSavedPostIds()

        post_view!!.setOnClickListener(View.OnClickListener {
            adapter = MyPostAdapter(activity,postList)
            recyclerview!!.adapter = adapter
        })

        save_post_view!!.setOnClickListener(View.OnClickListener {
            adapter = MyPostAdapter(activity,savedPostList)
            recyclerview!!.adapter = adapter
        })

        return root
    }

    private fun setUserInfo(imageView:CircleImageView,uname:TextView,Name:TextView,Bio:TextView){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Global.profileUserId);
        reference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var user: User? = snapshot.getValue(User::class.java)
                Glide.with(context!!).load(user!!.imgUrl).into(imageView)
                uname.setText(user!!.username)
                Name.setText(user!!.fullname)
                Bio.setText(user!!.bio)
            }

        })
    }

    private fun setPostCount(postCount:TextView){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Posts")
        reference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var count:Long = 0
               for(dataSnapshot:DataSnapshot in snapshot.children){
                   var post:Post? = dataSnapshot.getValue(Post::class.java)
                   if(post!!.publisher.equals(Global.profileUserId))
                       count++
               }
                postCount.setText(count.toString())
            }

        })
    }

    private fun setFollowersCount(followersCount:TextView){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference()
            .child("follow").child(Global.profileUserId).child("followers")
        reference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var count = snapshot.childrenCount
                followersCount.setText(count.toString())
            }

        })
    }

    private fun setFollowingsCount(followingsCount:TextView){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference()
            .child("follow").child(Global.profileUserId).child("following")
        reference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var count = snapshot.childrenCount
                followingsCount.setText(count.toString())
            }

        })
    }

    private fun isFollowing(btn:Button){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("follow")
            .child(Global.currentUserId).child("following")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.child(Global.profileUserId).exists())
                   btn.text = "Following"
                else
                   btn.text = "Follow"
            }
        })

    }

    private fun follow(btn:Button){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("follow")
        if(btn.text.equals("Follow")){
            reference.child(Global.currentUserId).child("following").child(Global.profileUserId).setValue(true)
            reference.child(Global.profileUserId).child("followers").child(Global.currentUserId).setValue(true)
        }else{
            reference.child(Global.currentUserId).child("following").child(Global.profileUserId).removeValue()
            reference.child(Global.profileUserId).child("followers").child(Global.currentUserId).removeValue()
        }
    }

    private fun getPosts(){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Posts")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postList!!.clear()
                for(dataSnapshot:DataSnapshot in snapshot.children){
                    var post:Post? = dataSnapshot.getValue(Post::class.java)
                    if(post!!.publisher.equals(Global.profileUserId))
                        postList!!.add(post)
                }
                println(postList!!.size)
                Collections.reverse(postList)
                adapter!!.notifyDataSetChanged()
            }

        })
    }

    private fun getSavedPost(){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Posts")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                savedPostList!!.clear()
                for(dataSnapshot:DataSnapshot in snapshot.children){
                    var post:Post? = dataSnapshot.getValue(Post::class.java)
                    if(savedPostIds!!.contains(post!!.postId))
                        savedPostList!!.add(post)
                }
            }

        })
    }

    private fun getSavedPostIds(){
        var reference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Saved").child(Global.currentUserId)
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                savedPostIds!!.clear()
                for(dataSnapshot:DataSnapshot in snapshot.children){
                    savedPostIds!!.add(dataSnapshot.key!!)
                }
                getSavedPost()
            }

        })
    }


}