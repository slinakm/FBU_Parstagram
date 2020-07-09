package com.example.parstagram.ui.fragments.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.BitmapManipulation;
import com.example.parstagram.R;
import com.example.parstagram.adapters.ProfilePostsAdapter;
import com.example.parstagram.databinding.FragmentProfileBinding;
import com.example.parstagram.models.Post;
import com.example.parstagram.ui.fragments.profile.ProfileViewModel;
import com.example.parstagram.ui.login.LoginActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment {
    private static final String TAG = com.example.parstagram.ui.fragments.profile.ProfileFragment.class.getSimpleName();
    public static final String KEY_PROFILEPIC = "profilePic";

    private final List<Post> allPosts = new ArrayList<>();

    private ProfileViewModel notificationsViewModel;
    private FragmentProfileBinding binding;
    private static ParseUser user;
    private ProfilePostsAdapter adapter;

    private File photoFile;
    private String photoFileName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        photoFileName = user.getUsername() + ".jpg";

        binding.btnLogout.setVisibility(View.GONE);
        ParseFile image = user.getParseFile(KEY_PROFILEPIC);
        Log.d(TAG, "onViewCreated: " + image);
        if (image != null) {
            Glide.with(this).
                    load(image.getUrl()).
                    transform(new CircleCrop()).
                    into(binding.ivBigProfilePic);
        }

        binding.tvUsername.setText(user.getUsername());

        adapter = new ProfilePostsAdapter(getActivity(), this, allPosts);
        binding.rvProfilePosts.setAdapter(adapter);
        binding.rvProfilePosts.setLayoutManager(new GridLayoutManager(getContext(), 3));

        getProfilePosts();
    }

    private void getProfilePosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.orderByDescending(Post.KEY_CREATEDAT);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "queryPosts: Issue getting posts", e);
                }

                for (Post p: posts) {
                    Log.i(TAG, "Post:" + p.getDescription()
                            + ", User: " + p.getUser().getUsername());
                }

                Log.i(TAG, "done: number of posts = " + posts.size());

                adapter.clear();
                adapter.addAll(posts);
            }
        });

    }


    /**
     *   Logout button
     * */
    private class logoutOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Log.i(TAG, "onClick: submit button was clicked by user");

            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();

            Intent i = new Intent(UserProfileFragment.this.getContext(), LoginActivity.class);
            getContext().startActivity(i);
        }
    }
}
