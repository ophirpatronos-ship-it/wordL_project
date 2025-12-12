package com.example.wordl_project.views;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.example.wordl_project.R;

public class KeyView extends AppCompatTextView {

    public KeyView(Context context) {
        super(context);
        init();
    }

    public KeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.key_bg));
        setBackgroundTintList(ColorStateList.valueOf(0xFF3F51B5)); // default blue
        setTextColor(0xFFFFFFFF);
        setGravity(CENTER);
        setPadding(8, 8, 8, 8);
    }

    // Optional: nice helper for your Wordle colors
    public void setKeyColor(int color) {
        setBackgroundTintList(ColorStateList.valueOf(color));
    }
}
