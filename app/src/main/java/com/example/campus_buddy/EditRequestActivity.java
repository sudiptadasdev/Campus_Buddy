package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditRequestActivity extends AppCompatActivity {

    private EditText titleEditText, skillEditText, detailsEditText;
    private Button updateButton;
    private FirebaseFirestore firestore;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        titleEditText = findViewById(R.id.et_edit_title);
        skillEditText = findViewById(R.id.et_edit_skill);
        detailsEditText = findViewById(R.id.et_edit_details);
        updateButton = findViewById(R.id.btn_submit_edit_request);

        // Retrieve extras
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String skill = intent.getStringExtra("skill");
        String details = intent.getStringExtra("details");
        documentId = intent.getStringExtra("documentId");
        System.out.println(documentId);

        // Populate fields
        titleEditText.setText(title);
        skillEditText.setText(skill);
        detailsEditText.setText(details);

        // Set click listener for the update button
        updateButton.setOnClickListener(v -> updateRequest());

    }

    private void updateRequest() {
        // Retrieve the current values from the EditText fields
        String currentTitle = titleEditText.getText().toString().trim();
        String currentSkill = skillEditText.getText().toString().trim();
        String currentDetails = detailsEditText.getText().toString().trim();

        // Check if any field is empty before proceeding
        if (currentTitle.isEmpty() || currentSkill.isEmpty() || currentDetails.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference the specific document by documentId
        DocumentReference requestDocRef = firestore.collection("Request").document(documentId);

        // Update the document fields in Firestore
        requestDocRef.update(
                        "title", currentTitle,
                        "skill", currentSkill,
                        "details", currentDetails
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request updated successfully", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedTitle", currentTitle);
                    resultIntent.putExtra("updatedSkill", currentSkill);
                    resultIntent.putExtra("updatedDetails", currentDetails);
                    resultIntent.putExtra("documentId", documentId);
                    setResult(RESULT_OK, resultIntent);

                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating request", Toast.LENGTH_SHORT).show();
                });
    }


}
