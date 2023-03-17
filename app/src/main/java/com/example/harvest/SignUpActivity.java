package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.harvest.modals.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(getApplicationContext(), "Already Signed in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        }
    }

    public void handleSignup(View view) {
        EditText emailField, passwordField, usernameField;
        FirebaseAuth mAuth;

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        usernameField = findViewById(R.id.usernameField);

        String username = usernameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
            Toast.makeText(SignUpActivity.this, "All Fields Must be filled .", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(username, email, ""));
                                emailField.setText("");
                                passwordField.setText("");
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            }
                            else{
                                emailField.setText("");
                                passwordField.setText("");
                                Toast.makeText(getApplicationContext(), "Process Error", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    public void switchToLogin(View view) {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }

}