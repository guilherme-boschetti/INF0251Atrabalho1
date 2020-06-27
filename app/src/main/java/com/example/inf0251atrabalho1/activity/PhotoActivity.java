package com.example.inf0251atrabalho1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.inf0251atrabalho1.R;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // Seta como Tela cheia
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Remove a ToolBar
        }

        Intent it = getIntent();
        if (it.hasExtra("photoPath")) {
            Glide.with(this).load(it.getStringExtra("photoPath")).into((ImageView) findViewById(R.id.photo));
        }
    }
}
