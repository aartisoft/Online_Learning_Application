package com.himanshumauri.learning.menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshumauri.learning.Home;
import com.himanshumauri.learning.R;

public class StudentsList extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private StudentsAdapter adapter;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_list);

        String parent = getIntent().getStringExtra("parent");
        String grand = getIntent().getStringExtra("grand");

        assert grand != null;
        assert parent != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contents").child("MCQ")
                .child("topic").child(grand).child(parent).child("students");

        toolbar = findViewById(R.id.StudentsListToolbar);
        recyclerView = findViewById(R.id.StudentsListRecyclerView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Students");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<StudentsModel> options = new FirebaseRecyclerOptions.Builder<StudentsModel>().setQuery(reference,StudentsModel.class).build();
        adapter = new StudentsAdapter(options);
        recyclerView.setAdapter(adapter);

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(StudentsList.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(StudentsList.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewStudentsList);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
