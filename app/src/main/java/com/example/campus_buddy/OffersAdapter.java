package com.example.campus_buddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Selected Offer: " + offer.getTitle(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView title, skill, details;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.offerTitleTextView);
            skill = itemView.findViewById(R.id.offerSkillTextView);
            details = itemView.findViewById(R.id.offerDetailsTextView);
        }
    }
}
