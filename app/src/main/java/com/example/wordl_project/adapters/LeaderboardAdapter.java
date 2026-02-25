package com.example.wordl_project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordl_project.R;
import com.example.wordl_project.models.User;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<User> userList;

    public LeaderboardAdapter() {
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leader_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        // דירוג (Position מתחיל מ-0 לכן מוסיפים 1)
        holder.tvRank.setText(String.valueOf(position + 1));

        // שם וניקוד
        holder.tvPlayerName.setText(user.getUsername());
        holder.tvPoints.setText(String.valueOf(user.getScore()));

        // חישוב אחוז הצלחה (לוגיקה קטנה)

        holder.tvWinRate.setText(user.getSuccessesRate() + "%");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<User> userList) {
        this.userList.clear();
        this.userList.addAll(userList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvPlayerName, tvPoints, tvWinRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvWinRate = itemView.findViewById(R.id.tvWinRate);
        }
    }
}

