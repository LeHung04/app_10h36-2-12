package com.example.todolist.Data.Repository;


import android.content.Context;
import com.example.todolist.Data.AppDatabase;
import com.example.todolist.Data.dao.CategoryDao;
import com.example.todolist.Data.entity.Category;

import java.util.List;

public class CategoryRepository {
    private final CategoryDao categoryDao;

    public CategoryRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.categoryDao = db.categoryDao();
    }

    public long insert(Category category) {
        return categoryDao.insertCategory(category);
    }

    public void update(Category category) {
        categoryDao.updateCategory(category);
    }

    public void delete(Category category) {
        categoryDao.deleteCategory(category);
    }

    public List<Category> getAll() {
        return categoryDao.getAllCategories();
    }
}

