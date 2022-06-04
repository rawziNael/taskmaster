package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private String teamName;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

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
            editor.putString("teamName", teamName);
            editor.apply();
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        }
    };

    public void onClickChooseYourTeam(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {

            case R.id.chooseTeam1:
                if (checked)
                    Log.i(TAG, "onClickRadioButton: team 1");
                teamName = "Team 1";
                break;
            case R.id.chooseTeam2:
                if (checked)
                    Log.i(TAG, "onClickRadioButton: team 2");
                teamName = "Team 2";
                break;
            case R.id.chooseTeam3:
                if (checked)
                    Log.i(TAG, "onClickRadioButton: team 3");
                teamName = "Team 3";
                break;
        }
    }
}