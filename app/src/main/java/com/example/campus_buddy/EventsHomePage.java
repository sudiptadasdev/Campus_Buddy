package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventsHomePage extends AppCompatActivity {

    private boolean isUserOrganization = false;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_home);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Set up click listeners for the tiles
        LinearLayout createEventTile = findViewById(R.id.create_event_tile);
        LinearLayout editEventTile = findViewById(R.id.edit_event_tile);
        LinearLayout viewEventsTile = findViewById(R.id.view_events_tile);

        isOrganization();

        createEventTile.setOnClickListener(view -> {
            if (isUserOrganization) {
                Intent intent = new Intent(EventsHomePage.this, CreateEventActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(EventsHomePage.this, "You are not an organization", Toast.LENGTH_SHORT).show();
            }
        });

        editEventTile.setOnClickListener(view -> {
            if (isUserOrganization) {
                Intent intent = new Intent(EventsHomePage.this, EditEventActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(EventsHomePage.this, "You are not an organization", Toast.LENGTH_SHORT).show();
            }
        });

        viewEventsTile.setOnClickListener(view -> {
            Intent intent = new Intent(EventsHomePage.this, MainActivity.class);
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

    private void isOrganization() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("Organization").document(userId);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    isUserOrganization = true;
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Error fetching user data", e);
            });
        }
    }
}
