package com.example.campus_buddy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrgHomeActivity extends AppCompatActivity {

    private TextView orgName, orgBio;
    private FirebaseFirestore firestore;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_home);
        firestore = FirebaseFirestore.getInstance();
        orgName = findViewById(R.id.orgName);
        orgBio = findViewById(R.id.orgDesc);
        email = getIntent().getStringExtra("email");
        loadOrgData();
    }

    private void loadOrgData() {
        if (email == null || email.isEmpty()) {
            orgName.setText("Email not provided");
            orgBio.setText("No bio available");
            return;
        }

        firestore.collection("Organization")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String name = documentSnapshot.getString("name");
                        String bio = documentSnapshot.getString("description");

                        orgName.setText(name != null ? "Welcome  " + name : "Org Name");
                        orgBio.setText(bio != null ? bio : "Org description");
                    } else {
                        orgName.setText("Org not found");
                        orgBio.setText("No bio available");
                    }
                }).addOnFailureListener(e -> {
                    orgName.setText("Error loading name");
                    orgBio.setText("Error loading bio");
                    e.printStackTrace();
                });
    }
}
