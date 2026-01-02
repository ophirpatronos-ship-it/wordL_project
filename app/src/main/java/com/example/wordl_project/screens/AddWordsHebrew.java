package com.example.wordl_project.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordl_project.R;
import com.example.wordl_project.services.DatabaseService;
import com.example.wordl_project.views.KeyView;

import java.util.ArrayList;
import java.util.List;

public class AddWordsHebrew extends AppCompatActivity {

    private EditText editTextNewWord;
    private Button btnAddWord;
    private RecyclerView recyclerViewWords;
    private WordAdapter adapter;
    private List<String> wordsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_words_hebrew); // וודאי שזה שם ה-XML שלך

        // קישור רכיבים מה-XML
        editTextNewWord = findViewById(R.id.editTextNewWord);
        btnAddWord = findViewById(R.id.btnAddWord);
        recyclerViewWords = findViewById(R.id.recyclerViewWords);

        // הגדרת ה-RecyclerView
        recyclerViewWords.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WordAdapter(wordsList);
        recyclerViewWords.setAdapter(adapter);

        // טעינת רשימת המילים הקיימת
        loadWords();

        // הגדרת כפתור ההוספה
        btnAddWord.setOnClickListener(v -> {
            String word = editTextNewWord.getText().toString().trim();
            if (!word.isEmpty()) {
                saveWord(word);
            } else {
                editTextNewWord.setError("נא להזין מילה");
            }
        });
    }



    private void saveWord(String word) {
        DatabaseService.getInstance().createNewHebrewWord(word, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(AddWordsHebrew.this, "המילה נוספה!", Toast.LENGTH_SHORT).show();
                editTextNewWord.setText("");
                loadWords(); // רענון הרשימה לאחר הוספה
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AddWordsHebrew.this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWords() {
        DatabaseService.getInstance().getHebrewWordList(new DatabaseService.DatabaseCallback<List<String>>() {
            @Override
            public void onCompleted(List<String> words) {
                wordsList.clear();
                wordsList.addAll(words);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("ManageWords", "טעינה נכשלה", e);
            }
        });
    }

    // --- האדפטר הפנימי של ה-RecyclerView ---
    private class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
        private List<String> list;

        public WordAdapter(List<String> list) { this.list = list; }

        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // יצירת שורה (ניתן להשתמש ב-layout פשוט של אנדרואיד או ליצור XML משלך)
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new WordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() { return list.size(); }

        class WordViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public WordViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
    private void setupKeyboard() {
        int[] letterIds = new int[]{
                R.id.ש, R.id.keyנ, R.id.keyב, R.id.keyג, R.id.keyק, R.id.keyכ,
                R.id.keyע, R.id.keyי, R.id.keyן, R.id.keyח, R.id.keyל, R.id.keyך,
                R.id.keyצ, R.id.keyמ, R.id.keyם, R.id.keyפ, R.id.keyת, R.id.keyר,
                R.id.keyד, R.id.keyא, R.id.keyו, R.id.keyה, R.id.keyף, R.id.keyס,
                R.id.keyט, R.id.keyז
        };

        for (int i = 0; i < letterIds.length; i++) {
            KeyView b = findViewById(letterIds[i]);
            final String letter = b.getText().toString().trim();


        }
    }
    
}