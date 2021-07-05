package com.example.projecteesa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projecteesa.ProfileSection.Profile;
import com.example.projecteesa.utils.MotionToastUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends AppCompatActivity {
    EditText mName;
    EditText mPassword;
    EditText mEmail;
    EditText mPhoneNum;
    Button mCreate;
    FirebaseAuth mAuth;
    CheckBox visibleSignIn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("Users");

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mName = (EditText) findViewById(R.id.create_name);
        mEmail = (EditText) findViewById(R.id.create_email);
        visibleSignIn = (CheckBox) findViewById(R.id.sign_up_checkbox);
        mPassword = (EditText) findViewById(R.id.create_password);
        mPhoneNum = (EditText) findViewById(R.id.create_phone_num);
        mCreate = (Button) findViewById(R.id.create);
        visibleSignIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String name = mName.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        final String phoneNum = mPhoneNum.getText().toString().trim();
        if (name.equals("")) {
            MotionToastUtils.showErrorToast(mContext, "Email required", "Please enter your email address");
        } else if (email.isEmpty()) {
            MotionToastUtils.showErrorToast(mContext, "Email required", "Please enter your email address");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            MotionToastUtils.showErrorToast(mContext, "Invalid email", "Please enter a valid email address");
        } else if (password.isEmpty()) {
            MotionToastUtils.showErrorToast(mContext, "Password is empty", "Please enter a valid password");
        } else if (password.length() < 6) {
            MotionToastUtils.showErrorToast(mContext, "Password too short", "Please enter a password of length greater than 6");
        } else if (phoneNum.isEmpty()) {
            MotionToastUtils.showErrorToast(mContext, "Phone number empty", "Please enter your phone number");
        } else if (!Patterns.PHONE.matcher(phoneNum).matches()) {
            MotionToastUtils.showErrorToast(mContext, "Invalid phone number", "Please enter a valid phone number");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.setMessage("Adding Data...");
                    FirebaseUser user = authResult.getUser();


                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            add(name,email,phoneNum,user);
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            MotionToastUtils.showInfoToast(mContext, "Verify your email", "Signup was successful but please make sure to verify your email");
                            progressDialog.dismiss();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            MotionToastUtils.showErrorToast(mContext, "Error occurred", "Some error occurred during signup");
                        }
                    });

              }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    MotionToastUtils.showErrorToast(mContext, "Error occured", "Some error occurred during signup");
                    e.printStackTrace();
                }
            });
        }

    }
    void add(String name, String email,String phoneNum,FirebaseUser user) {
        Profile profile = new Profile(name, email, phoneNum);
        String uid=user.getUid();
        users.document(uid+"").set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SignUpActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(SignUpActivity.this, "error!!!!" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

}