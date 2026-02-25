package com.example.wordl_project.screens;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordl_project.R;
import com.example.wordl_project.adapters.LeaderboardAdapter;
import com.example.wordl_project.models.User;
import com.example.wordl_project.services.DatabaseService;

import java.util.Comparator;
import java.util.List;

public class LeaderBoard extends BaseActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        recyclerView = findViewById(R.id.rvLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LeaderboardAdapter();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                users.sort(new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return Integer.compare(o2.getScore(), o1.getScore());
                    }
                });
                adapter.setUserList(users);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

    }
}
