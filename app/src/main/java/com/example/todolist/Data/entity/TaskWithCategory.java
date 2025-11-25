package com.example.todolist.Data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TaskWithCategory {
    @Embedded
    public Task task;

    @Relation(
            parentColumn = "category_id",
            entityColumn = "category_id"
    )
    public Category category;
}