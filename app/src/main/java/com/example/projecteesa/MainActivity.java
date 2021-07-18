package com.example.projecteesa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.projecteesa.Fragment.HomeFragment;
import com.example.projecteesa.Fragment.ProfileFragment;
import com.example.projecteesa.Fragment.SearchFragment;
import com.example.projecteesa.Posts.CreatePostActivity;
import com.example.projecteesa.ProfileSection.SavedPostsActivity;
import com.example.projecteesa.utils.AccountsUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BottomNavigation";
    FragmentManager fragmentManager;
    private Toolbar toolbar;

    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        AccountsUtil util = new AccountsUtil();
        setupToolbar();
        ChipNavigationBar navbar = findViewById(R.id.bottom_nav);
        if (savedInstanceState == null) {
            navbar.setItemSelected(R.id.home, true);
            fragmentManager=getSupportFragmentManager();
            HomeFragment home=new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.main_frame,home, "home").commit();
        }
        navbar.setOnItemSelectedListener(i -> {
            Fragment fragment = null;
            switch (i) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.search:
                    fragment = new SearchFragment();
                    break;
                case R.id.feed:
                    startActivity(new Intent(mContext, CreatePostActivity.class));
                    break;
                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;
            }
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .addToBackStack("home")
                        .commit();
            } else {
                Log.e(TAG, "ERROR");
            }
        });
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        return true;

                    case R.id.savedPosts:
                        startActivity(new Intent(mContext, SavedPostsActivity.class));
                        return true;


                }
                return false;
            }
        });
    }

}