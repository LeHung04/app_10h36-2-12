package com.example.todolist.Ui.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;import android.widget.TextView;
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

public class SignupActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLinkToLogin;

    private UserRepository userRepository; // <-- Khai báo UserRepository

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        // Khởi tạo Repository
        userRepository = new UserRepository(getApplication()); // <-- Khởi tạo ở đây

        setupWindowInsets();
        initViews();
        setupEventListeners();
    }

    // ... (các hàm initViews, setupWindowInsets, setupEventListeners giữ nguyên không đổi) ...
    private void initViews() {
        etUsername = findViewById(R.id.et_username_signup);
        etPassword = findViewById(R.id.et_password_signup);
        etConfirmPassword = findViewById(R.id.et_confirm_password_signup);
        btnSignup = findViewById(R.id.btn_signup);
        tvLinkToLogin = findViewById(R.id.tv_link_to_login);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupEventListeners() {
        btnSignup.setOnClickListener(v -> handleSignup());
        tvLinkToLogin.setOnClickListener(v -> navigateToLogin());
    }


    private void handleSignup() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!isInputValid(username, password, confirmPassword)) {
            return;
        }

        // Sử dụng repository để kiểm tra username
        if (isUsernameExists(username)) {
            Toast.makeText(this, "Tên đăng nhập này đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sử dụng repository để tạo người dùng
        createUserAndSave(username, password);

        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
        getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .edit()
                .putString("USERNAME", username)
                .apply();
        navigateToMain(username);
    }

    private boolean isInputValid(String username, String password, String confirmPassword) {
        // Logic này không thay đổi
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isUsernameExists(String username) {
        // Thay đổi ở đây: Gọi đến repository thay vì DB trực tiếp
        User existingUser = userRepository.findUserByUsername(username);
        return existingUser != null;
    }

    private void createUserAndSave(String username, String password) {
        String hashedPassword = SecurityUtils.hashPassword(password);
        if (hashedPassword == null) {
            Toast.makeText(this, "Lỗi hệ thống, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);

        // Thay đổi ở đây: Gọi đến repository thay vì DB trực tiếp
        userRepository.insertUser(newUser);
    }

    // ... (các hàm navigateToMain và navigateToLogin giữ nguyên không đổi) ...
    private void navigateToMain(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
