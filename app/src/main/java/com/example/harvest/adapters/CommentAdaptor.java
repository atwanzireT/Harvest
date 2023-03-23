package com.example.harvest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.harvest.R;
import com.example.harvest.modals.CommentModal;

import java.util.ArrayList;

public class CommentAdaptor extends RecyclerView.Adapter<CommentAdaptor.CommentHolder> {
    private Context context;
    private ArrayList<CommentModal> comments;

    public CommentAdaptor(Context context, ArrayList<CommentModal> comment) {
        this.context = context;
        this.comments = comment;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.commentholder, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.commentView.setText(comments.get(position).getComment());
        holder.authorView.setText(comments.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder{
        TextView commentView;
        TextView authorView;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentView = itemView.findViewById(R.id.comment_txt);
            authorView = itemView.findViewById(R.id.username_cmt);
        }
    }
}
