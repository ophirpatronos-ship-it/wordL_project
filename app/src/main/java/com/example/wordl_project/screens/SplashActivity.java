package com.example.wordl_project.screens;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wordl_project.R;
import com.example.wordl_project.utils.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DISPLAY_TIME = 7000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        VideoView videoView = findViewById(R.id.videoBackground);

// נתיב לסרטון שנמצא בתיקיית res/raw
        String path = "android.resource://" + getPackageName() + "/" + R.raw.splash_background;
        videoView.setVideoPath(path);
        videoView.start();

        Thread splashThread = new Thread(() -> {
            try {
                Thread.sleep(SPLASH_DISPLAY_TIME); // SPLASH_DISPLAY_TIME delay
            } catch (InterruptedException ignored) {
            } finally {
                // go to the correct activity after the delay
                Intent intent;
                /// Check if user is signed in or not and redirect to LandingActivity if not signed in
                if (SharedPreferencesUtil.isUserLoggedIn(this)) {
                    Log.d(TAG, "User signed in, redirecting to MainActivity");
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    Log.d(TAG, "User not signed in, redirecting to LandingActivity");
                    intent = new Intent(SplashActivity.this, landing.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        splashThread.start();
    }

}