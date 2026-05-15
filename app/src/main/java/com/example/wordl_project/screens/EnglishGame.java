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
import com.example.wordl_project.models.User;
import com.example.wordl_project.services.DatabaseService;
import com.example.wordl_project.utils.SharedPreferencesUtil;
import com.example.wordl_project.views.KeyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.UnaryOperator;

public class EnglishGame extends AppCompatActivity {

    private static final String TAG = "EnglisGameActivity";
    final int[] letterIds = new int[]{
            R.id.keyA, R.id.keyB, R.id.keyC, R.id.keyD, R.id.keyE, R.id.keyF,
            R.id.keyG, R.id.keyH, R.id.keyI, R.id.keyJ, R.id.keyK, R.id.keyL,
            R.id.keyM, R.id.keyN, R.id.keyO, R.id.keyP, R.id.keyQ, R.id.keyR,
            R.id.keyS, R.id.keyT, R.id.keyU, R.id.keyV, R.id.keyW, R.id.keyX,
            R.id.keyY, R.id.keyZ
    };
    final int green = Color.parseColor("#4CAF50");
    final int yellow = Color.parseColor("#FFEB3B");
    final int gray = Color.parseColor("#9E9E9E");
    private String targetWord;
    private int currentRow = 0;
    private int currentCol = 0;
    private LinearLayout[] rows = new LinearLayout[5];
    private TextView[][] cells = new TextView[5][5];
    private StringBuilder currentGuess = new StringBuilder();
    private List<StringWrapper> wordsList = new ArrayList<>();
    private Map<String, KeyView> keyboardMap = new HashMap<>();
    private Chronometer gameStopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_game);
        gameStopwatch = findViewById(R.id.gameStopwatch);
        gameStopwatch.setBase(android.os.SystemClock.elapsedRealtime()); // איפוס לשעה הנוכחית
        gameStopwatch.start(); // התחלת המדידה
        setupGrid();
        setupKeyboard();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseService.getInstance().getEnglishWordList(new DatabaseService.DatabaseCallback<List<StringWrapper>>() {
            @Override
            public void onCompleted(List<StringWrapper> words) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "words: " + words);
                        for (StringWrapper word : words) {
                            Log.d(TAG, "word: " + word.getText());
                        }
                        wordsList.clear();
                        wordsList.addAll(words);

                        // בחירת מילה רנדומלית להתחלת המשחק
                        chooseRandomWord();
                    }
                });

            }

            @Override
            public void onFailed(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                });

            }
        });
    }

    private void chooseRandomWord() {
        if (wordsList.isEmpty()) {
            Log.d(TAG, "chooseRandomWord: wordsList is empty");
            targetWord = null;
            return;
        }
        Log.d(TAG, "chooseRandomWord: wordsList size: " + wordsList.size());
        Random random = new Random();
        targetWord = wordsList.get(random.nextInt(wordsList.size())).getText();
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

            keyboardMap.put(letter, b);
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
        if (targetWord == null) return;
        if (currentCol >= 5 || currentRow >= 5) return;
        int len = cells[currentRow].length;
        cells[currentRow][len-1-currentCol].setText(letter);
        currentGuess.append(letter);
        currentCol++;
    }

    private void deleteLetter() {
        if (currentCol <= 0) {
            return;
        }
        currentCol--;
        int len = cells[currentRow].length;
        cells[currentRow][len-1-currentCol].setText("");
        currentGuess.deleteCharAt(currentGuess.length() - 1);
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
        int finalScore;
        if (currentGuess.length() != 5) {
            Toast.makeText(this, "המילה חייבת להיות בת 5 אותיות!", Toast.LENGTH_SHORT).show();
            return;
        }

        String guess = currentGuess.toString();
        checkWord(guess);

        // חישוב הזמן שעבר בשניות מתוך ה-Chronometer
        long elapsedMillis = android.os.SystemClock.elapsedRealtime() - gameStopwatch.getBase();
        long secondsElapsed = elapsedMillis / 1000;
        // מקרה של ניצחון
        if (guess.equals(targetWord)) {
            finalScore = calculateScore(currentRow, secondsElapsed); // חישוב הניקוד
            onGameOver(true, finalScore);
            return;
        }

        // מקרה של הפסד
        if (currentRow + 1 == 5) {
            onGameOver(false, 0);
            return;
        }

        currentRow++;
        currentCol = 0;
        currentGuess.setLength(0);

    }

    private void checkWord(String guess) {
        // מערך כדי לעקוב אחרי אותיות במילת המטרה שכבר "השתמשנו" בהן לצורך צביעה ירוקה
        // זה מונע מצב שבו אות תצבע בצהוב למרות שהיא כבר נמצאה כירוקה במקום אחר

        Log.d(TAG, "checkWord: " + guess);

        for (int i = 0; i < 5; i++) {
            char letter = guess.charAt(i);
            Log.d(TAG, "checkWord letter: " + letter);

            int color;
            if (letter == targetWord.charAt(i)) {
                color = green;
            } else if (targetWord.contains(letter + "")) {
                color = yellow; // צהוב
            } else {
                color = gray; // אפור
            }

            Log.d(TAG, "checkWord color: " + color);


            KeyView key = keyboardMap.get(String.valueOf(letter));
            if (key == null) continue;

            updateKeyboardColor(key, color);

            TextView cell = cells[currentRow][i];

            // צביעת התא בלוח
            cell.setBackgroundColor(color);
            cell.setTextColor(Color.WHITE); // מומלץ להוסיף כדי שהטקסט יהיה קריא
            Log.d(TAG, "checkWord cell: " + cell.getText().toString());

        }
    }

    // פונקציית עזר לעדכון צבע המקלדת (שומרת על הצבע ה"חזק" ביותר)
    private void updateKeyboardColor(KeyView key, int color) {
        if (key == null) return;

        Log.d(TAG, "updateKeyboardColor: " + color);

        // השימוש ב-setTint שומר על הפינות המעוגלות והעיצוב המקורי
        key.setKeyColor(color);
    }

    private int calculateScore(int row, long secondsElapsed) {
        int score;

        // ניקוד לפי מספר ניסיון (currentRow מתחיל מ-0)
        switch (row) {
            case 0:
                score = 1000;
                break; // ניסיון 1
            case 1:
                score = 300;
                break; // ניסיון 2
            case 2:
                score = 150;
                break; // ניסיון 3
            case 3:
                score = 100;
                break; // ניסיון 4
            case 4:
                score = 50;
                break; // ניסיון 5
            default:
                score = 0;
        }

        // בונוס זמן: פחות מדקה (60 שניות) מקנה פי 1.5
        if (secondsElapsed < 60 && score > 0) {
            score = (int) (score * 1.5);
        }

        return score;
    }


    private void onGameOver(final boolean isWin, final int score) {
        Log.i(TAG, "onGameOver: " + isWin + " " + score);
        gameStopwatch.stop();
        String timeString = gameStopwatch.getText().toString();
        showGameOverDialog(isWin, timeString, score);

        String userId = SharedPreferencesUtil.getUserId(this);
        DatabaseService.getInstance().updateUser(userId, new UnaryOperator<User>() {
            @Override
            public User apply(User user) {
                if (user == null) return user;
                user.addScore(score);
                user.addTotalWordCount(1);
                if (isWin) user.addSuccessesWordCount(1);
                return user;
            }
        }, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void v) {

            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

}

