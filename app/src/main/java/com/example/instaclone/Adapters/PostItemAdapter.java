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
import com.example.instaclone.fragments.ProfileFragment;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Post post = posts.get(position);
        Glide.with(mContext).load(post.getPostImage()).into(holder.postImage);
        if(post.getDescription().equals(""))
            holder.description.setVisibility(View.GONE);
        else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
        }
        setPublisherInfo(holder.profileImage,holder.username,post.getPublisher());
        isLike(holder.image_like,post.getPostId());
        getLikesCount(holder.likes,post.getPostId());

        holder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId());
                if(holder.image_like.getTag().equals("like")){
                    reference.child(Global.currentUserId).setValue(true);
                }else{
                    reference.child(Global.currentUserId).removeValue();
                }
            }
        });

        getCommentsCount(holder.comments,post.getPostId());

        holder.image_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CommentsFragment();
                if(holder.frameLayout.getVisibility()==View.GONE){
                    holder.frameLayout.setVisibility(View.VISIBLE);
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.comment_container,fragment).commit();
                }else{
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    holder.frameLayout.setVisibility(View.GONE);
                }
            }
        });

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.profileUserId = post.getPublisher();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });

        isSaved(holder.save_post,post.getPostId());

        holder.save_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost(holder.save_post,post.getPostId());
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
        private ImageView postImage,image_like,image_comment,save_post;
        private FrameLayout frameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
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

    private void setPublisherInfo(final CircleImageView profile_image, final TextView username, final String userId){
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

    private void getLikesCount(final TextView likes, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                likes.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLike(final ImageView imageView, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(Global.currentUserId).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                }else{
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCommentsCount(final TextView comments, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                comments.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isSaved(final ImageView imageView, final String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saved").child(Global.currentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postId).exists()){
                    imageView.setTag("saved");
                }else{
                    imageView.setTag("NotSaved");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void savePost(ImageView imageView,String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saved").child(Global.currentUserId);
        if(imageView.getTag().equals("saved")){
            reference.child(postId).removeValue();
        }else{
            reference.child(postId).setValue(true);
        }
    }

}
