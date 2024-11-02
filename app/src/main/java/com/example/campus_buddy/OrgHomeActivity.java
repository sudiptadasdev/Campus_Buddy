package com.example.campus_buddy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrgHomeActivity extends AppCompatActivity {

    private TextView orgName, orgBio;
    private FirebaseFirestore firestore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_home);

        // Initialize Firebase and Views
        firestore = FirebaseFirestore.getInstance();
        orgName = findViewById(R.id.orgName);
        orgBio = findViewById(R.id.orgDesc);

        // Assume userId is passed or fetched after login
        userId = getIntent().getStringExtra("USER_ID");

        // Load student data from Firestore
        DocumentReference docRef = firestore.collection("students").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve and display name and bio
                String name = documentSnapshot.getString("name");
                String bio = documentSnapshot.getString("bio");

                orgName.setText(name != null ? name : "Organization Name");
                orgBio.setText(bio != null ? bio : "Organization Description");
            }
        }).addOnFailureListener(e -> {
            orgName.setText("Error loading name");
            orgBio.setText("Error loading bio");
        });
    }
}
