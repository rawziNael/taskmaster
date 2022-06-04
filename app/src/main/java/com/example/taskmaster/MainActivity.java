package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.Adapter.RecyclerViewAdapter;
import java.util.ArrayList;
import java.util.List;
import com.amplifyframework.datastore.generated.model.Task;

public class MainActivity extends AppCompatActivity {

    public static final String TASK_NAME = "taskName";
    public static final String TASK_TITLE = "taskTitle";
    public static final String TASK_BODY = "taskBody";
    public static final String TASK_STATUS = "taskStatus";
    public static final String TASK_LIST = "TaskList";
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerViewAdapter adapter;
    private Handler handler;
    private Team teamData = null;
    private String teamNameData = null;

    List<Task> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newTask = findViewById(R.id.addTaskButton);
        newTask.setOnClickListener(this.newTask);

        Button allTasks = findViewById(R.id.allTasksButton);
        allTasks.setOnClickListener(this.allTasks);


        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }

//        Team team1 = Team.builder()
//                .name("Team 1")
//                .build();
//        saveTeamToAPI(team1);
//
//        Team team2 = Team.builder()
//                .name("Team 2")
//                .build();
//        saveTeamToAPI(team2);
//
//        Team team3 = Team.builder()
//                .name("Team 3")
//                .build();
//        saveTeamToAPI(team3);

        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        listItemDeleted();
                        return false;
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.List_tasks);


        dataList = new ArrayList<>();

        showTeamTasks();
        adapter = new RecyclerViewAdapter(dataList, new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent goToDetailsIntent = new Intent(getApplicationContext(), RecyclerViewActivity.class);
                goToDetailsIntent.putExtra(TASK_TITLE, dataList.get(position).getTitle());
                goToDetailsIntent.putExtra(TASK_BODY, dataList.get(position).getBody());
                goToDetailsIntent.putExtra(TASK_STATUS, dataList.get(position).getStatus());
                startActivity(goToDetailsIntent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void showTeamTasks(){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = preference.getString("username", "user") + "'s Tasks";
        String teamName = "Your Team Name is: " + preference.getString("teamName", "Choose your team");

        TextView userLabel = findViewById(R.id.userTasksLabel);
        TextView teamNameLabel = findViewById(R.id.teamTasksLabel);
        teamNameData = preference.getString("teamName", teamName);
        userLabel.setText(username);
        teamNameLabel.setText(teamName);
        if (teamNameData!= null){
            getTeamDetailFromAPIByName();
        }else{
            getTaskDataFromAPI();
        }
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

    private void goToSettings(){
            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);
    }

    public  void getTaskDataFromAPI(){
        Amplify.API.query(ModelQuery.list(Task.class),
                response -> {
                    for (Task task : response.getData()) {
                        dataList.add(task);
                        Log.i(TAG, "getFrom api: the Task from api are => " + task.toString());
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e(TAG, "getFrom api: Failed to get Task from api => " + error)
        );
    }

    public static void saveTaskToAPI(Task item){
        System.out.println(item);
        Amplify.API.mutate(ModelMutation.create(item),
                success -> Log.i(TAG, "Saved item to api : " + success.getData().getTitle()),
                error -> Log.e(TAG, "Could not save item to API/dynamodb", error));
    }

    public static void saveTeamToAPI(Team item){
        //System.out.println(item);
        Amplify.API.mutate(ModelMutation.create(item),
                success -> Log.i(TAG, "Saved item to api : " + success.getData().getId()),
                error -> Log.e(TAG, "Could not save item to API/dynamodb", error));
    }

    public void getTeamDetailFromAPIByName() {
        Log.i(TAG, "getTeamDetailFromAPIByName: get team");
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains(teamNameData)),
                response -> {
                    for (Team teamDetail : response.getData()) {
                        Log.i(TAG, teamDetail.toString());
                        if(teamDetail != null) {
                            teamData = teamDetail;
                            dataList.clear();
                            getTaskDataFromAPIByTeam();
                        }
                    }
                },
                error -> Log.e(TAG, "Query failure", error)
        );
    }

    public  void  getTaskDataFromAPIByTeam(){
        Log.i(TAG, "getTaskDataFromAPIByTeam: get task by team");
        Amplify.API.query(ModelQuery.list(Task.class, Task.TEAM.contains(teamData.getId())),
                response -> {
                    for (Task task : response.getData()) {

                        Log.i(TAG, "task-team-id: " + task.getTeam().getId());
                        Log.i(TAG, "team-id: "+ teamData.getId());
                        dataList.add(task);
                        Log.i(TAG, "getFrom api by team: the Task from api are => " + task);
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e(TAG, "getFrom api: Failed to get Task from api => " + error)
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void listItemDeleted() {
        adapter.notifyDataSetChanged();
    }
}