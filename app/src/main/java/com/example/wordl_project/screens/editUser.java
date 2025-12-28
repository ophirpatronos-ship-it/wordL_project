package com.example.wordl_project.screens;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordl_project.R;
import com.example.wordl_project.models.User;
import com.example.wordl_project.services.DatabaseService;
import com.example.wordl_project.utils.ImageUtil;
import com.example.wordl_project.utils.SharedPreferencesUtil;

public class editUser extends AppCompatActivity {
    private Button btnEditUser;
    private TextView txtUserName, txtEmail, txtPassword, userScore, userWinRate;
    private ImageView imgUserProfile;
    private ImageButton btnChangePhoto;

    private static final int REQ_CAMERA = 100;
    private static final int REQ_GALLERY = 200;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = SharedPreferencesUtil.getUser(this);

        btnEditUser = findViewById(R.id.btn_edit_profile);
        btnEditUser.setOnClickListener(v -> openEditDialog());

        imgUserProfile = findViewById(R.id.iv_profile_picture);
        btnChangePhoto = findViewById(R.id.btn_change_photo);

        btnChangePhoto.setOnClickListener(v -> openImagePicker());

        imgUserProfile.setOnClickListener(v -> {
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                showFullImageDialog();
            }
        });

        txtUserName = findViewById(R.id.tv_user_name);
        txtEmail = findViewById(R.id.tv_email);
        txtPassword = findViewById(R.id.tv_password);
        userScore = findViewById(R.id.tv_user_score);
        userWinRate = findViewById(R.id.tv_user_win_rate);

        loadUserDetailsFromSharedPref();
    }

    private void loadUserDetailsFromSharedPref() {
        txtUserName.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtPassword.setText(user.getPassword());
        userScore.setText(String.valueOf(user.getScore()));
        userWinRate.setText(String.format("%.1f%%", user.getSucssesRate() * 100));

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Bitmap bmp = ImageUtil.convertFrom64base(user.getImage());
            if (bmp != null) {
                imgUserProfile.setImageBitmap(bmp);
            }
        } else {
            imgUserProfile.setImageResource(R.drawable.ic_user);
        }
    }

    private void openEditDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_user);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText inputUserName = dialog.findViewById(R.id.inputEditUserName);
        EditText inputPassword = dialog.findViewById(R.id.inputEditUserPassword);
        Button btnSave = dialog.findViewById(R.id.btnEditUserSave);
        Button btnCancel = dialog.findViewById(R.id.btnEditUserCancel);

        inputUserName.setText(user.getUsername());
        inputPassword.setText(user.getPassword());

        btnSave.setOnClickListener(v -> {
            String uName = inputUserName.getText().toString().trim();
            String pass = inputPassword.getText().toString().trim();

            if (uName.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "מלא את כל השדות", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setUsername(uName);
            user.setPassword(pass);

            DatabaseService.getInstance().updateUser(user, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    SharedPreferencesUtil.saveUser(editUser.this, user);
                    loadUserDetailsFromSharedPref();
                    Toast.makeText(editUser.this, "הפרטים עודכנו", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(editUser.this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void openImagePicker() {
        ImageUtil.requestPermission(this);

        boolean hasImage = user.getImage() != null && !user.getImage().isEmpty();

        String[] options = hasImage
                ? new String[]{"צלם תמונה", "בחר מהגלריה", "מחק תמונת פרופיל"}
                : new String[]{"צלם תמונה", "בחר מהגלריה"};

        new AlertDialog.Builder(this)
                .setTitle("בחר תמונת פרופיל")
                .setItems(options, (dialog, which) -> {

                    if (which == 0) { // Camera
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, REQ_CAMERA);

                    } else if (which == 1) { // Gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, REQ_GALLERY);

                    } else if (hasImage && which == 2) { // Delete image
                        deleteProfileImage();
                    }
                })
                .show();
    }

    private void deleteProfileImage() {
        user.setImage(null);

        imgUserProfile.setImageResource(R.drawable.ic_user);

        DatabaseService.getInstance().updateUser(user, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                SharedPreferencesUtil.saveUser(editUser.this, user);
                Toast.makeText(editUser.this, "תמונת הפרופיל נמחקה", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(editUser.this, "שגיאה במחיקת התמונה", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        Bitmap bitmap = null;

        if (requestCode == REQ_CAMERA && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        else if (requestCode == REQ_GALLERY && data != null) {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData())
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (bitmap != null) {
            imgUserProfile.setImageBitmap(bitmap);

            //המרה ל־Base64 ושמירה
            String base64 = ImageUtil.convertTo64Base(imgUserProfile);
            user.setImage(base64);

            saveProfileImage();
        }
    }

    private void saveProfileImage() {
        DatabaseService.getInstance().updateUser(user, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                SharedPreferencesUtil.saveUser(editUser.this, user);

                Toast.makeText(editUser.this, "תמונת הפרופיל עודכנה!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(editUser.this, "שגיאה בעדכון התמונה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFullImageDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_full_image);

        ImageView dialogImage = dialog.findViewById(R.id.dialogImage);
        dialogImage.setImageDrawable(imgUserProfile.getDrawable());
        dialogImage.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}