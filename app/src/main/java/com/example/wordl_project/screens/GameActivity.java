package com.example.wordl_project.screens;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordl_project.R;
import com.example.wordl_project.models.StringWrapper;
import com.example.wordl_project.services.DatabaseService;
import com.example.wordl_project.views.KeyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    private String targetWord;
    private int currentRow = 0;
    private int currentCol = 0;

    private LinearLayout[] rows = new LinearLayout[5];
    private TextView[][] cells = new TextView[5][5];
    private StringBuilder currentGuess = new StringBuilder();
    private List<StringWrapper> wordsList = new ArrayList<>();


    final int[] letterIds = new int[]{
            R.id.keyש, R.id.keyנ, R.id.keyב, R.id.keyג, R.id.keyק, R.id.keyכ,
            R.id.keyע, R.id.keyי, R.id.keyן, R.id.keyח, R.id.keyל, R.id.keyך,
            R.id.keyצ, R.id.keyמ, R.id.keyם, R.id.keyפ, R.id.keyת, R.id.keyר,
            R.id.keyד, R.id.keyא, R.id.keyו, R.id.keyה, R.id.keyף, R.id.keyס,
            R.id.keyט, R.id.keyז
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameStopwatch = findViewById(R.id.gameStopwatch);
        gameStopwatch.setBase(android.os.SystemClock.elapsedRealtime()); // איפוס לשעה הנוכחית
        gameStopwatch.start(); // התחלת המדידה
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

                for (int id : letterIds) {
                    KeyView b = findViewById(id);
                    String letter = b.getText().toString().trim();

                    // הוספה למפה כדי שנוכל לשנות צבע בהמשך
                    keyboardMap.put(letter, b);

                    b.setOnClickListener(view -> addLetter(letter));
                }

                // ... הקוד של Delete ו-Enter כפי שהיה
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
        for (int letterId : letterIds) {
            KeyView b = findViewById(letterId);
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
    private Map<String, KeyView> keyboardMap = new HashMap<>();

    private void deleteLetter() {
        if (currentCol > 0) {
            currentCol--;
            cells[currentRow][currentCol].setText("");
            currentGuess.deleteCharAt(currentGuess.length() - 1);
        }
    }
    private void showGameOverDialog(boolean isWin, String timeSpent, int score) {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_layout);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView title = dialog.findViewById(R.id.dialogTitle);
        TextView message = dialog.findViewById(R.id.dialogMessage);
        Button btnRestart = dialog.findViewById(R.id.btnRestart);
        Button btnExit = dialog.findViewById(R.id.btnExit);

        if (isWin) {
            title.setText("כל הכבוד! 🏆");
            title.setTextColor(Color.parseColor("#4CAF50"));
            // הצגת הניקוד והזמן בתוך הדיאלוג
            message.setText("ניצחת תוך " + timeSpent + "!\n" +
                    "ניקוד סופי: " + score + "\n" +
                    "המילה היא: " + targetWord);
        } else {
            title.setText("לא נורא :( 💔");
            title.setTextColor(Color.parseColor("#E94560"));
            message.setText("נגמרו הניסיונות. המילה הייתה: " + targetWord);
        }

        btnRestart.setOnClickListener(v -> { dialog.dismiss(); recreate(); });
        btnExit.setOnClickListener(v -> { dialog.dismiss(); finish(); });

        dialog.setCancelable(false);
        dialog.show();
    }
    private void submitWord() {
        if (currentGuess.length() != 5) {
            Toast.makeText(this, "המילה חייבת להיות בת 5 אותיות!", Toast.LENGTH_SHORT).show();
            return;
        }
        int finalScore;

        String guess = currentGuess.toString();
        checkWord(guess);
        DatabaseService.getInstance().updatePlayerScore(finalScore);

        showGameOverDialog(true, gameStopwatch.getText().toString(), finalScore);
        return;
        // חישוב הזמן שעבר בשניות מתוך ה-Chronometer
        long elapsedMillis = android.os.SystemClock.elapsedRealtime() - gameStopwatch.getBase();
        long secondsElapsed = elapsedMillis / 1000;
        String timeString = gameStopwatch.getText().toString();

        // מקרה של ניצחון
        if (guess.equals(targetWord)) {
            gameStopwatch.stop();
            finalScore = calculateScore(currentRow, secondsElapsed); // חישוב הניקוד
            showGameOverDialog(true, timeString, finalScore);
            return;
        }

        // מקרה של הפסד
        if (currentRow + 1 == 5) {
            gameStopwatch.stop();
            showGameOverDialog(false, timeString, 0);
            return;
        }

        currentRow++;
        currentCol = 0;
        currentGuess.setLength(0);

    }


    final int green = Color.parseColor("#4CAF50");
    final int yellow = Color.parseColor("#FFEB3B");
    final int gray = Color.parseColor("#9E9E9E");

    private void checkWord(String guess) {
        boolean[] targetUsed = new boolean[5];
        int[] colors = new int[5];

        // שלב 1: זיהוי ירוקים
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == targetWord.charAt(i)) {
                colors[i] = green;
                targetUsed[i] = true;
            } else {
                colors[i] = -1;
            }
        }

        // שלב 2: זיהוי צהובים ואפורים
        for (int i = 0; i < 5; i++) {
            if (colors[i] == -1) {
                char g = guess.charAt(i);
                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (!targetUsed[j] && g == targetWord.charAt(j)) {
                        colors[i] = yellow;
                        targetUsed[j] = true;
                        found = true;
                        break;
                    }
                }
                if (!found) colors[i] = gray;
            }
        }

        // שלב 3: עדכון UI
        for (int i = 0; i < 5; i++) {
            TextView cell = cells[currentRow][i];
            String letter = String.valueOf(guess.charAt(i));

            cell.getBackground().setTint(colors[i]); // שומר על צורה מעוגלת
            cell.setTextColor(Color.WHITE);

            KeyView key = keyboardMap.get(letter);
            if (key != null) {
                updateKeyboardColor(key, colors[i]);
            }
        }
    }

    // פונקציית עזר לעדכון צבע המקלדת (שומרת על הצבע ה"חזק" ביותר)
    private void updateKeyboardColor(KeyView key, int color) {
        if (key == null) return;

        Log.d(TAG, "updateKeyboardColor: " + color);

        // השימוש ב-setTint שומר על הפינות המעוגלות והעיצוב המקורי
        key.setKeyColor(color);
    }

    private boolean isKeyColor(KeyView key, int color) {
        if (key.getBackground() instanceof android.graphics.drawable.ColorDrawable) {
            return ((android.graphics.drawable.ColorDrawable) key.getBackground()).getColor() == color;
        }
        // במקרה של שימוש ב-Tint, הבדיקה מעט מורכבת יותר, אבל לצורך הפשטות:
        return false;
    }
    private boolean isKeyGreen(KeyView key) {
        if (key.getBackground() instanceof android.graphics.drawable.ColorDrawable) {
            return ((android.graphics.drawable.ColorDrawable) key.getBackground()).getColor() == Color.parseColor("#4CAF50");
        }
        return false;
    }
    private Chronometer gameStopwatch;
    private int calculateScore(int row, long secondsElapsed) {
        int score = 0;

        // ניקוד לפי מספר ניסיון (currentRow מתחיל מ-0)
        switch (row) {
            case 0: score = 1000; break; // ניסיון 1
            case 1: score = 300;  break; // ניסיון 2
            case 2: score = 150;  break; // ניסיון 3
            case 3: score = 100;  break; // ניסיון 4
            case 4: score = 50;   break; // ניסיון 5
            default: score = 0;
        }

        // בונוס זמן: פחות מדקה (60 שניות) מקנה פי 1.5
        if (secondsElapsed < 60 && score > 0) {
            score = (int) (score * 1.5);
        }

        return score;
    }



}

