package com.example.campus_buddy;

import android.app.AlertDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder> {

    private final List<Offers> offerList;

    public OffersAdapter(List<Offers> offerList) {
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer_card, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offers offer = offerList.get(position);
        holder.title.setText(offer.getTitle());
        holder.skill.setText(offer.getSkill());
        holder.details.setText(offer.getDetails());

        // Handle click event for Accept button
        holder.accept_offer.setOnClickListener(v -> {
            Context context = v.getContext(); // Get context from the view

            // Get the current logged-in user's email
            String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (currentUserEmail == null) {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show confirmation dialog
            new AlertDialog.Builder(context)
                    .setTitle("Accept Offer")
                    .setMessage("Do you really want to accept this offer?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        String documentId = offer.getDocumentId();
                        Log.d("Firestore", "Updating offer with document ID: " + documentId);

                        // Update the status of the offer and the accepted_by field in Firestore
                        FirebaseFirestore.getInstance()
                                .collection("Offer")
                                .document(documentId) // Ensure the correct document is updated
                                .update("status", "Accepted", "accepted_by", currentUserEmail) // Add the email of the logged-in user
                                .addOnSuccessListener(aVoid -> {
                                    // Successfully updated offer
                                    Toast.makeText(context, "Offer Accepted: " + offer.getTitle(), Toast.LENGTH_SHORT).show();
                                    Log.d("Firestore", "Offer status updated successfully.");

                                    // Update the offer object in the list and notify adapter
                                    offer.setStatus("Accepted");
                                    offer.setAccepted_by(currentUserEmail); // Set the email in the local object
                                    notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to update the offer
                                    Log.e("Firestore", "Error updating offer: ", e);
                                    Toast.makeText(context, "Failed to accept offer. Please try again.", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView title, skill, details;
        Button accept_offer;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.offerTitleTextView);
            skill = itemView.findViewById(R.id.offerSkillTextView);
            details = itemView.findViewById(R.id.offerDetailsTextView);
            accept_offer = itemView.findViewById(R.id.acceptOfferButton);
        }
    }
}
