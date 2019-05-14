package com.upgrad.instagramclone.helper;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class FirebaseHelper {

    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private static FirebaseStorage storage;

    private static DatabaseReference userReference, postReference;

    private static StorageReference storageReference, imageReference;

    public static void init(Context context) {


        //FirebaseApp.initializeApp(context);

        //getInstance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        //created database reference
        userReference = database.getReference("users");
        postReference = database.getReference("posts");

        storageReference = storage.getReference();

    }

    public static UploadTask setFile(String imageLocation) {

        Uri file = Uri.fromFile(new File(imageLocation));
        imageReference = storageReference.child(file.getLastPathSegment());
        return imageReference.putFile(file);

    }

    public static StorageReference getImageRef() {
        return imageReference;
    }

    public static DatabaseReference getPostRef() {
        return postReference;
    }

    public static FirebaseAuth getFirebaseAuth() {
        return auth;
    }
}
