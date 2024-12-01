package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_home);

        // Set up click listeners for the tiles
        LinearLayout myOffersTile = findViewById(R.id.my_offers_tile);
        LinearLayout myRequestsTile = findViewById(R.id.my_requests_tile);
        LinearLayout editProfileTile = findViewById(R.id.edit_profile_tile);
        LinearLayout myEventsTile = findViewById(R.id.my_events_tile); // New My Events tile

        myOffersTile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileHomeActivity.this, MyOffersActivity.class);
            startActivity(intent);
        });

        myRequestsTile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileHomeActivity.this, MyRequestsActivity.class);
            startActivity(intent);
        });

        editProfileTile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileHomeActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        myEventsTile.setOnClickListener(view -> { // Add click listener for My Events
            Intent intent = new Intent(ProfileHomeActivity.this, MyEventsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
