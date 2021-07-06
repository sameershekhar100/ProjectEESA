package com.example.projecteesa;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
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
    AccountsUtil()
    {
        firestore = FirebaseFirestore.getInstance();
        db = firestore.collection("Users");
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        DocumentReference doc = db.document(Objects.requireNonNull(auth.getUid()));
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profile = documentSnapshot.toObject(Profile.class);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("Error: ","Something went wrong!");
            }
        });
    }
    public static Profile fetchData() {
        if(profile==null)
        {
            accountsUtil=new AccountsUtil();
            while (profile==null);
            return profile;
        }
        else
        {
            return profile;
        }
    }
}
