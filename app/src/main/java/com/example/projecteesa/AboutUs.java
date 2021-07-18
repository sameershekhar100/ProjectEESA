package com.example.projecteesa;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class AboutUs extends AppCompatActivity {

    TextView eesa, nitp;

    boolean b1 = true, b2 = true;
    LinearLayout l1, l2;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        eesa = findViewById(R.id.eesa);
        nitp = findViewById(R.id.nitp);
        l1 = findViewById(R.id.layout1);
        l2 = findViewById(R.id.layout2);
        getSupportActionBar().hide();
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_down_24, null);
        Drawable r = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_right_24, null);

        eesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b1) {
                    l1.setVisibility(View.VISIBLE);
                    eesa.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down_24, 0, 0, 0);
                    b1 = false;
                    return;
                }
                l1.setVisibility(View.GONE);
                eesa.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_right_24, 0, 0, 0);
                b1 = true;
            }
        });
        nitp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b2) {
                    l2.setVisibility(View.VISIBLE);
                    b2 = false;
                    nitp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down_24, 0, 0, 0);
                    return;
                }
                l2.setVisibility(View.GONE);
                nitp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_right_24, 0, 0, 0);

                b2 = true;
            }
        });
    }
}