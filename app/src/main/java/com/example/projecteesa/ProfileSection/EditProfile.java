package com.example.projecteesa.ProfileSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projecteesa.Fragment.ProfileFragment;
import com.example.projecteesa.MainActivity;
import com.example.projecteesa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class EditProfile extends AppCompatActivity {
public EditText name;
public EditText email;
public EditText phoneNo;
public Button update;
FirebaseFirestore firestore=FirebaseFirestore.getInstance();
String firebaseAuth=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
Profile profile;
DocumentReference doc=firestore.document("Users/"+firebaseAuth);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name=findViewById(R.id.edit_name);
        email=findViewById(R.id.edit_email);
        phoneNo=findViewById(R.id.edit_Phone);
        update=findViewById(R.id.update);
        Intent intent=getIntent();
        profile=(Profile)intent.getSerializableExtra("profile");
        name.setText(profile.getName());
        email.setText(profile.getEmail());
        phoneNo.setText(profile.getPhoneNO());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }
    void updateProfile(){
        String namex=name.getText().toString();
        String emailx=email.getText().toString();
        String phonex=phoneNo.getText().toString();
        profile.setName(namex);
        profile.setEmail(emailx);
        profile.setPhoneNO(phonex);
        doc.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditProfile.this, "updated!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(EditProfile.this, "some issue occured :|", Toast.LENGTH_SHORT).show();
            }
        });

    }
}