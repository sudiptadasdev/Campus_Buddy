package com.example.campus_buddy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleEditText, summaryEditText, locationEditText;
    private TextView dateTextView, timeTextView;
    private Button saveEventButton;
    private Timestamp eventTimestamp;
    private Calendar eventCalendar;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getEmail();

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        summaryEditText = findViewById(R.id.summaryEditText);
        locationEditText = findViewById(R.id.locationEditText);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        saveEventButton = findViewById(R.id.saveEventButton);

        // Initialize the Calendar instance
        eventCalendar = Calendar.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String summary = intent.getStringExtra("description");
            String location = intent.getStringExtra("location");
            String dateString = intent.getStringExtra("time");

            Log.i("Edit Event Intent", "Title fetched: " + title);
            Log.i("Edit Event Intent", "Summary fetched: " + summary);
            Log.i("Edit Event Intent", "Location fetched: " + location);
            Log.i("Edit Event Intent", "Date fetched: " + dateString);

            titleEditText.setText(title);
            summaryEditText.setText(summary);
            locationEditText.setText(location);
            dateTextView.setText(dateString);
        }

        dateTextView.setOnClickListener(v -> showDatePickerDialog());
        timeTextView.setOnClickListener(v -> showTimePickerDialog());
        saveEventButton.setOnClickListener(v -> saveEvent());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    // Update the eventCalendar with the selected date
                    eventCalendar.set(Calendar.YEAR, year);
                    eventCalendar.set(Calendar.MONTH, month);
                    eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateTextView.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                            .format(eventCalendar.getTime()));
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
                    // Update the eventCalendar with the selected time
                    eventCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    eventCalendar.set(Calendar.MINUTE, minute);
                    timeTextView.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault())
                            .format(eventCalendar.getTime()));

                    // Update the eventTimestamp with the selected date and time
                    eventTimestamp = new Timestamp(eventCalendar.getTime());
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    private void saveEvent() {
        String title = titleEditText.getText().toString().trim();
        String summary = summaryEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        if (title.isEmpty() || summary.isEmpty() || location.isEmpty() || eventTimestamp == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, eventTimestamp, summary, location, email);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Event").add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving event", Toast.LENGTH_SHORT).show();
                });
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
