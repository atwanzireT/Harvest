package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harvest.adapters.CommentAdaptor;
import com.example.harvest.modals.CommentModal;
import com.example.harvest.modals.IssueModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    EditText commentField;
    CommentAdaptor commentAdaptor;
    String issue_id;
    String author;
    ImageView sendImgBtn;
    RecyclerView comment_rv;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ArrayList<CommentModal> comments;
    TextView messageTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        comments = new ArrayList<>();
        commentAdaptor = new CommentAdaptor(this, comments);
        commentField = findViewById(R.id.editCmtInput);
        sendImgBtn = findViewById(R.id.sendCmtBtn);
        author = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        issue_id = getIntent().getStringExtra("issue_id");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("issue_comments");
        comment_rv = findViewById(R.id.comment_rv);
        comment_rv.setHasFixedSize(true);
        comment_rv.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        messageTxt = findViewById(R.id.idTVMsg);
//        messageTxt.setText(issue_id.toString());

        getComments(issue_id);

        sendImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentField.getText().toString();
                CommentModal commentModal = new CommentModal(issue_id, comment, author);
                databaseReference.push().setValue(commentModal).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            commentField.setText("");
                        } else {
                            commentField.setText("");
                        }
                    }
                });
            }
        });
    }

    private void getComments(String str) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("issue_comments");
        Query query = ref.orderByChild("issueID").equalTo(str);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        comments.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            CommentModal commentModal = dataSnapshot.getValue(CommentModal.class);
                            comments.add(commentModal);
                        }
                        commentAdaptor.notifyDataSetChanged();
                        comment_rv.setAdapter(commentAdaptor);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}