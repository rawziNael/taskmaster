package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

public class AddTask extends AppCompatActivity {

    private static final String TAG = "AddTask";
    private String spinner_task_status=null;
    private String teamName = null;
    private Team teamData;

//    private AppDatabase database;
//    private TaskDao taskDao;
    private Handler handler;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, MainActivity.TASK_LIST)
//                .allowMainThreadQueries().build();
//        taskDao = database.taskDao();

        Spinner spinner = findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_task_status = (String) parent.getItemAtPosition(position);
                System.out.println(spinner_task_status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        createTask();
                        return false;
                    }
                });

        Button addTask = findViewById(R.id.newTaskSubmit);
        addTask.setOnClickListener(newTaskCreateListener);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }

    private final View.OnClickListener newTaskCreateListener = new View.OnClickListener() {
        public void onClick(View v) {
            getTeamDetailFromAPIByName(teamName);
        }
    };

    public void onClickRadioButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.team1:
                if (checked)
                    Log.i(TAG, "onClickRadioButton: team 1");
                teamName = "Team 1";
                break;
            case R.id.team2:
                if (checked)
                    Log.i(TAG, "onClickRadioButton: team 2");
                teamName = "Team 2";
                break;
            case R.id.team3:
                if (checked)
                    Log.i(TAG, "onClickRadioButton: team 3");
                teamName = "Team 3";
                break;
        }
    }

    public void getTeamDetailFromAPIByName(String name) {
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains(name)),
                response -> {
                    for (Team teamDetail : response.getData()) {
                        Log.i(TAG, teamDetail.getName());
                        if(teamDetail != null){
                            teamData = teamDetail;
                            handler.sendEmptyMessage(1);
                        }
                    }
                },
                error -> Log.e(TAG, "Query failure", error)
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void createTask() {

        String taskTitle = ((EditText) findViewById(R.id.newTaskName)).getText().toString();
        String taskBody = ((EditText) findViewById(R.id.newTaskBody)).getText().toString();
        String taskStatus = spinner_task_status;

        Task item = Task.builder()
                .title(taskTitle)
                .body(taskBody)
                .team(teamData)
                .status(taskStatus)
                .build();
        MainActivity.saveTaskToAPI(item);
    }
}