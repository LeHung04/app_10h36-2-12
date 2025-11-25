package com.example.todolist.Data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Định nghĩa bảng trong CSDL Room
@Entity(tableName = "categories")
public class Category {

    // Cột khóa chính (tự động tăng)
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    private int categoryId;

    // Tên cột trong bảng
    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    // Constructor mặc định (Room yêu cầu có)
    public Category() {}

    // Constructor đầy đủ
    public Category(@NonNull String name) {
        this.name = name;
    }

    // Getter và Setter
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    // Hiển thị chuỗi đại diện cho đối tượng
    @Override
    public String toString() {
        return name;
    }
}
