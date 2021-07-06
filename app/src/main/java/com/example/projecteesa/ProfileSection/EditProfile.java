package com.example.projecteesa.ProfileSection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projecteesa.MainActivity;
import com.example.projecteesa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
public EditText name;
public EditText BIO;
public EditText phoneNo;
public Button update;
private static final int IMAGE_CODE=1;
CircleImageView imageView;
Uri downloadurl;
FirebaseFirestore firestore=FirebaseFirestore.getInstance();
StorageReference mstorageReference;
String firebaseAuth=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
Profile profile;
DocumentReference doc=firestore.document("Users/"+firebaseAuth);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();
        name=findViewById(R.id.edit_name);
        BIO =findViewById(R.id.edit_email);
        phoneNo=findViewById(R.id.edit_Phone);
        update=findViewById(R.id.update);
        imageView=findViewById(R.id.image);
        Intent intent=getIntent();
        profile=(Profile)intent.getSerializableExtra("profile");
        mstorageReference= FirebaseStorage.getInstance().getReference().child("images");

        name.setText(profile.getName());
        BIO.setText(profile.getBio());
        phoneNo.setText(profile.getPhoneNO());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent().setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Complete action using"), IMAGE_CODE);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }
    void updateProfile(){
        String namex=name.getText().toString();
        String BIOx= BIO.getText().toString();
        String phonex=phoneNo.getText().toString();
        profile.setName(namex);
        profile.setBio(BIOx);
        profile.setPhoneNO(phonex);
        profile.setImage(downloadurl.toString());


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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {
            assert data != null;
            Uri selected = data.getData();
            try {
                Bitmap currentImage = MediaStore.Images.Media.getBitmap
                        (this.getContentResolver(), selected);


                imageView.setImageBitmap(currentImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            StorageReference photoRef = mstorageReference.child(selected.getLastPathSegment());
            Toast.makeText(EditProfile.this,"Loading",Toast.LENGTH_SHORT).show();

            photoRef.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    downloadurl = urlTask.getResult();
//                    Toast.makeText(EditProfile.this, downloadurl+"", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(EditProfile.this, "error in uploading", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}