package com.example.campus_buddy;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private TextView eventsTab, offersTab, requestsTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar as action bar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        eventsTab = findViewById(R.id.eventsTab);
        offersTab = findViewById(R.id.offersTab);
        requestsTab = findViewById(R.id.requestsTab);

        // Load Offers fragment by default
        loadFragment(new EventsFragment());

        // Set click listeners for the footer tabs
        eventsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabSelection(eventsTab, offersTab, requestsTab);
                loadFragment(new EventsFragment());
            }
        });

        offersTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabSelection(offersTab, eventsTab, requestsTab);
                loadFragment(new OffersFragment());
            }
        });

        requestsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabSelection(requestsTab, eventsTab, offersTab);
                loadFragment(new RequestsFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void updateTabSelection(TextView selectedTab, TextView... otherTabs) {
        selectedTab.setBackgroundColor(getResources().getColor(R.color.purple_500));
        for (TextView tab : otherTabs) {
            tab.setBackgroundColor(getResources().getColor(R.color.gray));
        }
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
