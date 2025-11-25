package com.example.todolist.Ui.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todolist.Data.entity.User;
import com.example.todolist.R;
import com.example.todolist.Repository.UserRepository; // <-- Import UserRepository
import com.example.todolist.Ui.activity.MainActivity;
import com.example.todolist.utils.SecurityUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvLinkToSignup;

    private UserRepository userRepository; // <-- Khai báo UserRepository

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // Khởi tạo Repository
        userRepository = new UserRepository(getApplication()); // <-- Khởi tạo ở đây

        setupWindowInsets();
        initViews();
        setupEventListeners();
    }

    // ... (các hàm initViews, setupWindowInsets, setupEventListeners giữ nguyên không đổi) ...

    private void initViews() {
        etUsername = findViewById(R.id.et_username_login);
        etPassword = findViewById(R.id.et_password_login);
        btnLogin = findViewById(R.id.btn_login);
        tvLinkToSignup = findViewById(R.id.tv_link_to_signup);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupEventListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        tvLinkToSignup.setOnClickListener(v -> navigateToSignup());
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên đăng nhập và Mật khẩu.", Toast.LENGTH_SHORT).show();
            return;
        }

        // BƯỚC 1: Lấy thông tin người dùng từ REPOSITORY bằng `username`.
        User userFromDb = userRepository.findUserByUsername(username); // <-- Thay đổi ở đây

        if (userFromDb == null) {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
            return;
        }

        // BƯỚC 2 & 3: Mã hóa và so sánh mật khẩu (giữ nguyên logic)
        String hashedPassword = SecurityUtils.hashPassword(password);
        if (userFromDb.getPassword().equals(hashedPassword)) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            getSharedPreferences("USER_DATA", MODE_PRIVATE)
                    .edit()
                    .putString("USERNAME", username)
                    .apply();
            navigateToMain(userFromDb.getUsername());
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
        }
    }

    // ... (các hàm navigateToMain và navigateToSignup giữ nguyên không đổi) ...
    private void navigateToMain(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish();
    }

    private void navigateToSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
