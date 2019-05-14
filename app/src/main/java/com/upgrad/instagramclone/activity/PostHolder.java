package com.upgrad.instagramclone.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.upgrad.instagramclone.R;

class PostHolder extends RecyclerView.ViewHolder {


    ImageView logoView, postImageView;
    TextView userName, postTime, userCaption;


    public PostHolder(@NonNull View itemView) {
        super(itemView);

        logoView = itemView.findViewById(R.id.userImageLogo);
        postImageView = itemView.findViewById(R.id.postImageView);
        userName = itemView.findViewById(R.id.userNameTextView);
        postTime = itemView.findViewById(R.id.userPostTime);
        userCaption = itemView.findViewById(R.id.userCaptionTextView);

    }
}
