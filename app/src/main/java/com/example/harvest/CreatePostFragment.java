package com.example.harvest;

import static android.app.Activity.RESULT_OK;
import static com.example.harvest.R.id.detailField;
import static com.example.harvest.R.id.imageField;
import static com.example.harvest.R.id.imageView;
import static com.example.harvest.R.id.titleField;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.harvest.modals.IssueModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class CreatePostFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Declare the UI elements as instance variables
    private Button sendIssueBtn;
    private int GALLERY_CODE = 1000;
    private Uri imageUri;
    private EditText titleField, detailField, authorField;
    private ImageView imageField;
//    private Button submitBtn;
//
//    public CreatePostFragment() {
//        // Required empty public constructor
//    }

    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        // Initialize the UI elements
        sendIssueBtn = view.findViewById(R.id.sendIssueBtn);
        titleField = view.findViewById(R.id.titleField);
        detailField = view.findViewById(R.id.detailField);
//        authorField = view.findViewById(R.id.authorField);
        imageField = view.findViewById(R.id.imageField);

        imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_CODE);
            }
        });

        // Set the click listener for the sendIssueBtn
        sendIssueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String id = "1";
                String title = titleField.getText().toString().trim();
                String detail = detailField.getText().toString().trim();
                String author = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String randId = ""+System.currentTimeMillis();

                // Check if the required fields are empty
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(detail) || TextUtils.isEmpty(author)) {
                    Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
// Create a new IssueModal object with the entered details
                    IssueModal issueModal = new IssueModal(randId, title, detail, author, "");


                    // Create a dialog to show the progress
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Posting Issue...");
                    progressDialog.show();

                    // Upload the image if it exists
                    if (imageUri != null) {
                        // Get a reference to the Firebase storage
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReference();

                        // Get a reference to the folder where the image should be stored
                        final StorageReference imageRef = storageReference.child("images/" + imageUri.getLastPathSegment());

                        // Upload the image
                        UploadTask uploadTask = imageRef.putFile(imageUri);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                // Check if the upload was successful
                                if (task.isSuccessful()) {
                                    // Get the download url of the image and set it in the IssueModal object
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            issueModal.setImageUri(uri.toString());

                                            // Call the method to post the issue to the Firebase database
                                            postIssue(issueModal, progressDialog);
                                        }
                                    });
                                } else {
                                    // If the upload was not successful, dismiss the progress dialog and show an error message
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // If there is no image, post the issue to the Firebase database
                        postIssue(issueModal, progressDialog);
                    }
                }
            }
        });

        return view;
    }

    // This method is called when an image is selected from the gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageField.setImageURI(imageUri);
        }
    }

    // This method is used to post the issue to the Firebase database
    private void postIssue(IssueModal issueModal, ProgressDialog progressDialog) {
        // Get a reference to the Firebase database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FirebaseApp.getInstance());
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("issues");

        // Add the issue to the Firebase database
        String issueId = databaseReference.push().getKey();
        databaseReference.child(issueId).setValue(issueModal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // If the issue was added successfully, dismiss the progress dialog and show a success message
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Issue posted successfully", Toast.LENGTH_SHORT).show();

                // Reset the UI elements
                titleField.setText("");
                detailField.setText("");
                imageField.setImageResource(R.drawable.media);
            }
        });
    }

}