package com.example.projecteesa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.projecteesa.Fragment.PostsFragment;
import com.example.projecteesa.Fragment.HomeFragment;
import com.example.projecteesa.Fragment.ProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BottomNavigation";
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChipNavigationBar navbar =findViewById(R.id.bottom_nav);
        if(savedInstanceState==null){
            navbar.setItemSelected(R.id.home,true);
            fragmentManager=getSupportFragmentManager();
            HomeFragment home=new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.main_frame,home).commit();
        }
        navbar.setOnItemSelectedListener(i -> {
            Fragment fragment=null;
            switch (i){
                case R.id.home:
                    fragment=new HomeFragment();
                    break;
                case R.id.profile:
                    fragment=new ProfileFragment();
                    break;
                case R.id.feed:
                    fragment=new PostsFragment();
                    break;
            }
            if(fragment!=null){
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame,fragment)
                        .commit();
            }
            else {
                Log.e(TAG,"ERROR");
            }
        });
    }

}