package com.example.taskmaster;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class AddTask extends AppCompatActivity {

    private static final String TAG = "AddTask";
    private static final int REQUEST_CODE = 123;
    private String spinner_task_status = null;
    private String teamName = null;
    private Team teamData;
    private Handler handler;
    private String imageUrl = "";
    private Button uploadButton;
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocationData;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

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

        Intent intent = getIntent();
        String intentType = intent.getType();

        if (intentType != null && intentType.startsWith("image/")) {
            uploadSharedImage(intent);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestUserPermission();

        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        createTask();
                        return false;
                    }
                });

        uploadButton = findViewById(R.id.upload_btn);
        uploadButton.setOnClickListener(v1 -> uploadImage());

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
                teamName = "Team 1";
                break;
            case R.id.team2:
                teamName = "Team 2";
                break;
            case R.id.team3:
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
                        if (teamDetail != null) {
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
                .status(taskStatus)
                .teamTasksId(teamData.getId())
                .image(imageUrl)
                .locationLongitude(userLocationData.getLongitude())
                .locationLatitude(userLocationData.getLatitude())
                .build();
        MainActivity.saveTaskToAPI(item);
        finish();
    }

    public void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {

            Log.e(TAG, "onActivityResult: Error getting image from device");
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE:
                Uri imagePath = data.getData();
                Log.i(TAG, "onActivityResult: the uri is => " + imagePath);
                uploadImageAPI(imagePath);
                return;
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void uploadSharedImage(Intent intent) {
        Uri imagePath = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imagePath != null) {
            uploadImageAPI(imagePath);
        }
    }

    private void uploadImageAPI(Uri imageUri) {
        String imageName = getRandomString(5) + ".jpg";
        Bitmap bitmap;
        try {
            bitmap = getBitmapFromUri(imageUri);
            File file = new File(getApplicationContext().getFilesDir(), imageName);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Amplify.Storage.uploadFile(
                    imageName,
                    file,
                    result -> {
                        Log.i(TAG, "Successfully uploaded: " + result.getKey());
                        imageUrl = result.getKey();
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        uploadButton.setEnabled(false);
                    },
                    storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Generate random string
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestUserPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                getUserLocation();
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                getUserLocation();
                            } else {
                                // No location access granted.
                            }
                        }
                );

        // ...

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            userLocationData = location;
                        }
                    }
                });
    }

}