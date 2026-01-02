package com.example.wordl_project.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wordl_project.R;
import com.example.wordl_project.utils.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {
    private Button btnLogout, btnNewGame, btnEditUser, btnAddHebrew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAddHebrew = findViewById(R.id.btnAddHebrew);
        btnAddHebrew.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddWordsHebrew.class)));
        btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GameActivity.class)));
        btnEditUser = findViewById(R.id.btnProfile);
        btnEditUser.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, editUser.class)));
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            signOut();
        });
    }

    private void signOut() {
        SharedPreferencesUtil.signOutUser(MainActivity.this);

        Intent landingIntent = new Intent(MainActivity.this, landing.class);
        landingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(landingIntent);
    }
}