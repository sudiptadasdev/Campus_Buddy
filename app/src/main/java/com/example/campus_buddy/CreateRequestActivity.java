package com.example.campus_buddy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateRequestActivity extends AppCompatActivity {

    private EditText etTitle, etDetails, etSkill;
    private Button btnSubmit;
    // private DatabaseReference databaseReference; // Firebase reference (commented out for now)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        // Initialize the views
        etTitle = findViewById(R.id.et_title);
        etDetails = findViewById(R.id.et_details);
        etSkill = findViewById(R.id.et_skill);
        btnSubmit = findViewById(R.id.btn_submit_request);

        // Initialize Firebase Database (commented out for now)
        // FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // databaseReference = firebaseDatabase.getReference("requests");

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
                    // Simulate creating a request (database code can be added later)
                    createRequest(title, details, skill);
                }
            }
        });
    }

    // Method to simulate creating a request (without Firebase for now)
    private void createRequest(String title, String details, String skill) {
        // This is where the database code will go later
        // For now, just show a confirmation message
        Toast.makeText(this, "Request Created: " + title, Toast.LENGTH_SHORT).show();
        // Optionally, finish the activity to go back to the previous screen
        finish();
    }
}
