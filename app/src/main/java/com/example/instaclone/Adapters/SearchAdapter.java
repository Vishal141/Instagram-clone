package com.example.instaclone.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instaclone.Models.Global;
import com.example.instaclone.Models.User;
import com.example.instaclone.R;
import com.example.instaclone.fragments.ProfileFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static Context mContext;
    private List<User> userList;

    public SearchAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_user_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.username.setText(userList.get(position).getUsername());
        holder.name.setText(userList.get(position).getFullname());
        Glide.with(mContext).load(userList.get(position).getImgUrl()).into(holder.imageView);

        setButtonText(holder.followBtn,userList.get(position).getId());

        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow");
                if(holder.followBtn.getText().equals("Follow")){
                    reference.child(Global.currentUserId)
                            .child("following").child(userList.get(position).getId()).setValue(true);
                    reference.child(userList.get(position).getId())
                            .child("followers").child(Global.currentUserId).setValue(true);
                }else{
                    reference.child(Global.currentUserId)
                            .child("following").child(userList.get(position).getId()).removeValue();
                    reference.child(userList.get(position).getId())
                            .child("followers").child(Global.currentUserId).removeValue();
                }

                final Animation btnAnim = AnimationUtils.loadAnimation(mContext,R.anim.button_click_anim);
                holder.followBtn.startAnimation(btnAnim);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.profileUser = userList.get(position);
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setButtonText(final Button fButton, final String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow").child(Global.currentUserId).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userId).exists())
                    fButton.setText("Following");
                else
                    fButton.setText("Follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageView;
        private TextView username,name;
        private Button followBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.email);
            name = itemView.findViewById(R.id.fullname);
            followBtn = itemView.findViewById(R.id.follow_btn);

        }

    }
}
