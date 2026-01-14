package com.example.wordl_project.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordl_project.R;
import com.example.wordl_project.models.StringWrapper;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    public interface OnWordClickListener {
        void onWordClick(StringWrapper word);
        void onLongWordClick(StringWrapper word);
    }

    private List<StringWrapper> wordList;
    private OnWordClickListener onWordClickListener;
    public WordAdapter(@Nullable final OnWordClickListener onWordClickListener) {
        this.wordList = new ArrayList<>();
        this.onWordClickListener = onWordClickListener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // כאן אנחנו קוראים ל-Layout החדש שיצרנו במקום ל-simple_list_item_1
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        StringWrapper word = wordList.get(position);
        holder.textView.setText(word.getText());

        holder.btnDelete.setOnClickListener(v -> {
            Log.d("DeleteTest", "כפתור המחיקה נלחץ עבור המילה: " + word); // בדיקה ב-Logcat
            onWordClickListener.onWordClick(word);

        });
    }
    public void setWordList(List<StringWrapper> words) {
        wordList.clear();
        wordList.addAll(words);
        notifyDataSetChanged();
    }

    public void addWord(StringWrapper word) {
        wordList.add(word);
        notifyItemInserted(wordList.size() - 1);
    }
    public void updateWord(StringWrapper word) {
        int index = wordList.indexOf(word);
        if (index == -1) return;
        wordList.set(index, word);
        notifyItemChanged(index);
    }

    public void removeWord(StringWrapper word) {
        int index = wordList.indexOf(word);
        if (index == -1) return;
        wordList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() { return wordList.size(); }

    class WordViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View btnDelete;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewWord);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
