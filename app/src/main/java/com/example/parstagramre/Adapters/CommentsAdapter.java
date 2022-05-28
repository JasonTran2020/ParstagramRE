package com.example.parstagramre.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagramre.Models.Comment;
import com.example.parstagramre.Models.Post;
import com.example.parstagramre.R;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments =comments;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Comment comment=comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivUserImage;
        TextView tvUserName;
        TextView tvComment;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivUserImage=itemView.findViewById(R.id.ivUserImage);
            tvUserName=itemView.findViewById(R.id.tvUsername);
            tvComment=itemView.findViewById(R.id.tvComment);

        }

        public void bind(Comment comment)
        {
            tvUserName.setText(comment.getUser().getUsername());
            tvComment.setText(comment.getDescription());
            ParseFile image=comment.getUser().getParseFile("profileImage");
            Glide.with(context).load(image.getUrl()).into(ivUserImage);
        }
    }
}
