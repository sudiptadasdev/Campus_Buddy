package com.example.campus_buddy;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private TextView eventsTab, offersTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventsTab = findViewById(R.id.eventsTab);
        offersTab = findViewById(R.id.offersTab);

        // Load Events fragment by default
        loadFragment(new EventsFragment());

        // Set click listeners for the footer tabs
        eventsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsTab.setBackgroundColor(getResources().getColor(R.color.purple_500));
                offersTab.setBackgroundColor(getResources().getColor(R.color.gray));
                loadFragment(new EventsFragment());
            }
        });

        offersTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offersTab.setBackgroundColor(getResources().getColor(R.color.purple_500));
                eventsTab.setBackgroundColor(getResources().getColor(R.color.gray));
                loadFragment(new OffersFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
