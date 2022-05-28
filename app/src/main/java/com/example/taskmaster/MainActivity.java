package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
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
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e("Tutorial", "Could not initialize Amplify", e);
        }

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
        getTaskDataFromAPI();

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

    public  void getTaskDataFromAPI() {
        Amplify.API.query(ModelQuery.list(Task.class),
                response -> {
                    for (Task task : response.getData()) {
                        dataList.add(task);
                        Log.i(TAG, "getFrom api: the Task from api are => " + task.getTitle());
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e(TAG, "getFrom api: Failed to get Task from api => " + error.toString())
        );
    }

    public static void saveTaskToAPI(Task item) {
        Amplify.API.mutate(ModelMutation.create(item),
                success -> Log.i(TAG, "Saved item to api : " + success.getData().getTitle()),
                error -> Log.e(TAG, "Could not save item to API/dynamodb", error));

    }

    @SuppressLint("NotifyDataSetChanged")
    private void listItemDeleted() {
        adapter.notifyDataSetChanged();
    }
}