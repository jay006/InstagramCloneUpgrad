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
import com.upgrad.instagramclone.model.User;

import java.io.File;
import java.util.Date;


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

    public static boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public static void saveUser(Context context) {
        User user = SharedPref.getInstance(context).getUser();
        userReference.push().setValue(user);
    }

    public static String getTime(long seconds) {
        long timeMilli = new Date().getTime();
        long currentSecond = timeMilli/1000;
        seconds = currentSecond - seconds;
        if (seconds > 59) {
            long minutes = seconds / 60;
            if (minutes > 59) {
                long hours = minutes / 60;
                if (hours > 23) {
                    int days = (int) hours / 24;
                    if (days > 364) {
                        int year = days / 365;
                        return "asked " + String.valueOf(year) + " years ago";
                    } else {
                        return "asked " + String.valueOf(days) + " days ago";
                    }
                } else {
                    return "asked " + String.valueOf(hours) + " hours ago";
                }
            } else {
                return "asked " + String.valueOf(minutes) + " mins ago";
            }
        } else {
            return "asked " + String.valueOf(seconds) + " seconds ago";
        }
    }
}
