package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        String taskName = getIntent().getStringExtra(MainActivity.TASKNAME);
        TextView taskTitle = findViewById(R.id.taskDetailTitle);
        taskTitle.setText(taskName);
    }
}