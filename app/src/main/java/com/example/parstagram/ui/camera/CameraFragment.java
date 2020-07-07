package com.example.parstagram.ui.camera;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parstagram.Models.Post;
import com.example.parstagram.R;
import com.example.parstagram.databinding.FragmentCameraBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CameraFragment extends Fragment {

    private static final String TAG = CameraFragment.class.getSimpleName();
    private CameraViewModel cameraViewModel;
    private FragmentCameraBinding binding;

    private String desc;

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

        cameraViewModel.getDescription().observe(getViewLifecycleOwner(), new descObserver());

        binding.btnSubmit.setOnClickListener(new postSubmissionOnClickListener());

//        queryPosts();
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

    private class descObserver implements Observer<String> {
        @Override
        public void onChanged(@Nullable String s) {
            desc = s;
            binding.etDesc.setText(desc);
        }
    }

    private class postSubmissionOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Log.i(TAG, "onClick: submit button was clicked by user");
            checkPostable();
        }
    }

    private void checkPostable(){
        Log.d(TAG, "checkPostable: description =" + desc);
        desc = binding.etDesc.getText().toString();

        if (desc.isEmpty()) {
            Toast.makeText(getContext(),
                    R.string.toast_desc_empt, Toast.LENGTH_SHORT).show();
        } else {
            ParseUser currUser = ParseUser.getCurrentUser();
            savePost(desc, currUser);
        }
    }

    private void savePost(String desc, ParseUser currUser) {
        Post post = new Post(desc, currUser);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "done: Error while saving post", e);
                    Toast.makeText(getContext(), "Error while saving!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "done: Post save was successful!");
                    cameraViewModel.reset();
                }
            }
        });
    }

}