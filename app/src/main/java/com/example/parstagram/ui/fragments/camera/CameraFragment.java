package com.example.parstagram.ui.fragments.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parstagram.BitmapManipulation;
import com.example.parstagram.models.Post;
import com.example.parstagram.R;
import com.example.parstagram.databinding.FragmentCameraBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {

    private static final String TAG = CameraFragment.class.getSimpleName();

    private CameraViewModel cameraViewModel;
    private FragmentCameraBinding binding;

    private String desc;
    private File photoFile; // TODO: fix camera modelview
    private final String photoFileName = "photo.jpg";

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
        binding.btnLibrary.setOnClickListener(new libraryOnClickListener());
    }

    //TODO: Clean up the code
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

                binding.ivPost.setImageBitmap(resizedBitmap);
                binding.ivPost.setVisibility(View.VISIBLE);
//                binding.btnCamera.setVisibility(View.GONE);
//                binding.btnLibrary.setVisibility(View.GONE);

                photoFile =
                        BitmapManipulation.writeResizedBitmap(getContext(), photoFileName,
                                resizedBitmap, "resized", TAG);
            } else {
                Toast.makeText(getContext(), getString(R.string.toast_camera_err),
                        Toast.LENGTH_SHORT).show();
            }
        }

        if ((data != null) && requestCode == BitmapManipulation.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = BitmapManipulation.loadFromUri(getContext(), photoUri);

            Bitmap resizedBitmap = BitmapManipulation.scaleToFitWidth(selectedImage,
                    (int) getResources().getDimension((R.dimen.resized_post_image)));

            photoFile =
                    BitmapManipulation.writeResizedBitmap(getContext(), photoFileName,
                            resizedBitmap, "resized", TAG);

            // Load the selected image into a preview
            binding.ivPost.setImageBitmap(selectedImage);
            binding.ivPost.setVisibility(View.VISIBLE);
//            binding.btnCamera.setVisibility(View.GONE);
//            binding.btnLibrary.setVisibility(View.GONE);
        }
    }

    private class descObserver implements Observer<String> {
        @Override
        public void onChanged(@Nullable String s) {
            desc = s;
            binding.etDesc.setText(desc);
        }
    }

    /**
     *    onClickListener to open library media
     */
    private class libraryOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: library button was clicked by user");
            BitmapManipulation.onPickPhoto(getContext(), CameraFragment.this);
        }
    }

    /**
     *   onClickListener to open camera
     * */
    private class cameraOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Log.i(TAG, "onClick: camera button was clicked by user");
            photoFile =
                    BitmapManipulation.launchCamera(CameraFragment.this.getActivity(), CameraFragment.this,
                    photoFileName, TAG);
        }
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
        binding.pbLoading.setVisibility(ProgressBar.VISIBLE);

        Log.d(TAG, "checkPostable: description =" + desc);
        Log.d(TAG, "checkPostable: get size of image" + photoFile.getTotalSpace());
        desc = binding.etDesc.getText().toString();

        if (desc.isEmpty()) {
            Toast.makeText(getContext(),
                    R.string.toast_desc_empt, Toast.LENGTH_SHORT).show();
            binding.pbLoading.setVisibility(ProgressBar.INVISIBLE);
        } else if (photoFile == null
                || binding.ivPost.getDrawable() == null) {
            Toast.makeText(getContext(),
                    R.string.toast_img_empt, Toast.LENGTH_SHORT).show();
            binding.pbLoading.setVisibility(ProgressBar.INVISIBLE);
        } else {
            ParseUser currUser = ParseUser.getCurrentUser();
            savePost(desc, photoFile, currUser);
        }
    }

    private void savePost(String desc, File file, ParseUser currUser) {
        Post post = new Post(desc, new ParseFile(file), currUser);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                binding.pbLoading.setVisibility(ProgressBar.INVISIBLE);

                if (e != null) {
                    Log.e(TAG, "done: Error while saving post", e);
                    Toast.makeText(getContext(), getString(R.string.toast_save_err),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "done: Post save was successful!");
                    Toast.makeText(getContext(), getString(R.string.toast_save_succ),
                            Toast.LENGTH_SHORT).show();
                    reset();
                }
            }
        });
    }

    private void reset() {
        cameraViewModel.reset();
        binding.ivPost.setVisibility(View.GONE);
        binding.btnCamera.setVisibility(View.VISIBLE);
        binding.btnLibrary.setVisibility(View.VISIBLE);
        photoFile = null;
    }

}