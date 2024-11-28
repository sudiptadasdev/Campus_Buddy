package com.example.campus_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private RequestsAdapter adapter;
    private List<Requests> requestsList;
    private FloatingActionButton addRequestFab;
    private SearchView searchView;
    private String curUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        // Initialize Firestore and Auth
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.requestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = view.findViewById(R.id.searchRequestView);
        requestsList = new ArrayList<>();
        adapter = new RequestsAdapter(requestsList);
        recyclerView.setAdapter(adapter);

        // Initialize FAB and set its click listener
        addRequestFab = view.findViewById(R.id.addRequestFab);
        addRequestFab.setOnClickListener(v -> {
            // Navigate to CreateOfferActivity to add a new offer
            Intent intent = new Intent(getContext(), CreateRequestActivity.class);
            startActivity(intent);
        });

        // Check if the user is an organization and show FAB accordingly
        checkIfUserIsStudent();

        // Setup search functionality
        setupSearchView();

        // Fetch and display offers
        fetchRequests(null);

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchRequests(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optionally, you can trigger a search as the text changes
                return false;
            }
        });
    }

    private void checkIfUserIsStudent() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("Student").document(userId);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    addRequestFab.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Error fetching user data", e);
            });
        }
    }

    private void getCurrentUser() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("Student").document(userId);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    curUser = documentSnapshot.getString("email");
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Error fetching user data", e);
            });
        }
    }

    private void fetchRequests(String titleQuery) {
        getCurrentUser();
        CollectionReference requestsRef = firestore.collection("Request");
        Query query = requestsRef;

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                requestsList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("title");
                    String skill = document.getString("skill");
                    String details = document.getString("details");
                    String creator = document.getString("created_by");

                    if (creator != null && creator.equals(curUser)) {
                        Log.i("MyFilter", "Skipped title " + title);
                        continue;
                    }

                    Requests request = new Requests(title, skill, details);

                    // Filter by title if a title query is provided
                    if (titleQuery == null || title.toLowerCase().contains(titleQuery.toLowerCase())) {
                        requestsList.add(request);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.e("Firestore", "Error fetching offers", task.getException());
                Toast.makeText(getContext(), "Error fetching offers", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
