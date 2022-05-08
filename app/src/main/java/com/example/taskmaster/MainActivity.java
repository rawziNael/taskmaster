package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TASKNAME = "taskName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newTask = findViewById(R.id.addTaskButton);
        newTask.setOnClickListener(this.newTask);

        Button allTasks = findViewById(R.id.allTasksButton);
        allTasks.setOnClickListener(this.allTasks);

        //lab27

        Button taskDetailsBtn1 = findViewById(R.id.taskDetailsButton);
        taskDetailsBtn1.setOnClickListener(doShopping);

        Button taskDetailsBtn2 = findViewById(R.id.makeTaskDetailsButton1);
        taskDetailsBtn2.setOnClickListener(doWalking);

        Button taskDetailsBtn3 = findViewById(R.id.makeTaskDetailsButton2);
        taskDetailsBtn3.setOnClickListener(doHomework);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preference.getString("username", "user") + "'s Tasks";
        TextView userLabel = findViewById(R.id.userTasksLabel);
        userLabel.setText(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                goToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener newTask = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(), AddTask.class);
            startActivity(i);
        }
    };

    private View.OnClickListener allTasks = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getBaseContext(), AllTasks.class);
            startActivity(i);
        }
    };

    private final View.OnClickListener doShopping = new View.OnClickListener() {
        public void onClick(View v) {
            Button taskDetailsButton = findViewById(R.id.taskDetailsButton);
            String buttonText = taskDetailsButton.getText().toString();
            Intent intent = new Intent(getBaseContext(), TaskDetailActivity.class);
            intent.putExtra(TASKNAME, buttonText);
            startActivity(intent);
        }
    };

    private final View.OnClickListener doWalking = new View.OnClickListener() {
        public void onClick(View v) {
            Button makeTaskDetailsButton1 = findViewById(R.id.makeTaskDetailsButton1);
            String buttonText = makeTaskDetailsButton1.getText().toString();
            Intent intent = new Intent(getBaseContext(), TaskDetailActivity.class);
            intent.putExtra(TASKNAME, buttonText);
            startActivity(intent);
        }
    };

    private final View.OnClickListener doHomework = new View.OnClickListener() {
        public void onClick(View v) {
            Button makeTaskDetailsButton2 = findViewById(R.id.makeTaskDetailsButton2);
            String buttonText = makeTaskDetailsButton2.getText().toString();
            Intent intent = new Intent(getBaseContext(), TaskDetailActivity.class);
            intent.putExtra(TASKNAME, buttonText);
            startActivity(intent);
        }
    };

    private void goToSettings(){
            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);
    }
}