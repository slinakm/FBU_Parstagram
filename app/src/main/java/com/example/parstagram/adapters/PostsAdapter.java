package com.example.parstagram.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private static final String TAG = PostsAdapter.class.getSimpleName();
    private static Activity context;
    private List<Post> posts;

    public PostsAdapter(Activity context, List<Post> posts) {
        PostsAdapter.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        ItemPostBinding binding = ItemPostBinding.inflate(context.getLayoutInflater(), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;

        private ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Post post) {
            String username = post.getUser().getUsername();
            binding.tvUsername1.setText(username);
            binding.tvUsername2.setText(username);

            binding.tvDescription.setText(post.getDescription());

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(binding.ivPostImage);
            }
        }
    }
}
