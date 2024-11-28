package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomePage extends AppCompatActivity {

    private LinearLayout offersTile, requestsTile,eventsTile, profileTile;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);

        // Initialize Views
        offersTile = findViewById(R.id.tile_offers);
        requestsTile = findViewById(R.id.tile_requests);
        eventsTile = findViewById(R.id.tile_events);
        profileTile = findViewById(R.id.profile_tile);
        logoutButton = findViewById(R.id.logout_button);


        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set click listeners for tiles
        offersTile.setOnClickListener(v -> openOffersActivity());
        requestsTile.setOnClickListener(v -> openRequestsActivity());
        eventsTile.setOnClickListener(v -> openEventsActivity());
        profileTile.setOnClickListener(v -> openProfileActivity());

        // Set click listener for Logout button
        logoutButton.setOnClickListener(v -> logout());

        // Set click listener for Profile button
    }

    // Open Offers Activity
    private void openOffersActivity() {
        Intent intent = new Intent(HomePage.this, OfferHomePage.class);
        startActivity(intent);
    }

    // Open Requests Activity
    private void openRequestsActivity() {
        Intent intent = new Intent(HomePage.this, RequestHomePage.class);
        startActivity(intent);
    }

    // Open Events Activity
    private void openEventsActivity() {
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
    }

    // Open Profile Activity
    private void openProfileActivity() {
        Toast.makeText(HomePage.this, "Navigating to Profile", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomePage.this, ProfileHomeActivity.class);
        startActivity(intent);
    }

    // Logout logic
    private void logout() {
        // Navigate back to Login Activity
        Intent intent = new Intent(HomePage.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close HomePage Activity
    }
}
