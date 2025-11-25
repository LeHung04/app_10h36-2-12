package com.example.todolist.Data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.todolist.Data.entity.Task;
import com.example.todolist.Data.entity.TaskWithCategory;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    // Dùng @Transaction vì ta đang query từ 2 bảng (Task và Category)
    @Transaction
    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    List<TaskWithCategory> getAllTasks();

    @Transaction
    @Query("SELECT * FROM tasks WHERE due_date >= :startOfDay AND due_date <= :endOfDay")
    List<TaskWithCategory> getTasksByDate(long startOfDay, long endOfDay);

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 1")
    int getCompletedCount();

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 0")
    int getNotCompletedCount();

    class CompletedTaskByDate {
        public String date;
        public int total;
    }

    @Query("SELECT date(due_date / 1000, 'unixepoch', 'localtime') AS date, COUNT(*) AS total " +
            "FROM tasks " +
            "WHERE is_completed = 1 " +
            "AND date(due_date / 1000, 'unixepoch', 'localtime') >= date('now', '-' || :days || ' days', 'localtime') " +
            "GROUP BY date " +
            "ORDER BY date ASC")
    List<CompletedTaskByDate> getCompletedTaskCountByDays(int days);
}