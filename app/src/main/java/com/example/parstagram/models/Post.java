package com.example.parstagram.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String TAG = Post.class.getSimpleName();
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_USER = "user";
    public static final String KEY_LIKES = "likes";


    // Set up empty constructor to register as ParseObject subclass
    public Post(){}

    public Post(String desc, ParseFile file, ParseUser user) {
        put(KEY_DESCRIPTION, desc);
        put(KEY_IMAGE, file);
        put(KEY_USER, user);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public String getRelativeTime(){
        return getRelativeTimeAgo(getCreatedAt());
    }

    private String getRelativeTimeAgo(Date date) {
        String relativeDate = "";
        long dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public int getLikes() {
        ArrayList<String> likes = (ArrayList<String>) get(KEY_LIKES);

        if (likes != null) {
            return new HashSet<>(likes).size();
        } else {
            put(KEY_LIKES, new ArrayList<String>());
            saveInBackground();

            return 0;
        }
    }

    public boolean isLiked() {
        ArrayList<String> likes = (ArrayList<String>) get(KEY_LIKES);

        if (likes == null) {
            likes = new ArrayList<>();
            return false;
        } else {
            return likes.contains(ParseUser.getCurrentUser().getObjectId());
        }
    }

    public void addLike() {
        ArrayList<String> likes = (ArrayList<String>) get(KEY_LIKES);

        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(ParseUser.getCurrentUser().getObjectId());
        put(KEY_LIKES, likes);
        saveInBackground();

//        add(KEY_LIKES, ParseUser.getCurrentUser().getObjectId());
    }

    public void unlike() {
        ArrayList<String> likes = (ArrayList<String>) get(KEY_LIKES);
        likes.remove(ParseUser.getCurrentUser().getObjectId());
        put(KEY_LIKES, likes);
        saveInBackground();
//        likes.remove(ParseUser.getCurrentUser().getObjectId());
//        addAllUnique(KEY_LIKES, likes);
    }

}
