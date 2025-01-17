package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditEventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_events);
        getEmail();

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();

        // Set up the adapter
        adapter = new EventAdapter(this, eventList) {
            @Override
            public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                Event event = eventList.get(position);

                // Modify the button text and click listener for editing
                holder.registerButton.setText("Edit Event");
                holder.registerButton.setOnClickListener(v -> openEditEventPage(event));
            }
        };

        recyclerView.setAdapter(adapter);

        fetchEventsByCreatorEmail();
    }

    private void fetchEventsByCreatorEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            firestore.collection("Organization").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            email = documentSnapshot.getString("email");
                            Log.i("Email", "Email fetched: " + email);

                            firestore.collection("Event")
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        if (!querySnapshot.isEmpty()) {
                                            eventList.clear();
                                            for (QueryDocumentSnapshot document : querySnapshot) {
                                                String title = document.getString("name");
                                                String description = document.getString("description");
                                                String location = document.getString("location");
                                                Timestamp time = document.getTimestamp("date");
                                                String creator = document.getString("email");

                                                Log.i("Creator", "Creator fetched: " + creator);

                                                if (creator != null && creator.equals(email)) {
                                                    Log.i("Adding Event", "Adding event: " + title);
                                                    eventList.add(new Event(title, time, description, location, ""));
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(this, "No events found." + " " + email, Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error fetching events: " + e.getMessage());
                                        Toast.makeText(this, "Error fetching events.", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });
        }
    }

    private void openEditEventPage(Event event) {
        Intent intent = new Intent(EditEventActivity.this, CreateEventActivity.class);

        // Pass event details as extras
        intent.putExtra("title", event.getName());
        intent.putExtra("description", event.getDescription());
        intent.putExtra("location", event.getLocation());
        intent.putExtra("time", event.getDateString());
        intent.putExtra("edit", true);

        startActivity(intent);
    }

    private void getEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("Organization").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            email = documentSnapshot.getString("email");
                            Log.i("Email", "Email fetched: " + email);
                        }
                    });
        }
    }
}
