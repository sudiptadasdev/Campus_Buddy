package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class OfferHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back navigation in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Set up click listeners for the tiles
        LinearLayout createRequestTile = findViewById(R.id.createoffer_tile);
        LinearLayout editRequestTile = findViewById(R.id.editoffer_tile);
        LinearLayout viewAcceptedRequestsTile = findViewById(R.id.viewacceptedoffers_tile);
        LinearLayout viewAllRequestsTile = findViewById(R.id.viewalloffers_tile);

        createRequestTile.setOnClickListener(view -> {
            Intent intent = new Intent(OfferHomePage.this, CreateOfferActivity.class);
            startActivity(intent);
        });

        editRequestTile.setOnClickListener(view -> {
            Intent intent = new Intent(OfferHomePage.this, MyOffersActivity.class);
            intent.putExtra("FRAGMENT_TO_LOAD", "MyOffersFragment");
            startActivity(intent);
        });

        viewAcceptedRequestsTile.setOnClickListener(view -> {
            Intent intent = new Intent(OfferHomePage.this,MyAcceptedOffersActivity.class);
            intent.putExtra("FRAGMENT_TO_LOAD", "MyAcceptedOffersFragment");
            startActivity(intent);
        });

        viewAllRequestsTile.setOnClickListener(view -> {
            Intent intent = new Intent(OfferHomePage.this, MainActivity.class);
            intent.putExtra("FRAGMENT_TO_LOAD", "OffersFragment");
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