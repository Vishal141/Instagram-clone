package com.example.instaclone.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaclone.Models.Post;
import com.example.instaclone.R;
import com.example.instaclone.fragments.CommentsFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostItemAdapter extends RecyclerView.Adapter<PostItemAdapter.ViewHolder> {

    private Activity mContext;
    private List<Post> posts;

    public PostItemAdapter(Activity mContext, List<Post> posts) {
        this.mContext = mContext;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.username.setText(posts.get(position).getUsername());
        holder.image_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.frameLayout.getVisibility()==View.GONE){
                    holder.frameLayout.setVisibility(View.VISIBLE);
                    Fragment fragment = new CommentsFragment();
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.comment_container,fragment).commit();
                }else{
                    holder.frameLayout.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView username,description,likes,comments;
        private ImageView postImage,image_like,image_comment;
        private FrameLayout frameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            postImage = itemView.findViewById(R.id.post_image);
            image_like = itemView.findViewById(R.id.image_like);
            image_comment = itemView.findViewById(R.id.image_comment);
            frameLayout = itemView.findViewById(R.id.comment_container);
        }
    }
}
