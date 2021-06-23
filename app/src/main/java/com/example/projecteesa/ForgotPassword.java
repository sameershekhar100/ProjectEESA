package com.example.projecteesa;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText e1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        e1 = findViewById(R.id.txt1);
        b1 = findViewById(R.id.reset);

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
            FirebaseAuth.getInstance().sendPasswordResetEmail(memail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "Reset Link sent\n on your Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}