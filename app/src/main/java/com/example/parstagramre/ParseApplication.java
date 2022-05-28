package com.example.parstagramre;

import android.app.Application;

import com.example.parstagramre.Models.Comment;
import com.example.parstagramre.Models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ox4m2pS7GgcbBKn4r89IUbjH5XxlG4FurJ0oiw4G")
                .clientKey("CmNAS3hO0zkckK5M8uCjF0G846ygibymZXcZxtWP")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
