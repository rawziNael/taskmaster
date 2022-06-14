package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.squareup.picasso.Picasso;

import java.io.File;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = TaskDetailActivity.class.getSimpleName();
    private Task taskDetails;
    private ImageView imageView;
    private Handler handler;
    public static final String TASK_ID = "taskID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();

        TextView titleTV = findViewById(R.id.taskDetailTitle);
        TextView bodyTV = findViewById(R.id.taskBodyTitle);
        TextView statusTV = findViewById(R.id.task_Detail_State);
        TextView locationTV = findViewById(R.id.task_location);
        imageView = findViewById(R.id.task_img);
        handler = new Handler(Looper.getMainLooper() , msg -> {

            titleTV.setText("Title\n" + taskDetails.getTitle());
            bodyTV.setText("Body\n" + taskDetails.getBody());
            statusTV.setText("Status\n" + taskDetails.getStatus());
            locationTV.setText("Location\n" + taskDetails.getLocationLatitude() + ", " + taskDetails.getLocationLongitude());

            if (taskDetails.getImage() != null){
                setImage(taskDetails.getImage());
            }
            return true ;
        });
        String id = intent.getStringExtra(TASK_ID);
        getDetailsFromAPI(id);
    }

    private void setImage(String image) {
        if(image != null) {
            Amplify.Storage.downloadFile(
                    image,
                    new File(getApplicationContext().getFilesDir() + "/" + image + "download.jpg"),
                    result -> {
                        Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                        Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
                        runOnUiThread(() -> {
                            Picasso.get().load(result.getFile().getAbsoluteFile()).into(imageView);
                        });
                    },
                    error -> Log.e(TAG, "Download Failure", error)
            );
        }
    }

    private void getDetailsFromAPI(String id){
        Amplify.API.query(
                ModelQuery.get(Task.class, id),
                response -> {
                    Log.i("MyAmplifyApp", (response.getData()).getTitle()) ;

                    taskDetails = response.getData();
                    Bundle bundle = new Bundle();
                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );
    }
}