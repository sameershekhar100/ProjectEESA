package com.example.projecteesa;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projecteesa.utils.ActivityProgressDialog;
import com.example.projecteesa.utils.MotionToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText e1;
    Button b1;
    private ActivityProgressDialog progressDialog;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        e1 = findViewById(R.id.txt1);
        b1 = findViewById(R.id.reset);
        progressDialog = new ActivityProgressDialog(mContext);
        progressDialog.setCancelable(false);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fix();
            }
        });
    }

    void fix() {
        String memail = e1.getText().toString();
        if (memail.isEmpty()) {
            Toast.makeText(this, "Enter your email!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(memail).matches()) {
            Toast.makeText(this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Sending password reset mail");
            progressDialog.setMessage("Please wait while we send you email to reset your password");
            progressDialog.hideDialog();
            FirebaseAuth.getInstance().sendPasswordResetEmail(memail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.hideDialog();
                            if (task.isSuccessful()) {
                                MotionToastUtils.showSuccessToast(mContext, "Password reset mail sent", "Please check your email");
                            }
                        }
                    });
        }
    }

}