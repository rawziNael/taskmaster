package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

//        TextView textTitle = findViewById(R.id.text_title);
//        Intent intent = getIntent();
//        textTitle.setText(intent.getStringExtra("title"));

        String taskName = getIntent().getStringExtra(MainActivity.TASK_NAME);
        TextView taskTitle = findViewById(R.id.taskDetailTitle);
        taskTitle.setText(taskName);

//        String taskTitle = getIntent().getStringExtra(MainActivity.TASK_TITLE);
//        TextView taskTitleID = findViewById(R.id.taskDetailTitle);
//        taskTitleID.setText(taskTitle);
//
//        String taskBody = getIntent().getStringExtra(MainActivity.TASK_BODY);
//        TextView taskBodyID = findViewById(R.id.taskDetails);
//        taskBodyID.setText(taskBody);
//
//        String taskState = getIntent().getStringExtra(MainActivity.TASK_STATUS);
//        TextView taskStateID = findViewById(R.id.taskDetailState);
//        taskStateID.setText(taskState);
    }
}