package com.example.parstagramre.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagramre.Models.Post;
import com.example.parstagramre.PostDetailsActivity;
import com.example.parstagramre.R;
import com.example.parstagramre.UserPostActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        Post post=posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPostUser;
        private ImageView ivImage;
        private ImageView ivUserImage;
        private TextView tvPostDesc;
        private TextView tvCreatedAt;
        ImageButton ibtnComment;
        ImageButton ibtnLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostUser= itemView.findViewById(R.id.tvPostUser);
            ivImage=itemView.findViewById(R.id.ivImage);
            ivUserImage=itemView.findViewById(R.id.ivUserProfile);
            tvPostDesc=itemView.findViewById(R.id.tvPostDesc);
            tvCreatedAt=itemView.findViewById(R.id.tvCreatedAt);
            ibtnComment=itemView.findViewById(R.id.ibtnComment);
            ibtnLike=itemView.findViewById(R.id.ibtnLike);
        }

        public void bind(Post post) {
            tvPostDesc.setText(post.getDescription());
            tvPostUser.setText(post.getUser().getUsername());
            tvCreatedAt.setText(getANiceDateFromParseIPurposelyMadeThisMethodNameSuperLong(post.getCreatedAt().toString()));
            ParseFile image=post.getImage();
            if(image!=null)
            {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            image=post.getUser().getParseFile("profileImage");
            Glide.with(context).load(image.getUrl()).into(ivUserImage);

            tvPostUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, UserPostActivity.class);
                    i.putExtra("post", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

            ivUserImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, UserPostActivity.class);
                    i.putExtra("post", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, PostDetailsActivity.class);
                    i.putExtra("post",Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

            ibtnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likePost(post);
                }
            });

            ibtnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, PostDetailsActivity.class);
                    i.putExtra("post",Parcels.wrap(post));
                    context.startActivity(i);
                }
            });
            try {
                if (jsonHasString(post.getJSONArray("likes"),ParseUser.getCurrentUser().getObjectId())!=-1)
                {
                    ImageViewCompat.setImageTintList(ibtnLike, ColorStateList.valueOf(ContextCompat.getColor(context,R.color.insta_Red)));
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void likePost(Post post)
        {
            boolean liked=false;
            JSONArray likeArray=post.getJSONArray("likes");
            try {
                int position=jsonHasString(likeArray, ParseUser.getCurrentUser().getObjectId());
                if(position!=-1)
                {
                    likeArray.remove(position);
                    liked=false;
                }
                else
                {
                    likeArray.put(ParseUser.getCurrentUser().getObjectId());
                    liked=true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            post.put("likes",likeArray);
            boolean finalLiked = liked;
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (finalLiked)
                    {
                        ImageViewCompat.setImageTintList(ibtnLike, ColorStateList.valueOf(ContextCompat.getColor(context,R.color.insta_Red)));
                    }
                    else
                    {
                        ImageViewCompat.setImageTintList(ibtnLike, ColorStateList.valueOf(ContextCompat.getColor(context,R.color.black)));
                    }
                }
            });
        }

        public int jsonHasString(JSONArray json, String id) throws JSONException {
            for(int i = 0; i < json.length(); i++) {  // iterate through the JsonArray
                // first I get the 'i' JsonElement as a JsonObject, then I get the key as a string and I compare it with the value
                if(json.getString(i).equals(id))
                    return i;
            }
            return  -1;
        }


        //What do you think this does
        public String getANiceDateFromParseIPurposelyMadeThisMethodNameSuperLong(String parseDate)
        {
            List<String> list= Arrays.asList(parseDate.split(" "));
            return  list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(5);
        }
    }
}