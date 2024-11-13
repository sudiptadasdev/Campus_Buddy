package com.example.campus_buddy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventList = new ArrayList<>();
        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        fetchEvents();

        return view;
    }

    private void fetchEvents() {
        CollectionReference eventsRef = firestore.collection("Event");

        eventsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.e("Firestore", "No events found");
                    Toast.makeText(getContext(), "No events available", Toast.LENGTH_SHORT).show();
                    return;
                }

                eventList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("name");
                    String description = document.getString("description");
                    String location = document.getString("location");
                    Timestamp time = document.getTimestamp("date");
                    String date = time != null ? time.toDate().toString() : "01/01/1970";
                    Log.i("Firestore", "Fetched event: " + title + " " + description + " " + location + " " + date);

                    Event event = new Event(title, time, description, location);
                    eventList.add(event);
                    Log.d("Firestore", "Event fetched: " + event.getTitle());
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.e("Firestore", "Error fetching events", task.getException());
                Toast.makeText(getContext(), "Error fetching events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
