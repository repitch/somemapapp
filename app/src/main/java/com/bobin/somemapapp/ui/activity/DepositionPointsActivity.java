package com.bobin.somemapapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bobin.somemapapp.R;
import com.bobin.somemapapp.ui.fragment.MapFragment;

public class DepositionPointsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposition_points);

        if (savedInstanceState != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new MapFragment())
                    .commit();
        }
    }
}
