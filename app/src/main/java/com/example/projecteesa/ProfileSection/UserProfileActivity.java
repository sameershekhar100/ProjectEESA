package com.example.projecteesa.ProfileSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.projecteesa.Fragment.ProfileFragment;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.Constants;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().hide();

        Intent incommingIntent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        TextView titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText("User Profile");
        ProfileFragment profileFragment = new ProfileFragment(incommingIntent.getStringExtra(Constants.USER_UID_KEY));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .commit();
    }
}