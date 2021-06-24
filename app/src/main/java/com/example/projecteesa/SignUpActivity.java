package com.example.projecteesa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText mName;
    EditText mPassword;
    EditText mEmail;
    EditText mPhoneNum;
    Button mCreate;
    FirebaseAuth mAuth;
    CheckBox visibleSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mName=(EditText) findViewById(R.id.create_name);
        mEmail=(EditText) findViewById(R.id.create_email);
        visibleSignIn=(CheckBox) findViewById(R.id.sign_up_checkbox);
        mPassword=(EditText) findViewById(R.id.create_password);
        mPhoneNum=(EditText) findViewById(R.id.create_phone_num);
        mCreate=(Button) findViewById(R.id.create);
        visibleSignIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
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
        final String name=mName.getText().toString().trim();
        String email=mEmail.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        final String phoneNum=mPhoneNum.getText().toString().trim();
        if(name.equals(""))
        {
            Toast.makeText(this,"Enter your name!",Toast.LENGTH_SHORT).show();
        }
        else if(email.isEmpty())
        {
            Toast.makeText(this,"Please Enter your email",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this,"Please enter a valid email address",Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty())
        {
            Toast.makeText(this,"Enter a password!",Toast.LENGTH_SHORT).show();
        }
        else if(password.length()<6)
        {
            Toast.makeText(this,"Password must be greater than equal to 6 characters",Toast.LENGTH_SHORT).show();
        }
        else if(phoneNum.isEmpty())
        {
            Toast.makeText(this,"Enter your phone number!",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.PHONE.matcher(phoneNum).matches())
        {
            Toast.makeText(this,"Please enter a valid phone number.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.setMessage("Adding Data...");
                    FirebaseUser user=authResult.getUser();
                    HashMap<Object,String> userData=new HashMap<>();
                    userData.put("name",name);
                    userData.put("uid",user.getUid());
                    userData.put("email",email);
                    userData.put("PhoneNo",phoneNum);
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference=firebaseDatabase.getReference("Users");
                    databaseReference.child(user.getUid()).setValue(userData);
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUpActivity.this,"Verification E-mail has been sent. Please verify!",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this,"Error in sending verfication e-mail",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,"Unable to signup.",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        }

    }
}