package com.example.campus_buddy;

import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
public class StudentHomeActivity extends AppCompatActivity {

    private TextView studentName, studentBio;
    private FirebaseFirestore firestore;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_student_home);

        firestore = FirebaseFirestore.getInstance();
        studentName = findViewById(R.id.studentName);
        studentBio = findViewById(R.id.studentBio);

        // Retrieve email from Intent
        email = getIntent().getStringExtra("email");

        // Fetch student data from Firestore
        loadStudentData();
    }

    private void loadStudentData() {
        if (email == null || email.isEmpty()) {
            studentName.setText("Email not provided");
            studentBio.setText("No bio available");
            return;
        }

        DocumentReference docRef = firestore.collection("Student").document("email");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("first_name");
                String bio = documentSnapshot.getString("bio");

                studentName.setText(name != null ? name : "Student Name");
                studentBio.setText(bio != null ? bio : "Student Bio");
            } else {
                studentName.setText("Student not found");
                studentBio.setText("No bio available");
            }
        }).addOnFailureListener(e -> {
            studentName.setText("Error loading name");
            studentBio.setText("Error loading bio");
            e.printStackTrace();
        });
    }
}
