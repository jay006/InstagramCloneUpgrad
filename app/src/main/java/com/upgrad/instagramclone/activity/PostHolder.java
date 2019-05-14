package com.upgrad.instagramclone.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.upgrad.instagramclone.R;

class PostHolder extends RecyclerView.ViewHolder {

    public ImageView postImageView;

    public PostHolder(@NonNull View itemView) {
        super(itemView);
        postImageView = itemView.findViewById(R.id.postImageView);
    }
}
