package com.example.campus_buddy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateRequestActivity extends AppCompatActivity {

    private EditText etTitle, etDetails, etSkill;
    private Button btnSubmit;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        // Initialize Firebase Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize the views
        etTitle = findViewById(R.id.et_title);
        etDetails = findViewById(R.id.et_details);
        etSkill = findViewById(R.id.et_skill);
        btnSubmit = findViewById(R.id.btn_submit_request);

        // Set up the button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String details = etDetails.getText().toString().trim();
                String skill = etSkill.getText().toString().trim();

                // Validate the input fields
                if (title.isEmpty() || details.isEmpty() || skill.isEmpty()) {
                    Toast.makeText(CreateRequestActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create the request in Firestore
                    createRequest(title, details, skill);
                }
            }
        });
    }

    // Method to create a request and store it in Firestore
    private void createRequest(String title, String details, String skill) {
        // Get the email of the logged-in user
        String createdBy = firebaseAuth.getCurrentUser() != null
                ? firebaseAuth.getCurrentUser().getEmail()
                : "Anonymous";

        // Reference to the "requests" collection in Firestore
        CollectionReference requestsRef = firestore.collection("Request");

        // Generate a new request ID (auto-increment simulation)
        requestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int requestId = task.getResult().size() + 2; // Auto-increment starting from 2

                // Create a map of request data
                Map<String, Object> requestData = new HashMap<>();
                requestData.put("created_by", createdBy);
                requestData.put("details", details);
                requestData.put("request_id", requestId);
                requestData.put("skill", skill);
                requestData.put("status", "pending");
                requestData.put("title", title);
                requestData.put("accepted_by","");

                // Add the request to Firestore
                requestsRef.add(requestData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Request Created Successfully!", Toast.LENGTH_SHORT).show();
                            finish(); // Optionally, close the activity
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to create request: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Failed to fetch requests: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
