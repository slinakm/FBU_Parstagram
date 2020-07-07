package com.example.parstagram.ui.camera;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parstagram.MainActivity;
import com.example.parstagram.Models.Post;
import com.example.parstagram.R;
import com.example.parstagram.databinding.FragmentCameraBinding;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class CameraFragment extends Fragment {

    private static final String TAG = CameraFragment.class.getSimpleName();
    private CameraViewModel cameraViewModel;
    private FragmentCameraBinding binding;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCameraBinding.inflate(getLayoutInflater());
        cameraViewModel =
                ViewModelProviders.of(this).get(CameraViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraViewModel.getDescription().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.etDesc.setText(s);
            }
        });

        binding.btnCamera.setOnClickListener(new postSubmissionOnClickListener());

        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
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
            }
        });

    }

    private class postSubmissionOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            checkPostable();
        }
    }

    private boolean checkPostable(){
        String desc = cameraViewModel.getDescription().toString();
        if (desc.isEmpty()) {
            Toast.makeText(getContext(),
                    R.string.toast_desc_empt, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            ParseUser currUser = ParseUser.getCurrentUser();
            savePost(desc, currUser);
            return true;
        }
    }

    private void savePost(String desc, ParseUser currUser) {
        Post post = new Post();

    }

}