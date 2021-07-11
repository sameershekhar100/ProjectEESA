package com.example.projecteesa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projecteesa.utils.AccountsUtil;
import com.example.projecteesa.utils.ActivityProgressDialog;
import com.example.projecteesa.utils.MotionToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Context mContext = this;

    EditText email, password;
    Button submit;
    private FirebaseAuth mAuth;
    TextView forgPass;
    TextView createAccount;
    private ImageView splashLogo;
    private ConstraintLayout loginLayout;
    private ActivityProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hiding the action bar
        getSupportActionBar().hide();
        progressDialog = new ActivityProgressDialog(mContext);
        progressDialog.setCancelable(false);
        splashLogo = findViewById(R.id.splash_logo);
        loginLayout = findViewById(R.id.login_layout);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        forgPass = findViewById(R.id.forgotPass);
        createAccount = findViewById(R.id.create_account);
        forgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        startSplashAnimation();
    }

    private void startSplashAnimation() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            splashLogo.animate().scaleX(0.0f).scaleY(0.0f).withEndAction(() -> {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    AccountsUtil util=new AccountsUtil();
                    Intent transfer = new Intent(this, MainActivity.class);
                    startActivity(transfer);
                    finish();
                } else {
                    loginLayout.setVisibility(View.VISIBLE);
                }
            });
        }, 2000);

    }

    private void login() {
        String memail = email.getText().toString();
        String mPassword = password.getText().toString();

        if (memail.isEmpty()) {
            MotionToastUtils.showErrorToast(mContext, "Email required", "Please enter your email address");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(memail).matches()) {
            MotionToastUtils.showErrorToast(mContext, "Invalid email", "Please enter a valid email address");
        } else if (mPassword.isEmpty()) {
            MotionToastUtils.showErrorToast(mContext, "Password is empty", "Please enter a valid password");
        }
        else {
            progressDialog.setTitle("Logging in");
            progressDialog.setMessage("Please wait while we log you in");
            progressDialog.showDialog();

            mAuth.signInWithEmailAndPassword(memail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.hideDialog();

                    if (task.isSuccessful()) {
                        AccountsUtil util=new AccountsUtil();
                        MotionToastUtils.showSuccessToast(mContext, "Logged In", "Glad to see you");
                        Intent transfer = new Intent(LoginActivity.this, MainActivity.class);
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
                            MotionToastUtils.showErrorToast(mContext, "Unable to login", "We were to log you in, please check your credentials");
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

}