package com.example.taskmaster;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.predictions.models.LanguageType;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = TaskDetailActivity.class.getSimpleName();
    private Task taskDetails;
    private ImageView imageView;
    private Handler handler;
    public static final String TASK_ID = "taskID";
    private final MediaPlayer mp = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();

        TextView titleTV = findViewById(R.id.taskDetailTitle);
        TextView bodyTV = findViewById(R.id.taskBodyTitle);
        TextView statusTV = findViewById(R.id.task_Detail_State);
        TextView locationTV = findViewById(R.id.task_location);
        Button readBtn = findViewById(R.id.button_read);
        readBtn.setOnClickListener(view ->
                readText());



        imageView = findViewById(R.id.task_img);
        handler = new Handler(Looper.getMainLooper(), msg -> {

            titleTV.setText("Title\n" + taskDetails.getTitle());
            bodyTV.setText("Body\n" + taskDetails.getBody());
            statusTV.setText("Status\n" + taskDetails.getStatus());
            locationTV.setText("Location\n" + taskDetails.getLocationLatitude() + ", " + taskDetails.getLocationLongitude());


            if (taskDetails.getImage() != null) {
                setImage(taskDetails.getImage());
            }
            translateText();
            return true;
        });
        String id = intent.getStringExtra(TASK_ID);
        getDetailsFromAPI(id);
    }



    private void translateText() {
        Amplify.Predictions.translateText(
                taskDetails.getBody(),
                LanguageType.ENGLISH,
                LanguageType.ARABIC,
                result -> {
                    //Toast.makeText(getApplicationContext(), result.getTranslatedText(), Toast.LENGTH_SHORT).show();
                    Log.i("MyAmplifyApp", result.getTranslatedText());
                },
                error -> Log.e("MyAmplifyApp", "Translation failed", error)
        );
    }

    private void setImage(String image) {
        if (image != null) {
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

    private void getDetailsFromAPI(String id) {
        Amplify.API.query(
                ModelQuery.get(Task.class, id),
                response -> {
                    Log.i("MyAmplifyApp", (response.getData()).getTitle());

                    taskDetails = response.getData();
                    Bundle bundle = new Bundle();
                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );
    }

    private void readText(){
        Amplify.Predictions.convertTextToSpeech(
                taskDetails.getBody(),
                result -> playAudio(result.getAudioData()),
                error -> Log.e("MyAmplifyApp", "Conversion failed", error)
        );
    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }

}