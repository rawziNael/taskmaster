package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.login_emailInput);
        EditText passwordInput = findViewById(R.id.login_passwordInput);
        Button loginBtn = findViewById(R.id.login_btn);
        Button signupBtn = findViewById(R.id.login_signupBtn);

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
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
    }

    public void signIn(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                success -> {
                    Log.i(TAG, "signIn: worked " + success.toString());
                    Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
                    goToMain.putExtra("email",email);
                    startActivity(goToMain);
                },
                error -> Log.e(TAG, "signIn: failed" + error.toString()));
    }
}