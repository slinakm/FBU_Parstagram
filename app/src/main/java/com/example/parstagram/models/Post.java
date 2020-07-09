package com.example.parstagram.models;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String TAG = Post.class.getSimpleName();
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_USER = "user";

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
}
