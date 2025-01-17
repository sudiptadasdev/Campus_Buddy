package com.example.campus_buddy;

import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyAcceptedRequestsAdapter extends RecyclerView.Adapter<MyAcceptedRequestsAdapter.RequestViewHolder> {

    private final List<Requests> requestList;

    public MyAcceptedRequestsAdapter(List<Requests> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_acceptedrequests_card, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Requests request = requestList.get(position);
        holder.title.setText(request.getTitle());
        holder.skill.setText(request.getSkill());
        holder.details.setText(request.getDetails());

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView title, skill, details;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.requestTitleTextView);
            skill = itemView.findViewById(R.id.requestSkillTextView);
            details = itemView.findViewById(R.id.requestDetailsTextView);

        }
    }
}
