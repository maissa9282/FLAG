package com.example.flag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 3000; // الانتظار 3 ثواني (3000 ميلي ثانية)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Question_activity.class);
            startActivity(intent);
            finish(); // حتى لا يرجع المستخدم إلى شاشة البداية عند الضغط على "رجوع"
        }, SPLASH_DELAY);


    }
}