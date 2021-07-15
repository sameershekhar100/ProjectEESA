package com.example.projecteesa.ProfileSection;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.bumptech.glide.Glide;




import com.example.projecteesa.MainActivity;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.ActivityProgressDialog;
import com.example.projecteesa.utils.MotionToastUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    private static final int IMAGE_CODE = 1;
    public EditText name;
    public EditText BIO;
    public EditText phoneNo;
    public Button update;
    CircleImageView imageView;
    Uri downloadurl;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    StorageReference mstorageReference;
    String firebaseAuth = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    Profile profile;
    DocumentReference doc = firestore.document("Users/" + firebaseAuth);

    private ActivityProgressDialog progressDialog;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();
        progressDialog = new ActivityProgressDialog(mContext);
        name = findViewById(R.id.edit_name);
        BIO = findViewById(R.id.edit_bio);
        phoneNo = findViewById(R.id.edit_Phone);
        update = findViewById(R.id.update);
        imageView = findViewById(R.id.image);
        Intent intent = getIntent();
        profile = (Profile) intent.getSerializableExtra("profile");
        mstorageReference = FirebaseStorage.getInstance().getReference().child("images");

        name.setText(profile.getName());
        BIO.setText(profile.getBio());
        phoneNo.setText(profile.getPhoneNO());
        setProfileData();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_CODE);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    //function to set the existing profile data in the UI
    private void setProfileData() {
        name.setText(profile.getName());
        BIO.setText(profile.getBio() == null ? "" : profile.getBio());
        phoneNo.setText(profile.getPhoneNO() == null ? "" : profile.getPhoneNO());
        if (profile.getImage() != null && !profile.getImage().isEmpty())
            Glide.with(this).load(profile.getImage()).into(imageView);
    }

    void updateProfile() {
        String namex = name.getText().toString();
        String BIOx = BIO.getText().toString();
        String phonex = phoneNo.getText().toString();
        profile.setName(namex);
        profile.setBio(BIOx);
        profile.setPhoneNO(phonex);
        if (downloadurl != null && !downloadurl.toString().isEmpty())
            profile.setImage(downloadurl.toString());

        progressDialog.setTitle("Updating profile data");
        progressDialog.setMessage("Please wait while we update your profile data");
        progressDialog.setCancelable(false);
        progressDialog.showDialog();
        doc.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.hideDialog();
                MotionToastUtils.showSuccessToast(mContext, "Profile updated", "Your profile data is successfully updated");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.hideDialog();
                MotionToastUtils.showErrorToast(mContext, "Error", "Some error occurred while updating your profile data, try after some time");
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

                //compression of selected image to 10% of the actual size
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selected);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                byte[] imageData = baos.toByteArray();
                String fileType = getFileType(selected);
                StorageReference photoRef = mstorageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "." + fileType);
                progressDialog.setTitle("Uploading profile picture");
                progressDialog.setMessage("Please wait while we upload your latest profile picture");
                progressDialog.showDialog();

                photoRef.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        progressDialog.hideDialog();
                        downloadurl = urlTask.getResult();
//                    Toast.makeText(EditProfile.this, downloadurl+"", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.hideDialog();
                        MotionToastUtils.showErrorToast(mContext, "Error", "Some error occurred while uploading your profile picture");
                    }
                });

                imageView.setImageBitmap(currentImage);
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }

    private String getFileType(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }
}