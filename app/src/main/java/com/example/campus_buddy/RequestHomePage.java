package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class RequestHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_home);

        // Set up click listeners for the tiles
        LinearLayout createRequestTile = findViewById(R.id.createrequest_tile);
        LinearLayout editRequestTile = findViewById(R.id.editrequest_tile);
        LinearLayout viewAcceptedRequestsTile = findViewById(R.id.viewacceptedrequests_tile);
        LinearLayout viewAllRequestsTile = findViewById(R.id.viewallrequests_tile);

        createRequestTile.setOnClickListener(view -> {
            Intent intent = new Intent(RequestHomePage.this, CreateRequestActivity.class);
            startActivity(intent);
        });

        editRequestTile.setOnClickListener(view -> {
            Intent intent = new Intent(RequestHomePage.this, RequestsFragment.class);
            startActivity(intent);
        });

        viewAcceptedRequestsTile.setOnClickListener(view -> {
            Intent intent = new Intent(RequestHomePage.this, CreateRequestActivity.class);
            startActivity(intent);
        });

        viewAllRequestsTile.setOnClickListener(view -> {
            Intent intent = new Intent(RequestHomePage.this, CreateRequestActivity.class);
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