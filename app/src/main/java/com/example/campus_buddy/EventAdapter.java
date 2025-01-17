package com.example.campus_buddy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private final List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getName());
        holder.summary.setText(event.getDescription());
        holder.date.setText(event.getDateString());
        holder.location.setText(event.getLocation());

        holder.registerButton.setOnClickListener(v ->
                registerForEvent(event)
        );
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private void registerForEvent(Event event) {
        String eventTitle = event.getName();
        getUserEmail(email -> {
            if (email.equals("null")) {
                Toast.makeText(this.context, "User not found", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                firestore.collection("Student")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentReference doc = queryDocumentSnapshots.getDocuments().get(0).getReference();

                                doc.get().addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        List<String> rsvpedEvents = (List<String>) documentSnapshot.get("rsvped_events");

                                        if (rsvpedEvents == null) {
                                            rsvpedEvents = new ArrayList<>();
                                            rsvpedEvents.add(eventTitle);
                                        }
                                        else{
                                            rsvpedEvents.add(eventTitle);
                                        }

                                        doc.update("rsvped_events", rsvpedEvents);
                                        Toast.makeText(this.context, "Event registered successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }
        });
    }

    private void getUserEmail(FirestoreCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String id = user.getUid();
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("Student").document(id);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String email = documentSnapshot.getString("email");
                    callback.onCallback(email);
                } else {
                    Log.e("Firestore", "User document does not exist");
                    callback.onCallback("null");
                }
            });
        }
    }

    public interface FirestoreCallback {
        void onCallback(String email);
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, summary, date, location;
        Button registerButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitleTextView);
            summary = itemView.findViewById(R.id.eventDescriptionTextView);
            date = itemView.findViewById(R.id.eventDateTextView);
            location = itemView.findViewById(R.id.eventLocationTextView);
            registerButton = itemView.findViewById(R.id.registerEventButton);
        }
    }
}
