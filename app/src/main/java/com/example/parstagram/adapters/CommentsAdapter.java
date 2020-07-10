package com.example.parstagram.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.models.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private static final String TAG = CommentsAdapter.class.getSimpleName();
    private static Activity context;
    private static Fragment fragment;
    private List<Comment> comments;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        ItemP binding = ItemPostBinding.inflate(context.getLayoutInflater(), parent, false);
        return new CommentsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void bind() {}

    }
}
