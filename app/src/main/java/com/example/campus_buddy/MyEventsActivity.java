package com.example.campus_buddy;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    private List<String> eventList; // List of event names
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        // Initialize Firebase and UI components
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);

        eventList = new ArrayList<>();
        adapter = new EventListAdapter(eventList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch the RSVPed events
        fetchRSVPedEvents();
    }

    private void fetchRSVPedEvents() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            firestore.collection("Student").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            List<String> rsvpedEvents = (List<String>) documentSnapshot.get("rsvped_events");
                            if (rsvpedEvents != null && !rsvpedEvents.isEmpty()) {
                                eventList.clear();
                                eventList.addAll(rsvpedEvents);
                                adapter.notifyDataSetChanged(); // Update the RecyclerView
                            } else {
                                Toast.makeText(this, "No RSVPed events found!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("Firestore", "User document does not exist.");
                            Toast.makeText(this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error fetching RSVPed events", e);
                        Toast.makeText(this, "Error fetching RSVPed events.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }
}
