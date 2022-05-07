package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TextView successLabel = findViewById(R.id.newTaskSubmitSuccess);
        successLabel.setVisibility(View.GONE);

        Button addTask = findViewById(R.id.newTaskSubmit);
        addTask.setOnClickListener(newTaskCreateListener);

    }

    private final View.OnClickListener newTaskCreateListener = new View.OnClickListener() {
        public void onClick(View v) {
            TextView submit = findViewById(R.id.newTaskSubmitSuccess);
            submit.setVisibility(View.VISIBLE);
        }
    };
}