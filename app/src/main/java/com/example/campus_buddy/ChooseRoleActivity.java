package com.example.campus_buddy;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoleActivity extends AppCompatActivity {

    private Button organizationButton, userButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role); // Set layout for choosing roles

        // Initialize buttons from layout
        organizationButton = findViewById(R.id.organizationButton);
        userButton = findViewById(R.id.userButton);
        organizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent orgIntent = new Intent(ChooseRoleActivity.this, OrganizationSignUpActivity.class);
                startActivity(orgIntent);
            }
        });

        // Set listener for user signup route
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(ChooseRoleActivity.this, SignUpActivity.class);
                startActivity(userIntent);
            }
        });
    }
}
