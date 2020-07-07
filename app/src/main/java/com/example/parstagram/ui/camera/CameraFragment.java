package com.example.parstagram.ui.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.util.List;

public class CameraFragment extends Fragment {

    private static final String TAG = CameraFragment.class.getSimpleName();
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private CameraViewModel cameraViewModel;
    private FragmentCameraBinding binding;

    private String desc;
    private File photoFile;
    private String photoFileName;

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
        binding.btnCamera.setOnClickListener(new cameraOnClickListener());

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

    /**
     *   onClickListener and helper methods to launch camera
     * */
    private class cameraOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Log.i(TAG, "onClick: camera button was clicked by user");
            launchCamera();
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(),
                "com.codepath.fileprovider.Parstagram", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(
                getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                TAG);

        if (!mediaStorageDir.exists()
                && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "getPhotoFileUri: ");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    /**
     *   OnClickListener and helper methods to submit post
     * */
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
                    Toast.makeText(getContext(), getString(R.string.toast_save_err),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "done: Post save was successful!");
                    Toast.makeText(getContext(), getString(R.string.toast_save_succ),
                            Toast.LENGTH_SHORT).show();
                    cameraViewModel.reset();
                }
            }
        });
    }

}