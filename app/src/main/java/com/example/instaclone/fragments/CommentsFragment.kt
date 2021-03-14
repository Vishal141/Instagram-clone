package com.example.instaclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Adapters.CommentAdapter
import com.example.instaclone.Models.Comment
import com.example.instaclone.R

class CommentsFragment : Fragment() {

    var comment:EditText?=null
    var sendBtn:Button?=null
    var commentsView:RecyclerView?=null

    var adapter:CommentAdapter?=null
    var commentList:ArrayList<Comment>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_comments, container, false)

        comment = root.findViewById(R.id.commnet)
        sendBtn = root.findViewById(R.id.send_comment)
        commentsView = root.findViewById(R.id.comments_recyclerView);

        var c1 = Comment("v1","","","hello")
        var c2 = Comment("v2","","","nice pic")
        var c3 = Comment("v3","","","great")
        var c4 = Comment("v4","","","awesome!!!")

        commentList = ArrayList<Comment>()
        commentList!!.add(c1)
        commentList!!.add(c2)
        commentList!!.add(c3)
        commentList!!.add(c4)

        adapter = CommentAdapter(activity,commentList)
        commentsView!!.layoutManager = LinearLayoutManager(context)
        commentsView!!.adapter = adapter

        sendBtn!!.setOnClickListener { v->
            var anim:Animation = AnimationUtils.loadAnimation(context,R.anim.button_click_anim);
            sendBtn!!.startAnimation(anim)
        }

        return root
    }
}