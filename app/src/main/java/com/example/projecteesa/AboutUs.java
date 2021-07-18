package com.example.projecteesa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AboutUs extends AppCompatActivity {

    TextView aboutUs;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        aboutUs=findViewById(R.id.aboutUS);
        getSupportActionBar().hide();
//        firestore.collection("AboutUs").document("AboutUsText").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                String txt=documentSnapshot.getString("Text");
////                aboutUs.setText(txt);
//            }
//        });
    }
}