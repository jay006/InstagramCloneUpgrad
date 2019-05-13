package com.upgrad.instagramclone.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fxn.pix.Pix;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.upgrad.instagramclone.R;
import com.upgrad.instagramclone.helper.FirebaseHelper;
import com.upgrad.instagramclone.helper.SharedPref;
import com.upgrad.instagramclone.model.Post;
import com.upgrad.instagramclone.model.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int RC_PIX = 101;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseHelper.init();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RC_PIX) {
            ArrayList<String> images = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            uploadImage(images.get(0));
        }

    }

    public void uploadClicked(View view) {
        Pix.start(MainActivity.this, RC_PIX, 1);
    }

    private void uploadImage(String imageLocation) {

        final UploadTask uploadTask = FirebaseHelper.setFile(imageLocation);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int percent = (int) (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()) * 100;
                //TODO update the progress dialog from here

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //upload completed
                FirebaseHelper.getImageRef().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        updateDatabase(downloadUrl.toString());
                    }
                });

            }
        });
    }

    private void updateDatabase(String imageUrl) {

        long unixTime = System.currentTimeMillis() / 1000L;
        User user = SharedPref.getInstance(getApplicationContext()).getUser();
        Post post = new Post("", unixTime, imageUrl, user);
        FirebaseHelper.getPostRef().push().setValue(post);

    }
}
