package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harvest.modals.CommentModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentActivity extends AppCompatActivity {
    EditText commentField;
    String issue_id;
    ImageView sendImgBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentField = findViewById(R.id.editCmtInput);
        sendImgBtn = findViewById(R.id.sendCmtBtn);
        issue_id = getIntent().getStringExtra("issue_id");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("issue_comments");

        sendImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentField.getText().toString();
                CommentModal commentModal = new CommentModal(issue_id, comment);
                databaseReference.push().setValue(commentModal).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            commentField.setText("");
                        }else {
                            commentField.setText("");
                        }
                    }
                });
            }
        });
    }
}