package com.example.parstagramre.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagramre.Models.Post;
import com.example.parstagramre.Adapters.PostAdapter;
import com.example.parstagramre.R;
import com.example.parstagramre.Utility.EndlessRecyclerViewScrollListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    public static final String TAG="PostFragment";
    private RecyclerView rvPosts;
    protected PostAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    protected LinearLayoutManager layoutManager;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts=view.findViewById(R.id.rvPosts);
        swipeRefreshLayout=view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clear();
                queryPosts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        allPosts=new ArrayList<>();
        adapter=new PostAdapter(getContext(),allPosts);

        layoutManager=new LinearLayoutManager(getContext());
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(layoutManager);

        endlessRecyclerViewScrollListener=new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMore();
            }
        };

        rvPosts.addOnScrollListener(endlessRecyclerViewScrollListener);

        queryPosts();

    }


    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(2);
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
        query.setLimit(2);
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