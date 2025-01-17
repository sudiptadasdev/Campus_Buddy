package com.example.campus_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class OrganizationSignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private EditText orgName, orgEmail, orgPassword, orgDescription, orgId;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_organization);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        orgName = findViewById(R.id.org_name);
        orgEmail = findViewById(R.id.org_email);
        orgPassword = findViewById(R.id.org_password);
        orgDescription = findViewById(R.id.org_description);
        orgId = findViewById(R.id.org_id);
        signupButton = findViewById(R.id.org_signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = orgName.getText().toString().trim();
                String email = orgEmail.getText().toString().trim();
                String password = orgPassword.getText().toString().trim();
                String description = orgDescription.getText().toString().trim();
                String organizationId = orgId.getText().toString().trim();
                if (name.isEmpty()) {
                    orgName.setError("Organization Name cannot be empty");
                    return;
                }
                if (email.isEmpty()) {
                    orgEmail.setError("Email cannot be empty");
                    return;
                }
                if (password.isEmpty()) {
                    orgPassword.setError("Password cannot be empty");
                    return;
                }
                if (organizationId.isEmpty()) {
                    orgId.setError("Organization ID cannot be empty");
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = auth.getCurrentUser().getUid();

                            Map<String, Object> organization = new HashMap<>();
                            organization.put("name", name);
                            organization.put("email", email);
                            organization.put("description", description);
                            organization.put("organization_id", organizationId);
                            DocumentReference docRef = firestore.collection("Organization").document(userId);
                            docRef.set(organization).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(OrganizationSignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(OrganizationSignUpActivity.this, LoginActivity.class));
                                    } else {
                                        Toast.makeText(OrganizationSignUpActivity.this, "Firestore Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(OrganizationSignUpActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrganizationSignUpActivity.this, LoginActivity.class));
            }
        });
    }
}