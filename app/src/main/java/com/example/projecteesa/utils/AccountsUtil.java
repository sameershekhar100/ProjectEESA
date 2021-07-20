package com.example.projecteesa.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projecteesa.ProfileSection.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AccountsUtil {
    FirebaseFirestore firestore;
    static CollectionReference db;
    static FirebaseAuth auth;
    static FirebaseUser firebaseUser;
    static Profile profile;
    static AccountsUtil accountsUtil;
    static String uid;

    public AccountsUtil() {
        if (accountsUtil == null) {
            firestore = FirebaseFirestore.getInstance();
            db = firestore.collection("Users");
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            DocumentReference doc = db.document(Objects.requireNonNull(auth.getUid()));
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    profile = documentSnapshot.toObject(Profile.class);

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.i("Error: ", "Something went wrong!");
                }
            });
        }
    }

    public static Profile fetchData() {
        if (profile != null) {
            return profile;
        } else {
            return null;
        }
    }

    public static String getUID() {
        uid = FirebaseAuth.getInstance().getUid();
        return uid;
    }

    public static void notifyProfileDataChange() {
        accountsUtil = new AccountsUtil();
    }
}
