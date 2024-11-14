package com.example.campus_buddy;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
public class StudentHomeActivity extends AppCompatActivity {

    private TextView studentName, studentBio;
    private FirebaseFirestore firestore;
    private String email;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_student_home);

        firestore = FirebaseFirestore.getInstance();
        studentName = findViewById(R.id.studentName);
        studentBio = findViewById(R.id.studentBio);
        email = getIntent().getStringExtra("email");

        editProfileButton = findViewById(R.id.editProfileButton);

        loadStudentData();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomeActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loadStudentData() {
        if (email == null || email.isEmpty()) {
            studentName.setText("Email not provided");
            studentBio.setText("No bio available");
            return;
        }

        firestore.collection("Student")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String name = documentSnapshot.getString("first_name");
                        String bio = documentSnapshot.getString("bio");

                        studentName.setText(name != null ? "Welcome  " +name : "Student Name");
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
