package com.example.campus_buddy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateOfferActivity extends AppCompatActivity {

    private EditText etTitle, etDetails, etSkill;
    private Button btnSubmit;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        // Set up the toolbar with a back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Offer");
        }

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
                    Toast.makeText(CreateOfferActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create the request in Firestore
                    createOffer(title, details, skill);
                }
            }
        });
    }

    // Method to create a request and store it in Firestore
    private void createOffer(String title, String details, String skill) {
        // Get the email of the logged-in user
        String createdBy = firebaseAuth.getCurrentUser() != null
                ? firebaseAuth.getCurrentUser().getEmail()
                : "Anonymous";

        // Reference to the "offers" collection in Firestore
        CollectionReference offersRef = firestore.collection("Offer");

        // Generate a new request ID (auto-increment simulation)
        offersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int offerId = task.getResult().size() + 2; // Auto-increment starting from 2

                // Create a map of offer data
                Map<String, Object> offerData = new HashMap<>();
                offerData.put("created_by", createdBy);
                offerData.put("details", details);
                offerData.put("offer_id", offerId);
                offerData.put("skill", skill);
                offerData.put("status", "pending");
                offerData.put("title", title);

                // Add the offer to Firestore
                offersRef.add(offerData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Offer Created Successfully!", Toast.LENGTH_SHORT).show();
                            finish(); // Optionally, close the activity
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to create offer: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Failed to fetch offers: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle back button click in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
