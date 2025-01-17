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

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private final List<Requests> requestList;

    public RequestsAdapter(List<Requests> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request_card, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Requests request = requestList.get(position);
        holder.title.setText(request.getTitle());
        holder.skill.setText(request.getSkill());
        holder.details.setText(request.getDetails());

        holder.accept_request.setOnClickListener(v -> {
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
                    .setMessage("Do you really want to accept this request?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        String documentId = request.getDocumentId();
                        Log.d("Firestore", "Updating offer with document ID: " + documentId);

                        // Update the status of the offer and the accepted_by field in Firestore
                        FirebaseFirestore.getInstance()
                                .collection("Request")
                                .document(documentId) // Ensure the correct document is updated
                                .update("status", "Accepted", "accepted_by", currentUserEmail) // Add the email of the logged-in user
                                .addOnSuccessListener(aVoid -> {
                                    // Successfully updated offer
                                    Toast.makeText(context, "Request Accepted: " + request.getTitle(), Toast.LENGTH_SHORT).show();
                                    Log.d("Firestore", "Request status updated successfully.");

                                    // Update the offer object in the list and notify adapter
                                    request.setStatus("Accepted");
                                    request.setAccepted_by(currentUserEmail); // Set the email in the local object
                                    notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to update the offer
                                    Log.e("Firestore", "Error updating request: ", e);
                                    Toast.makeText(context, "Failed to accept request. Please try again.", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView title, skill, details;
        Button accept_request;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.requestTitleTextView);
            skill = itemView.findViewById(R.id.requestSkillTextView);
            details = itemView.findViewById(R.id.requestDetailsTextView);
            accept_request = itemView.findViewById(R.id.acceptRequestButton);
        }
    }
}
