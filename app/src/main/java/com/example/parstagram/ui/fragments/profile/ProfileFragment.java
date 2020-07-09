package com.example.parstagram.ui.fragments.profile;

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
import com.example.parstagram.models.Post;
import com.example.parstagram.ui.fragments.camera.CameraFragment;
import com.example.parstagram.ui.login.LoginActivity;
import com.example.parstagram.databinding.FragmentProfileBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
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

        binding.btnLogout.setOnClickListener(new logoutOnClickListener());
        binding.ivBigProfilePic.setOnClickListener(new cameraOnClickListener());

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
     *   onClickListener and helper methods to launch camera
     * */
    private class cameraOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Log.i(TAG, "onClick: camera button was clicked by user");
            photoFile =
                    BitmapManipulation.launchCamera(ProfileFragment.this.getActivity(), ProfileFragment.this,
                            photoFileName, TAG);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: here!");
        if (requestCode == BitmapManipulation.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: picture taken");
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                Bitmap resizedBitmap = BitmapManipulation.scaleToFitWidth(takenImage,
                        (int) getResources().getDimension((R.dimen.resized_post_image)));

                photoFile =
                        BitmapManipulation.writeResizedBitmap(getContext(), photoFileName,
                                resizedBitmap, "resized", TAG);

                Glide.with(getContext()).
                        load(photoFile).
                        transform(new CircleCrop()).
                        into(binding.ivBigProfilePic);

                user.put(KEY_PROFILEPIC, new ParseFile(photoFile));

                user.saveInBackground();

            } else {
                Toast.makeText(getContext(), getString(R.string.toast_camera_err),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *   Logout button
     * */
    private class logoutOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Log.i(TAG, "onClick: submit button was clicked by user");

            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();

            Intent i = new Intent(ProfileFragment.this.getContext(), LoginActivity.class);
            getContext().startActivity(i);
        }
    }
}