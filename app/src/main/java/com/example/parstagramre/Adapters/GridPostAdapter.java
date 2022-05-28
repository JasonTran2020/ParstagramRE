package com.example.parstagramre.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagramre.Models.Post;
import com.example.parstagramre.R;
import com.parse.ParseFile;

import java.util.List;

public class GridPostAdapter extends RecyclerView.Adapter<GridPostAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public GridPostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public GridPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.grid_item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage=itemView.findViewById(R.id.ivPostGrid);
        }

        public void bind(Post post) {
            ParseFile image=post.getImage();
            if(image!=null)
            {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

        }

    }
}
