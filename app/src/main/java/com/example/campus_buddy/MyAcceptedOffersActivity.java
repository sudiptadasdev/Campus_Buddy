package com.example.campus_buddy;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MyAcceptedOffersActivity extends AppCompatActivity {

    private TextView offersTab, requestsTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccepted_offers);

        // Set up toolbar as action bar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        offersTab = findViewById(R.id.offersTab);
        requestsTab = findViewById(R.id.requestsTab);

        // Load OffersFragment by default
        loadFragment(new MyAcceptedOffersFragment());

        String fragmentToLoad = getIntent().getStringExtra("FRAGMENT_TO_LOAD");

        // Check which fragment to load based on intent
        if ("MyAcceptedOffersFragment".equals(fragmentToLoad)) {
            updateTabSelection(offersTab, requestsTab);
            loadFragment(new MyAcceptedOffersFragment());
        } else if ("MyAcceptedRequestsFragment".equals(fragmentToLoad)) {
            updateTabSelection(requestsTab, offersTab);
            loadFragment(new MyAcceptedRequestsFragment());
        }

        // Set click listeners for the tabs
        offersTab.setOnClickListener(v -> {
            updateTabSelection(offersTab, requestsTab);
            loadFragment(new MyAcceptedOffersFragment());
        });

        requestsTab.setOnClickListener(v -> {
            updateTabSelection(requestsTab, offersTab);
            loadFragment(new MyAcceptedRequestsFragment());
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void updateTabSelection(TextView selectedTab, TextView otherTab) {
        selectedTab.setBackgroundColor(getResources().getColor(R.color.purple_500));
        otherTab.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button press
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
