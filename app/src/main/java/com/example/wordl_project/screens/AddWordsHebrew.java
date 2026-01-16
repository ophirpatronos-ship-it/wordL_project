package com.example.wordl_project.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordl_project.R;
import com.example.wordl_project.adapters.WordAdapter;
import com.example.wordl_project.models.StringWrapper;
import com.example.wordl_project.services.DatabaseService;
import com.example.wordl_project.views.KeyView;

import java.util.ArrayList;
import java.util.List;

public class AddWordsHebrew extends AppCompatActivity {

    private EditText editTextNewWord;
    private Button btnAddWord, btnHomePage;
    private RecyclerView recyclerViewWords;
    private WordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_words_hebrew); // וודאי שזה שם ה-XML שלך

        // קישור רכיבים מה-XML
        editTextNewWord = findViewById(R.id.editTextNewWord);
        btnAddWord = findViewById(R.id.btnAddWord);
        recyclerViewWords = findViewById(R.id.recyclerViewWords);
        btnHomePage = findViewById(R.id.btnHomePage);
        btnHomePage.setOnClickListener(v -> startActivity(new Intent(AddWordsHebrew.this, MainActivity.class)));

        // הגדרת ה-RecyclerView
        recyclerViewWords.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WordAdapter(new WordAdapter.OnWordClickListener() {
            @Override
            public void onWordClick(StringWrapper word) {
                deleteWordFromDatabase(word);
            }

            @Override
            public void onLongWordClick(StringWrapper word) {

            }
        });
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
        String id = DatabaseService.getInstance().generateHebrewWordId();
        StringWrapper stringWrapper = new StringWrapper(id, word);
        DatabaseService.getInstance().createNewHebrewWord(stringWrapper, new DatabaseService.DatabaseCallback<Void>() {
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
        DatabaseService.getInstance().getHebrewWordList(new DatabaseService.DatabaseCallback<List<StringWrapper>>() {
            @Override
            public void onCompleted(List<StringWrapper> words) {
                adapter.setWordList(words);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("ManageWords", "טעינה נכשלה", e);
            }
        });
    }

    private void deleteWordFromDatabase(StringWrapper wordToDelete) {
        // אנחנו ניגשים ל-Firebase, מחפשים את המילה ומוחקים אותה
        DatabaseService.getInstance().removeHebrewWord(wordToDelete, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void v) {
                Toast.makeText(AddWordsHebrew.this, "המילה '" + wordToDelete + "' נמחקה", Toast.LENGTH_SHORT).show();
                loadWords();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }


    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}