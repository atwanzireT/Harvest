package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(getApplicationContext(), "Already Signed in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    public void handleSignin(View view) {
        EditText emailField, passwordField;
        FirebaseAuth mAuth;
        emailField = findViewById(R.id.loginemailField);
        passwordField = findViewById(R.id.loginpasswordField);
        mAuth = FirebaseAuth.getInstance();


        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "All Fields Must be Filled .", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        FirebaseDatabase.getInstance().getReference("user");
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        loginIntent.putExtra("email", mAuth.getCurrentUser().getEmail());
                        loginIntent.putExtra("uid", mAuth.getCurrentUser().getUid());
                        startActivity(loginIntent);
                    } else {
                        emailField.setText("");
                        passwordField.setText("");
                        Toast.makeText(getApplicationContext(), "Process Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void switchToSignUp(View view) {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
}