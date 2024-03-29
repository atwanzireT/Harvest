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
import com.example.harvest.modals.IssueModal;

import java.io.File;
import java.util.ArrayList;

public class IssueAdaptor extends RecyclerView.Adapter<IssueAdaptor.MyViewHolder> {
    Context context;
    ArrayList<IssueModal> list;

    private static OnIssueClickListener onIssueClickListener;
    public interface OnIssueClickListener {
        void onIssueClicked(int position);
    }

    public IssueAdaptor(Context context, ArrayList<IssueModal> list, OnIssueClickListener onIssueClickListener) {
        this.context = context;
        this.list = list;
        this.onIssueClickListener = onIssueClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.text_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        IssueModal issueModal = list.get(position);
        holder.title.setText(issueModal.getTitle());
        holder.detail.setText(issueModal.getDetail());
        holder.author.setText(issueModal.getAuthor());

        Glide.with(context).load(list.get(position)
                        .getImageUri()).error(R.drawable.media)
                .placeholder(null).into(holder.image);
//
//        Picasso.get().load(new File(issueModal.getImageUri())).into(holder.imageview);
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, detail, author;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIssueClickListener.onIssueClicked(getAdapterPosition());
                }
            });

            title = itemView.findViewById(R.id.textCardTitle);
            detail = itemView.findViewById(R.id.textCardBody);
            author = itemView.findViewById(R.id.textcard_username);
            image = itemView.findViewById(R.id.imageView);

        }
    }
}
