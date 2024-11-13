package com.example.campus_buddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
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
        holder.title.setText(event.getTitle());
        holder.summary.setText(event.getDescription());
        holder.date.setText(event.getDateString());
        holder.location.setText(event.getLocation());

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Clicked: " + event.getTitle(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, summary, date, location;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            summary = itemView.findViewById(R.id.summaryTextView);
            date = itemView.findViewById(R.id.dateTextView);
            location = itemView.findViewById(R.id.locationTextView);
        }
    }
}
