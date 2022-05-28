package com.example.parstagramre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parstagramre.Adapters.CommentsAdapter;
import com.example.parstagramre.Models.Comment;
import com.example.parstagramre.Models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {
    private static final String TAG ="PostDetailsActivity" ;
    ImageView ImageView3;
    TextView tvTimeStamp;
    TextView tvDescription;
    TextView tvLikes;
    ImageButton ibtnComment;
    ImageButton ibtnLike;
    EditText etComment;
    RecyclerView rvComments;
    CommentsAdapter adapter;
    List<Comment> comments;

    Post post;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ImageView3=findViewById(R.id.imageView3);
        tvTimeStamp=findViewById(R.id.tvTimeStamp);
        tvDescription=findViewById(R.id.tvDescription);
        tvLikes=findViewById(R.id.tvLikeCount);
        ibtnComment=findViewById(R.id.ibtnComment);
        ibtnLike=findViewById(R.id.ibtnLike);
        etComment=findViewById(R.id.etComment);
        rvComments=findViewById(R.id.rvComments);

        post= Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvTimeStamp.setText(calculateTimeAgo(post.getCreatedAt()));
        tvDescription.setText(post.getString(Post.KEY_DESCRIPTION));
        tvLikes.setText(post.getJSONArray("likes").length()+" Likes");
        Glide.with(this).load(post.getImage().getUrl()).into(ImageView3);

        comments=new ArrayList<>();
        adapter=new CommentsAdapter(this, comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(adapter);

        ibtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likePost();
            }
        });

        ibtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createComment();
            }
        });

        try {
            if (jsonHasString(post.getJSONArray("likes"),ParseUser.getCurrentUser().getObjectId())!=-1)
            {
                ImageViewCompat.setImageTintList(ibtnLike, ColorStateList.valueOf(ContextCompat.getColor(this,R.color.insta_Red)));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        queryComments(post);

    }

    public void queryComments(Post post)
    {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER);
        query.setLimit(20);
        query.whereEqualTo(Comment.KEY_POST,post);
        query.orderByDescending(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e!=null)
                {
                    Log.e(TAG,"Error getting comments");
                    return;
                }
                addAll(objects);
            }
        });
    }

    public void createComment()
    {
        Comment comment= new Comment();
        if(etComment.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Comment can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        comment.setDescription(etComment.getText().toString());
        comment.setUser(ParseUser.getCurrentUser());
        comment.setPost(post);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null)
                {
                    Log.e(TAG,"Error publishing comment");
                    return;
                }
                comments.add(0,comment);
                etComment.setText("");
                adapter.notifyDataSetChanged();
                rvComments.scrollToPosition(0);
            }
        });
    }

    public void likePost()
    {
        boolean liked=false;
        JSONArray likeArray=post.getJSONArray("likes");
        try {
            int position=jsonHasString(likeArray,ParseUser.getCurrentUser().getObjectId());
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
                    ImageViewCompat.setImageTintList(ibtnLike, ColorStateList.valueOf(ContextCompat.getColor(PostDetailsActivity.this,R.color.insta_Red)));
                }
                else
                {
                    ImageViewCompat.setImageTintList(ibtnLike, ColorStateList.valueOf(ContextCompat.getColor(PostDetailsActivity.this,R.color.black)));
                }
                tvLikes.setText(post.getJSONArray("likes").length()+" Likes");

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


    public void addAll(List<Comment> commentList)
    {
        comments.addAll(commentList);
        adapter.notifyDataSetChanged();
    }

    protected void clear()
    {
        comments.clear();
        adapter.notifyDataSetChanged();
    }




    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}