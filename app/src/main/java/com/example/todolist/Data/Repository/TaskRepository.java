package com.example.todolist.Data.Repository;

import android.content.Context;
import com.example.todolist.Data.AppDatabase;
import com.example.todolist.Data.dao.CategoryDao;
import com.example.todolist.Data.dao.TaskDao;
import com.example.todolist.Data.entity.Category;
import com.example.todolist.Data.entity.Task;
import com.example.todolist.Data.entity.TaskWithCategory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {

    private final TaskDao taskDao;
    private final CategoryDao categoryDao; // Cần thêm CategoryDao
    private final ExecutorService executorService;

    public TaskRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.taskDao = db.taskDao();
        this.categoryDao = db.categoryDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // --- CRUD TASK ---
    public long insert(Task task) {
        return taskDao.insert(task);
    }

    public void update(Task task) {
        executorService.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        executorService.execute(() -> taskDao.delete(task));
    }

    // --- QUERY ---
    // Trả về TaskWithCategory để lấy được tên danh mục
    public List<TaskWithCategory> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public List<TaskWithCategory> getTasksByDateRange(long start, long end) {
        return taskDao.getTasksByDate(start, end);
    }

    // --- HELPERS CHO CATEGORY ---

    /**
     * Hàm này sẽ tìm ID của category dựa theo tên.
     * Nếu chưa có tên đó trong DB, nó sẽ tự tạo mới và trả về ID mới.
     */
    public int getCategoryIdByName(String categoryName) {
        Category existing = categoryDao.getCategoryByName(categoryName);
        if (existing != null) {
            return existing.getCategoryId();
        } else {
            // Nếu chưa có thì tạo mới
            Category newCat = new Category(categoryName);
            long newId = categoryDao.insertCategory(newCat);
            return (int) newId;
        }
    }

    // --- THỐNG KÊ ---
    public int getCompletedCount() {
        return taskDao.getCompletedCount();
    }

    public int getNotCompletedCount() {
        return taskDao.getNotCompletedCount();
    }

    public LinkedHashMap<String, Integer> getCompletedTaskCountByDays(int days) {
        List<TaskDao.CompletedTaskByDate> list = taskDao.getCompletedTaskCountByDays(days);
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        if (list != null) {
            for (TaskDao.CompletedTaskByDate item : list) {
                data.put(item.date, item.total);
            }
        }
        return data;
    }
}