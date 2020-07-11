package com.example.parstagram.ui.fragments.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.parstagram.databinding.FragmentDetailsBinding;
import com.example.parstagram.models.Post;

import java.util.Date;

public class DetailsFragment extends DialogFragment {

    FragmentDetailsBinding binding;
    Post post;

    /**
     * Empty constructor required for DialogFragment
     */
    public DetailsFragment () { }

    public static DetailsFragment newInstance(Post post) {
        DetailsFragment frag = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Post.TAG, post);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        post = (Post) getArguments().get(Post.TAG);
        binding.tvDescription.setText(post.getDescription());
        binding.tvUsername.setText(post.getUser().getUsername());
        binding.tvLikes.setText(String.format("%d", post.getLikes()));
        binding.tvDescription.setVisibility(View.GONE);
        binding.tvDescriptionTag.setVisibility(View.GONE);

        Date date = post.getCreatedAt();
        if (date != null) {
            binding.tvTimestamp.setText(date.toString());
        }

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
