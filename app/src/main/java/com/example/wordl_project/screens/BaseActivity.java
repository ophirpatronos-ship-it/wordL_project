package com.example.wordl_project.screens;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordl_project.R;
import com.example.wordl_project.services.DatabaseService;
import com.google.firebase.database.annotations.Nullable;

public class BaseActivity extends AppCompatActivity {

    protected DatabaseService databaseService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    protected boolean shouldShowBackButton() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}