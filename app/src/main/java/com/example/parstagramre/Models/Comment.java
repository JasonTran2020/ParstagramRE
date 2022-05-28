package com.example.parstagramre.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;

@Parcel(analyze = Comment.class)
@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_BODY="body";
    public static final String KEY_USER="user";
    public static final String KEY_POST="post";
    public static final String KEY_CREATEDAT="createdAt";

    public String getDescription()
    {
        return getString(KEY_BODY);
    }
    public void setDescription(String description)
    {
        put(KEY_BODY,description);
    }


    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser parseUser)
    {
        put(KEY_USER,parseUser);
    }

    public ParseObject getPost(){return getParseObject(KEY_POST);}
    public void setPost(Post post){put(KEY_POST,post);}

    public Date getKeyCreatedat(){return getCreatedAt();}
}
