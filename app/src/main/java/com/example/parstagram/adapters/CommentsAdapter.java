package com.example.parstagram.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.R;
import com.example.parstagram.databinding.ItemCommentBinding;
import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.example.parstagram.ui.fragments.home.UserProfileFragment;
import com.example.parstagram.ui.fragments.profile.ProfileFragment;
import com.parse.ParseFile;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private static final String TAG = CommentsAdapter.class.getSimpleName();
    private static Activity context;
    private static Fragment fragment;
    private List<Comment> comments;


    public CommentsAdapter(Activity context, Fragment fragment, List<Comment> comments) {
        this.context = context;
        this.fragment = fragment;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        ItemCommentBinding binding = ItemCommentBinding.inflate(context.getLayoutInflater(), parent, false);
        return new CommentsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");
        Comment c = comments.get(position);
        holder.bind(c);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Comment> list) {
        comments.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;

        private ViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(final Comment comment) {
            binding.tvUsername1.setText(comment.getUser().getUsername());
            binding.tvContent.setText(comment.getContent());

            ParseFile profilePic = comment.getUser().getParseFile(ProfileFragment.KEY_PROFILEPIC);
            Log.d(TAG, "bind: " + profilePic);
            if (profilePic != null) {
                Glide.with(context).
                        load(profilePic.getUrl()).
                        transform(new CircleCrop()).
                        into(binding.ivProfilePic);
            }

            class userOnClickListener implements View.OnClickListener {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = fragment.getParentFragmentManager();
                    UserProfileFragment userProfileFragment =
                            UserProfileFragment.newInstance(comment.getUser());

                    FragmentTransaction fts = fm.beginTransaction();
                    // Replace the content of the container
                    fts.replace(R.id.nav_host_fragment,userProfileFragment);
                    // Append this transaction to the backstack
                    fts.addToBackStack(UserProfileFragment.class.getSimpleName());
                    // Commit the changes
                    fts.commit();
                }
            }
            binding.tvUsername1.setOnClickListener(new userOnClickListener());
            binding.ivProfilePic.setOnClickListener(new userOnClickListener());
        }

    }
}
