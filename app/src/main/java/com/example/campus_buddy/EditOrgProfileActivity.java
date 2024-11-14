package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditOrgProfileActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, bioEditText;
    private Button saveButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_org_profile);

        firstNameEditText = findViewById(R.id.editTextFirstName);
        bioEditText = findViewById(R.id.editTextBio);
        saveButton = findViewById(R.id.buttonSave);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });


    }



    private void saveProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String bio = bioEditText.getText().toString().trim();
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(bio)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }



        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("name", firstName);
        userProfile.put("description", bio);

        DocumentReference userRef = db.collection("Organization").document(currentUser.getUid());
        userRef.update(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditOrgProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditOrgProfileActivity.this, OrgHomeActivity.class);
                    intent.putExtra("email", currentUser.getEmail());
                    startActivity(intent);
                } else {
                    Toast.makeText(EditOrgProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

