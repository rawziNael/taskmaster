package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText usernameInput;
    EditText passwordInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.login_emailInput);
        passwordInput = findViewById(R.id.login_passwordInput);
        Button loginBtn = findViewById(R.id.login_btn);
        Button signupBtn = findViewById(R.id.login_signupBtn);

        loginBtn.setOnClickListener(v -> {
            String email = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()){
                signIn(email,password);
            }else {
                Toast.makeText(LoginActivity.this, "Please insert your info.??", Toast.LENGTH_SHORT).show();
            }
        });

        signupBtn.setOnClickListener(v -> {
            Intent goToSignUp = new Intent(LoginActivity.this,SignupActivity.class);
            startActivity(goToSignUp);
        });

        fillData();
    }

    public void signIn(String username, String password) {
        Amplify.Auth.signIn(
                username,
                password,
                success -> {
                    Log.i(TAG, "signIn: worked " + success.toString());
                    Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
                    goToMain.putExtra("email",username);
                    startActivity(goToMain);
                },
                error -> Log.e(TAG, "signIn: failed" + error.toString()));
    }

    private void fillData(){
        usernameInput.setText("rawzinael");
        passwordInput.setText("Rawzin993$");
    }
}