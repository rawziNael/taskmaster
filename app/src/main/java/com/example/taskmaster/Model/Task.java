package com.example.taskmaster.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_data")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "body")
    private final String body;

    @ColumnInfo(name = "status")
    private final String status;

    public Task(String title, String body, String status) {
        this.title = title;
        this.body = body;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }
}
