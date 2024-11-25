package com.example.campus_buddy;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditRequestActivity extends AppCompatActivity {

    private EditText titleEditText, skillEditText, detailsEditText;
    private Button saveButton;

    private FirebaseFirestore firestore;
    private String requestId; // ID of the request to edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Bind views
        titleEditText = findViewById(R.id.editTitle);
        skillEditText = findViewById(R.id.editSkill);
        detailsEditText = findViewById(R.id.editDetails);
        saveButton = findViewById(R.id.saveButton);

        // Get the request ID passed via intent
        requestId = getIntent().getStringExtra("REQUEST_ID");
        loadRequestDetails();

        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void loadRequestDetails() {
        if (requestId == null) {
            Toast.makeText(this, "Error: No request ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch the request data from Firestore
        DocumentReference requestRef = firestore.collection("Request").document(requestId);
        requestRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                titleEditText.setText(documentSnapshot.getString("title"));
                skillEditText.setText(documentSnapshot.getString("skill"));
                detailsEditText.setText(documentSnapshot.getString("details"));
            } else {
                Toast.makeText(this, "Request not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error loading request details", e);
            Toast.makeText(this, "Error loading request details", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void saveChanges() {
        String title = titleEditText.getText().toString().trim();
        String skill = skillEditText.getText().toString().trim();
        String details = detailsEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(skill) || TextUtils.isEmpty(details)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the request in Firestore
        DocumentReference requestRef = firestore.collection("Request").document(requestId);
        requestRef.update("title", title, "skill", skill, "details", details)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error updating request", e);
                    Toast.makeText(this, "Error updating request", Toast.LENGTH_SHORT).show();
                });
    }
}
