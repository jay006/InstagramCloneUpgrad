package com.upgrad.instagramclone.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseHelper.init(getApplicationContext());
        if (!FirebaseHelper.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        setPostUpdateListener();

    }

    private void setPostUpdateListener() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseHelper.getPostRef();


        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(MainActivity.this)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Post, PostHolder>(options) {

            @NonNull
            @Override
            public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_item, viewGroup, false);
                return new PostHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull Post model) {
                Glide.with(getApplicationContext()).load(model.getImageUrl()).thumbnail(0.1f).into(holder.postImageView);
            }
        };

        recyclerView.setAdapter(adapter);

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

        showToast("Upload Started");
        final UploadTask uploadTask = FirebaseHelper.setFile(imageLocation);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int percent = (int) ((double) taskSnapshot.getBytesTransferred() / (double) taskSnapshot.getTotalByteCount() * 100);
                //TODO update the progress dialog from here
                Log.d("upload_percent", String.valueOf(percent));
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //upload completed
                FirebaseHelper.getImageRef().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        updateDatabase(uri.toString());
                    }
                });
                showToast("UPload complted");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO update app about its failuer
                showToast("Upload error");
                Log.d("upload_error", e.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateDatabase(String imageUrl) {

        long unixTime = System.currentTimeMillis() / 1000L;
        User user = SharedPref.getInstance(getApplicationContext()).getUser();
        Post post = new Post("", unixTime, imageUrl, user);
        FirebaseHelper.getPostRef().child(String.valueOf(unixTime)).setValue(post);

    }
}
