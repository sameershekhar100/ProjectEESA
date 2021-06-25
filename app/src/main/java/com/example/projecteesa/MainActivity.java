package com.example.projecteesa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.projecteesa.Fragment.FeedFragment;
import com.example.projecteesa.Fragment.HomeFragment;
import com.example.projecteesa.Fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null)
        {
            HomeFragment fragment=new HomeFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main_frame,fragment).commit();
        }
        bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);
    }
    BottomNavigationView.OnNavigationItemSelectedListener navListner=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId())
            {
                case R.id.home:
                {
                    fragment=new HomeFragment();
                    break;
                }
                case R.id.feed:
                {
                    fragment=new FeedFragment();
                    break;
                }
                case R.id.profile:
                {
                    fragment=new ProfileFragment();
                    break;
                }
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment).commit();
            return true;
        }
    };
}