package com.example.todolist.Ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.todolist.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cài đặt");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout themeLayout = findViewById(R.id.theme_layout);
        LinearLayout advanceSettingLayout = findViewById(R.id.advance_setting_layout);
        LinearLayout faqLayout = findViewById(R.id.faq_layout);
        LinearLayout feedbackLayout = findViewById(R.id.feedback_layout);
        LinearLayout starredTasksLayout = findViewById(R.id.starred_tasks_layout);

        advanceSettingLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, AdvanceSettingActivity.class);
            startActivity(intent);
        });

        faqLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, FaqActivity.class);
            startActivity(intent);
        });

        feedbackLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, FeedbackActivity.class);
            startActivity(intent);
        });

        starredTasksLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, StarredTasksActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
