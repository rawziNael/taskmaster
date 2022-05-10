package com.example.taskmaster.Model;

public class Task {

    private final String title;
    private final String body;
    private final String status;

    public Task(String title, String body, String status) {
        this.title = title;
        this.body = body;
        this.status = status;
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
