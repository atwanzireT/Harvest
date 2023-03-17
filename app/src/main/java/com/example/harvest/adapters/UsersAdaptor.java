package com.example.harvest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.harvest.R;
import com.example.harvest.modals.User;

import java.util.ArrayList;

public class UsersAdaptor extends RecyclerView.Adapter<UsersAdaptor.UserHolder> {
    private ArrayList<User> user;
    private Context context;
    private OnUserClickListener onUserClickListener;

    public interface OnUserClickListener{
        void onUserClicked(int position);
    }

    public UsersAdaptor(ArrayList<User> user, Context context, OnUserClickListener onUserClickListener) {
        this.user = user;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userholder, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.txtUsername.setText(user.get(position).getUsername());
        Glide.with(context).load(user.get(position)
                        .getProfilePicture()).error(R.drawable.account_img)
                .placeholder(R.drawable.account_img).into(holder.profileImg);

    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    class UserHolder extends RecyclerView.ViewHolder{
        TextView txtUsername;
        ImageView profileImg;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserClickListener.onUserClicked(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUsername);
            profileImg = itemView.findViewById(R.id.img_profile);
        }
    }
}
