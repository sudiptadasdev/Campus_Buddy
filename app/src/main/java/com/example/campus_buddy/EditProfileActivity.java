package com.example.campus_buddy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText inputName, inputBio;
    private Button btnSave;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private DocumentReference userDocRef; // Reference to the user's document

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase Firestore and Auth
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI components
        inputName = findViewById(R.id.input_name);
        inputBio = findViewById(R.id.input_bio);
        btnSave = findViewById(R.id.btn_save);

        // Load current user data
        loadUserProfile();

        // Handle save button click
        btnSave.setOnClickListener(v -> saveBio());
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // First, check in the Student collection
            firestore.collection("Student").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // User found in Student collection
                            userDocRef = documentSnapshot.getReference();
                            String name = documentSnapshot.getString("first_name") + " " + documentSnapshot.getString("last_name");;
                            populateProfile(name, documentSnapshot.getString("bio"));
                        } else {
                            // If not found in Student, check in Organization collection
                            firestore.collection("Organization").document(userId).get()
                                    .addOnSuccessListener(orgSnapshot -> {
                                        if (orgSnapshot.exists()) {
                                            // User found in Organization collection
                                            userDocRef = orgSnapshot.getReference();
                                            populateProfile(orgSnapshot.getString("name"), orgSnapshot.getString("bio"));
                                        } else {
                                            // User not found in either collection
                                            Toast.makeText(this, "User not found in any collection!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error checking Organization collection: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error checking Student collection: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void populateProfile(String name, String bio) {
        inputName.setText(name != null ? name : "");
        inputBio.setText(bio != null ? bio : "");
    }

    private void saveBio() {
        if (userDocRef == null) {
            Toast.makeText(this, "User document reference not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        String updatedBio = inputBio.getText().toString().trim();
        if (updatedBio.isEmpty()) {
            Toast.makeText(this, "Bio cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("bio", updatedBio);

        userDocRef.update(updates)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
