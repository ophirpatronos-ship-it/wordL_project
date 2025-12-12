package com.example.wordl_project.screens;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wordl_project.R;
import com.example.wordl_project.views.KeyView;

public class GameActivity extends AppCompatActivity {


    private String targetWord = "APPLE"; // change if you want
    private int currentRow = 0;
    private int currentCol = 0;

    private LinearLayout[] rows = new LinearLayout[5];
    private TextView[][] cells = new TextView[5][5];

    private StringBuilder currentGuess = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupGrid();
        setupKeyboard();
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
                R.id.keyA, R.id.keyB, R.id.keyC, R.id.keyD, R.id.keyE, R.id.keyF,
                R.id.keyG, R.id.keyH, R.id.keyI, R.id.keyJ, R.id.keyK, R.id.keyL,
                R.id.keyM, R.id.keyN, R.id.keyO, R.id.keyP, R.id.keyQ, R.id.keyR,
                R.id.keyS, R.id.keyT, R.id.keyU, R.id.keyV, R.id.keyW, R.id.keyX,
                R.id.keyY, R.id.keyZ
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

