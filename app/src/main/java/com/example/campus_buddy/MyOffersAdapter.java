package com.example.campus_buddy;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyOffersAdapter extends RecyclerView.Adapter<MyOffersAdapter.OfferViewHolder> {

    private final List<Offers> offerList;

    public MyOffersAdapter(List<Offers> offerList) {
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_myoffer_card, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offers offer = offerList.get(position);
        holder.title.setText(offer.getTitle());
        holder.skill.setText(offer.getSkill());
        holder.details.setText(offer.getDetails());

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditOfferActivity.class);
            intent.putExtra("title", offer.getTitle());
            intent.putExtra("skill", offer.getSkill());
            intent.putExtra("details", offer.getDetails());
            intent.putExtra("documentId", offer.getDocumentId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView title, skill, details;
        Button editButton;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.offerTitleTextView);
            skill = itemView.findViewById(R.id.offerSkillTextView);
            details = itemView.findViewById(R.id.offerDetailsTextView);
            editButton = itemView.findViewById(R.id.editOfferButton);
        }
    }
}
