package com.example.wordl_project.screens;

import android.graphics.Color;
import android.os.Bundle;
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
                int[] letterIds = new int[]{
                        R.id.keyש, R.id.keyנ, R.id.keyב, // ... שאר ה-IDs שלך
                };

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
        int[] letterIds = new int[]{
                R.id.keyש, R.id.keyנ, R.id.keyב, R.id.keyג, R.id.keyק, R.id.keyכ,
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
    private java.util.HashMap<String, KeyView> keyboardMap = new java.util.HashMap<>();

    private void deleteLetter() {
        if (currentCol > 0) {
            currentCol--;
            cells[currentRow][currentCol].setText("");
            currentGuess.deleteCharAt(currentGuess.length() - 1);
        }
    }
    private void showGameOverDialog(boolean isWin, String timeSpent) {
        // יצירת הדיאלוג
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_layout);

        // הפיכת רקע הדיאלוג המקורי לשקוף (כדי שיראו את הפינות המעוגלות שלנו)
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // קישור רכיבי ה-UI מה-Layout החדש
        TextView title = dialog.findViewById(R.id.dialogTitle);
        TextView message = dialog.findViewById(R.id.dialogMessage);
        Button btnRestart = dialog.findViewById(R.id.btnRestart);
        Button btnExit = dialog.findViewById(R.id.btnExit);

        // התאמת התוכן לפי ניצחון/הפסד
        if (isWin) {
            title.setText("כל הכבוד! 🏆");
            title.setTextColor(Color.parseColor("#4CAF50")); // ירוק
            message.setText("ניצחת תוך " + timeSpent + " שניות!\nהמילה היא: " + targetWord);        } else {
            title.setText("לא נורא :( 💔");
            title.setTextColor(Color.parseColor("#E94560")); // אדום-ורוד
            message.setText("נגמרו הניסיונות.\nהמילה הייתה: " + targetWord);
        }

        // הגדרת כפתורים
        btnRestart.setOnClickListener(v -> {
            dialog.dismiss();
            recreate();
        });

        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void submitWord() {
        if (currentGuess.length() != 5) {
            Toast.makeText(this, "המילה חייבת להיות בת 5 אותיות!", Toast.LENGTH_SHORT).show();
            return;
        }

        String guess = currentGuess.toString();
        checkWord(guess);
        String timeSpent = null;
        // מקרה של ניצחון
        if (guess.equals(targetWord)) {
            gameStopwatch.stop(); // עוצר את השעון
            timeSpent = gameStopwatch.getText().toString();
            showGameOverDialog(true, timeSpent); // מעביר את הזמן לדיאלוג
            return;
        }


        if (currentRow+1 == 5) {
            gameStopwatch.stop();
            timeSpent = gameStopwatch.getText().toString();
            showGameOverDialog(false, timeSpent);
            return;
        }
        currentRow++;
        currentCol = 0;
        currentGuess.setLength(0);

    }

    private void checkWord(String guess) {
        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            TextView cell = cells[currentRow][i];
            String letter = String.valueOf(g);
            KeyView key = keyboardMap.get(letter); // מוצא את המקש המתאים

            if (g == targetWord.charAt(i)) {
                // ירוק - במקום הנכון
                int green = Color.parseColor("#4CAF50");
                cell.setBackgroundColor(green);
                if (key != null) key.setBackgroundColor(green);

            } else if (targetWord.contains(letter)) {
                // צהוב - קיימת במילה
                int yellow = Color.parseColor("#FFEB3B");
                cell.setBackgroundColor(yellow);

                // צובע מקלדת בצהוב רק אם היא לא כבר ירוקה
                if (key != null && !isKeyGreen(key)) {
                    key.setBackgroundColor(yellow);
                }

            } else {
                // אפור - לא במילה
                int gray = Color.parseColor("#9E9E9E");
                cell.setBackgroundColor(gray);
                if (key != null) key.setBackgroundColor(gray);
            }


            if (g == targetWord.charAt(i)) {
                cell.setBackgroundColor(Color.parseColor("#4CAF50")); // green
            } else if (targetWord.contains(String.valueOf(g))) {
                cell.setBackgroundColor(Color.parseColor("#FFEB3B")); // yellow
            } else {
                cell.setBackgroundColor(Color.parseColor("#9E9E9E")); // gray
            }
        }
    }
    private boolean isKeyGreen(KeyView key) {
        if (key.getBackground() instanceof android.graphics.drawable.ColorDrawable) {
            return ((android.graphics.drawable.ColorDrawable) key.getBackground()).getColor() == Color.parseColor("#4CAF50");
        }
        return false;
    }
    private Chronometer gameStopwatch;



}

