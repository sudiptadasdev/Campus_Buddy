package com.example.campus_buddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyAcceptedOffersAdapter extends RecyclerView.Adapter<MyAcceptedOffersAdapter.OfferViewHolder> {

    private final List<Offers> offerList;

    public MyAcceptedOffersAdapter(List<Offers> offerList) {
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_myacceptedoffer_card, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offers offer = offerList.get(position);
        holder.title.setText(offer.getTitle());
        holder.skill.setText(offer.getSkill());
        holder.details.setText(offer.getDetails());
        holder.completeButton.setOnClickListener(v -> {
            // Reference to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Show an alert dialog to get the rating from the user
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Rate the Offer");

            // Add a rating bar to the alert dialog
            final RatingBar ratingBar = new RatingBar(v.getContext());
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(1.0f);
            builder.setView(ratingBar);

            // Set up the dialog buttons
            builder.setPositiveButton("Submit", (dialog, which) -> {
                float newRating = ratingBar.getRating(); // Get the user's rating

                // Update the status to "completed" in the Offer collection
                DocumentReference offerRef = db.collection("Offer").document(offer.getDocumentId());
                offerRef.update("status", "completed")
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Offer status updated to completed"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error updating offer status", e));

                // Fetch and update the rating in the Student collection
                db.collection("Student")
                        .whereEqualTo("email", offer.getCreatedBy()) // Match email with created_by
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            Log.d("Firestore", "Number of matching students: " + querySnapshot.size() + offer.getCreatedBy());
                            if (!querySnapshot.isEmpty()) {
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    Log.d("Firestore", "Processing student: " + document.getId());
                                    DocumentReference studentRef = document.getReference();

                                    // Fetch the previous rating
                                    Double previousRating = document.getDouble("rating");
                                    Log.d("Firestore", "Previous Rating: " + previousRating);

                                    if (previousRating == null) {
                                        previousRating = 0.0; // Default if no rating exists
                                    }

                                    // Calculate the new average rating
                                    double updatedRating = (previousRating + newRating) / 2;
                                    Log.d("Firestore", "Updated Rating: " + updatedRating);

                                    // Update the rating in the Student collection
                                    studentRef.update("rating", updatedRating)
                                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Rating updated successfully"))
                                            .addOnFailureListener(e -> Log.e("Firestore", "Error updating rating", e));
                                }
                            } else {
                                Log.d("Firestore", "No matching students found.");
                            }
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error fetching student data", e));

            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            // Show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView title, skill, details;
        Button completeButton;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.offerTitleTextView);
            skill = itemView.findViewById(R.id.offerSkillTextView);
            details = itemView.findViewById(R.id.offerDetailsTextView);
            completeButton = itemView.findViewById(R.id.completeOfferButton);
        }
    }
}
