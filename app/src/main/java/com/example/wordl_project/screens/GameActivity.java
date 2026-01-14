package com.example.wordl_project.screens;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordl_project.R;
import com.example.wordl_project.models.StringWrapper;
import com.example.wordl_project.services.DatabaseService;
import com.example.wordl_project.views.KeyView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {


    private String targetWord;
    private int currentRow = 0;
    private int currentCol = 0;

    private LinearLayout[] rows = new LinearLayout[5];
    private TextView[][] cells = new TextView[5][5];
    private Button btnmain;
    private StringBuilder currentGuess = new StringBuilder();
    private List<StringWrapper> wordsList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseService.getInstance().getHebrewWordList(new DatabaseService.DatabaseCallback<List<StringWrapper>>() {
            @Override
            public void onCompleted(List<StringWrapper> words) {
                wordsList.clear();
                wordsList.addAll(words);

                // בחירת מילה רנדומלית להתחלת המשחק
                chooseRandomWord();
                // חובה לקרוא לפונקציות האלו כדי שהמשחק יתחיל לעבוד!
                setupGrid();
                setupKeyboard();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void chooseRandomWord() {
        if (!wordsList.isEmpty()) {
            Random random = new Random();
            targetWord = wordsList.get(random.nextInt(wordsList.size())).getText();
        }
    }


    private void setupGrid() {
        rows[0] = findViewById(R.id.row1);
        rows[1] = findViewById(R.id.row2);
        rows[2] = findViewById(R.id.row3);
        rows[3] = findViewById(R.id.row4);
        rows[4] = findViewById(R.id.row5);

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cells[r][c] = (TextView) rows[r].getChildAt(c);
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

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addLetter(letter);
                }
            });
        }

        KeyView del = findViewById(R.id.keyDel);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLetter();
            }
        });

        KeyView enter = findViewById(R.id.keyEnter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitWord();
            }
        });
    }

    private void addLetter(String letter) {
        if (currentCol < 5 && currentRow < 5) {
            cells[currentRow][currentCol].setText(letter);
            currentGuess.append(letter);
            currentCol++;
        }
    }


    private void deleteLetter() {
        if (currentCol > 0) {
            currentCol--;
            cells[currentRow][currentCol].setText("");
            currentGuess.deleteCharAt(currentGuess.length() - 1);
        }
    }

    private void submitWord() {
        if (currentGuess.length() != 5) {
            Toast.makeText(this, "Word must be 5 letters!", Toast.LENGTH_SHORT).show();
            return;
        }

        String guess = currentGuess.toString();
        checkWord(guess);

        if (guess.equals(targetWord)) {
            Toast.makeText(this, "כל הכבוד!!", Toast.LENGTH_LONG).show();
            return;
        }

        currentRow++;
        currentCol = 0;
        currentGuess.setLength(0);

        if (currentRow == 5) {
            Toast.makeText(this, "לא נורא, נסה שוב", Toast.LENGTH_LONG).show();
        }
    }

    private void checkWord(String guess) {
        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            TextView cell = cells[currentRow][i];

            if (g == targetWord.charAt(i)) {
                cell.setBackgroundColor(Color.parseColor("#4CAF50")); // green
            } else if (targetWord.contains(String.valueOf(g))) {
                cell.setBackgroundColor(Color.parseColor("#FFEB3B")); // yellow
            } else {
                cell.setBackgroundColor(Color.parseColor("#9E9E9E")); // gray
            }
        }
    }


}

