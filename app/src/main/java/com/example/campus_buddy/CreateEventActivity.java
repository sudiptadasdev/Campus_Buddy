package com.example.campus_buddy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;


public class CreateEventActivity extends AppCompatActivity {

    private EditText titleEditText, summaryEditText, locationEditText;
    private TextView dateTextView, timeTextView;
    private Button saveEventButton;
    private Timestamp eventTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        summaryEditText = findViewById(R.id.summaryEditText);
        locationEditText = findViewById(R.id.locationEditText);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        saveEventButton = findViewById(R.id.saveEventButton);

        dateTextView.setOnClickListener(v -> showDatePickerDialog());
        timeTextView.setOnClickListener(v -> showTimePickerDialog());

        saveEventButton.setOnClickListener(v -> saveEvent());
    }

    // Implement showDatePickerDialog(), showTimePickerDialog(), and saveEvent()
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    dateTextView.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                            .format(calendar.getTime()));
                    updateEventTimestamp(calendar);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    timeTextView.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault())
                            .format(calendar.getTime()));
                    updateEventTimestamp(calendar);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    private void updateEventTimestamp(Calendar calendar) {
        eventTimestamp = new Timestamp(calendar.getTime());
    }

    private void saveEvent() {
        String title = titleEditText.getText().toString().trim();
        String summary = summaryEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        if (title.isEmpty() || summary.isEmpty() || location.isEmpty() || eventTimestamp == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an Event object
        Event event = new Event(title, eventTimestamp, summary, location);

        // Save to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Event")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving event", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error adding document", e);
                });
    }
}