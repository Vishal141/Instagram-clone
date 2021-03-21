package com.example.instaclone.Adapters;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.example.instaclone.Models.Global;
import com.example.instaclone.Models.Post;
import com.example.instaclone.Models.User;
import com.example.instaclone.R;
import com.example.instaclone.fragments.CommentsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        Post post = posts.get(position);
        Glide.with(mContext).load(post.getPostImage()).into(holder.postImage);
        if(post.getDescription().equals(""))
            holder.description.setVisibility(View.GONE);
        else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
        }
        setPublisherInfo(holder.profileImage,holder.username,post.getPublisher());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView username,description,likes,comments;
        private ImageView postImage,image_like,image_comment,save_post;
        private FrameLayout frameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.email);
            description = itemView.findViewById(R.id.description);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            postImage = itemView.findViewById(R.id.post_image);
            save_post = itemView.findViewById(R.id.save_post);
            image_like = itemView.findViewById(R.id.image_like);
            image_comment = itemView.findViewById(R.id.image_comment);
            frameLayout = itemView.findViewById(R.id.comment_container);
        }
    }

    public void setPublisherInfo(final CircleImageView profile_image, final TextView username, final String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImgUrl()).into(profile_image);
                if(userId.equals(Global.currentUserId))
                    username.setText("You");
                else
                    username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
