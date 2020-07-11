package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_CREATEDAT = "createdAt";


    // Set up empty constructor to register as ParseObject subclass
    public Comment(){}

    public Comment(ParseUser user, Post post, String content) {
        put(KEY_USER, user);
        put(KEY_POST, post);
        put(KEY_CONTENT, content);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public Post getPost() {
        return (Post) get(KEY_POST);
    }

    public String getContent() {
        return getString(KEY_CONTENT);
    }
}
