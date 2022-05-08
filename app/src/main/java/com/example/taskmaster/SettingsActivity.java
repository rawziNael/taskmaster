package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();;
    public EditText mUsername;
    public static final String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button saveUsernameButton = findViewById(R.id.usernameSaveButton);
        saveUsernameButton.setOnClickListener(updateUsernameListener);

    }

    private final View.OnClickListener updateUsernameListener = new View.OnClickListener() {
        public void onClick(View v) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = preferences.edit();
            EditText usernameField = findViewById(R.id.usernameInput);
            String username = usernameField.getText().toString();
            editor.putString("username", username);
            editor.apply();
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        }
    };
}