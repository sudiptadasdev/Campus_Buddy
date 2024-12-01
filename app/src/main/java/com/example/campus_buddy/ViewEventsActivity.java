package com.example.campus_buddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewEventsActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private FloatingActionButton addEventFab;
    private SearchView searchView;
    private TextView startDateTextView, endDateTextView;
    private Date startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events); // Use the correct layout for this activity

        // Initialize Firestore and Auth
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        eventList = new ArrayList<>();
//        adapter = new EventAdapter(requireContext(), eventList);
        recyclerView.setAdapter(adapter);

        // Initialize FAB and set its click listener
        addEventFab = findViewById(R.id.addEventFab);
        addEventFab.setOnClickListener(v -> {
            Intent intent = new Intent(ViewEventsActivity.this, CreateEventActivity.class);
            startActivity(intent);
        });

        setupSearchView();
        setupDateFilters();

        // Check if the user is an organization and show FAB accordingly
        checkIfUserIsOrganization();

        // Fetch and display events
        fetchEvents(null, null, null);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchEvents(query, startDate, endDate);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // Optionally trigger search as the text changes
            }
        });
    }

    private void setupDateFilters() {
        startDateTextView.setOnClickListener(v -> showDatePickerDialog(true));
        endDateTextView.setOnClickListener(v -> showDatePickerDialog(false));
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    Date selectedDate = calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

                    if (isStartDate) {
                        startDate = selectedDate;
                        startDateTextView.setText(sdf.format(startDate));
                    } else {
                        endDate = selectedDate;
                        endDateTextView.setText(sdf.format(endDate));
                    }

                    // Refetch events based on the new date range
                    fetchEvents(searchView.getQuery().toString(), startDate, endDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void checkIfUserIsOrganization() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("Organization").document(userId);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    addEventFab.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Error fetching user data", e);
            });
        }
    }

    private void fetchEvents(String titleQuery, Date start, Date end) {
        CollectionReference eventsRef = firestore.collection("Event");
        Query query = eventsRef;

        // Filter by title if a title query is provided
        if (start != null) {
            query = query.whereGreaterThanOrEqualTo("date", new Timestamp(start));
        }
        if (end != null) {
            query = query.whereLessThanOrEqualTo("date", new Timestamp(end));
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                eventList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("name");
                    String description = document.getString("description");
                    String location = document.getString("location");
                    Timestamp timestamp = document.getTimestamp("date");

                    if (timestamp != null) {
                        Date date = timestamp.toDate();
                        Event event = new Event(title, timestamp, description, location);

                        if (titleQuery == null || title.toLowerCase().contains(titleQuery.toLowerCase())) {
                            eventList.add(event);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.e("Firestore", "Error fetching events", task.getException());
                Toast.makeText(this, "Error fetching events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
