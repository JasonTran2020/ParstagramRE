package com.example.parstagramre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagramre.Adapters.GridPostAdapter;
import com.example.parstagramre.Models.Post;
import com.example.parstagramre.Utility.EndlessRecyclerViewScrollListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class UserPostActivity extends AppCompatActivity {

    public static final String TAG="UserPostActivity";
    TextView tvProfileName;
    ImageView ivProfileImage;
    private RecyclerView rvPosts;
    protected ParseUser user;
    protected GridPostAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    protected GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        Post post= Parcels.unwrap(getIntent().getParcelableExtra("post"));
        user=post.getUser();
        tvProfileName=findViewById(R.id.tvProfileName);
        ivProfileImage=findViewById(R.id.ivProfileImage);
        rvPosts=findViewById(R.id.rvPosts);
        swipeRefreshLayout=findViewById(R.id.swipeContainer);
        allPosts=new ArrayList<>();
        adapter=new GridPostAdapter(this,allPosts);
        layoutManager=new GridLayoutManager(this,3);
        endlessRecyclerViewScrollListener=new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMore();
            }
        };

        tvProfileName.setText(post.getUser().getUsername());

        ParseFile image=post.getUser().getParseFile("profileImage");
        Glide.with(this).load(image.getUrl()).into(ivProfileImage);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.addOnScrollListener(endlessRecyclerViewScrollListener);
        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        //Want specific user
        query.whereEqualTo(Post.KEY_USER,user);

        query.setLimit(20);
        query.orderByDescending(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null)
                {
                    Log.i(TAG,"Issue with getting post");
                    return;
                }
                for(Post post:posts)
                {
                    Log.i(TAG, "Post: "+post.getDescription());
                }
                addAll(posts);
            }
        });
    }

    protected  void loadMore()
    {
        Post lastPost=allPosts.get(allPosts.size()-1);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        //Want specific user
        query.whereEqualTo(Post.KEY_USER,user);

        query.orderByDescending(Post.KEY_CREATEDAT);
        query.whereLessThan(Post.KEY_CREATEDAT,lastPost.getCreatedAt());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e!=null)
                {
                    Log.i(TAG,"Issue with loading more post");
                    return;
                }
                for(Post post:posts)
                {
                    Log.i(TAG, "Post: "+post.getDescription());
                }
                addAll(posts);
            }
        });

    }

    public void addAll(List<Post> posts)
    {
        allPosts.addAll(posts);
        adapter.notifyDataSetChanged();
    }

    protected void clear()
    {
        allPosts.clear();
        adapter.notifyDataSetChanged();
    }
}