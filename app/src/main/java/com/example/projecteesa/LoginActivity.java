package com.example.projecteesa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button submit;
    private FirebaseAuth mAuth;
    TextView forgPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        mAuth=FirebaseAuth.getInstance();
        forgPass=findViewById(R.id.forgotPass);

        forgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }
    private void login(){
        String memail= email.getText().toString();
        String mPassword= password.getText().toString();

        if(memail.isEmpty())
        {
            Toast.makeText(this,"Enter your email!",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(memail).matches())
        {
            Toast.makeText(this,"Enter a valid email address!",Toast.LENGTH_SHORT).show();
        }
        else if(mPassword.isEmpty())
        {
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging in...");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(memail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if(task.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), "correct Credentials!!!", Toast.LENGTH_SHORT).show();
                        Intent transfer=new Intent(LoginActivity.this,ProfileFeed.class);
                        transfer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(transfer);
                        finish();

                    }
                    else
                    {

                        if(task.getException() instanceof FirebaseAuthInvalidUserException)
                        {
                            createAlert("Error","This email is not registered","OK");
                        }
                        else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            createAlert("Error","Wrong Password!","OK");
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Unable to login.",Toast.LENGTH_SHORT).show();
                            Objects.requireNonNull(task.getException()).printStackTrace();

                        }
                    }
                }
            });
        }
    }
    private void createAlert(String heading, String message, String possitive)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(heading)
                .setMessage(message)
                .setPositiveButton(possitive,null)
                .create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            Intent transfer=new Intent(this,ProfileFeed.class);
            transfer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(transfer);
            finish();
        }
    }
}