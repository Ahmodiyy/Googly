package com.example.googly;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.googly.ui.main.SectionsPagerAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Bundle bundle = new Bundle();
    private static MapsFragment mapsFragment = new MapsFragment();

    public static MapsFragment getMapsFragment() {
        return mapsFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // setting an adapter to the autocomplete support fragment
        bundle.putDoubleArray("destLatLong", new double[]{ 0.0, 0.0});
        mapsFragment.setArguments(bundle);
        setAutoSupportFragment();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sectionsPagerAdapter.addFragment(new MapsFragment(), "Map");
        sectionsPagerAdapter.addFragment(new ItemFragment(), "Alarm");

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        // setting  icons to the tabs
        tabs.getTabAt(0).setIcon(R.drawable.ic_baseline_map_24);
        tabs.getTabAt(1).setIcon(R.drawable.ic_baseline_access_alarm_24);


        FloatingActionButton fab = findViewById(R.id.fab);
        // setting an click listener on floating action bar
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    /**
     * set an adapter to the autocomplete support fragment
     */
    private void setAutoSupportFragment() {
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        // Specify the types of place data to return.
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
               LatLng latLng = place.getLatLng();
                Log.v("lat", String.valueOf(latLng.latitude));
                Log.v("lat", String.valueOf(latLng.longitude));
               bundle.putDoubleArray("destLatLong", new double[]{ latLng.latitude, latLng.longitude});
               mapsFragment.setArguments(bundle);

            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MainActivity.this, "retry", Toast.LENGTH_SHORT).show();
            }
        });
    }

}