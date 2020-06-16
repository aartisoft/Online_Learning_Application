package com.himanshumauri.learning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshumauri.learning.login.Start;
import com.himanshumauri.learning.menu.ContactUs;
import com.himanshumauri.learning.menu.Profile;
import com.himanshumauri.learning.menu.Settings;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {
    private Toolbar mToolBar;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    private AdView mAdView;
    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        final String pID = getIntent().getStringExtra("pId");

        //--------ToolBar-----------
        mToolBar = findViewById(R.id.homeToolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(pID);


        //----------------------Category Recycler View---------------------------------
        recyclerView = findViewById(R.id.CategoryRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        final DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference("Category").child(pID).child("subCategory");
        fDatabase.keepSynced(true);
        FirebaseRecyclerOptions<ListModel> options = new FirebaseRecyclerOptions.Builder<ListModel>().setQuery(fDatabase, ListModel.class).build();
        categoryAdapter = new CategoryAdapter(options);
        recyclerView.setAdapter(categoryAdapter);


        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(Category.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(Category.this);
        adView.setAdSize(AdSize.FULL_BANNER);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewCategory);
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

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            if (!currentUser.isEmailVerified()) {
                sendToStart();
            } else {
                categoryAdapter.startListening();
            }
        } else {
            sendToStart();
            categoryAdapter.stopListening();
        }
    }

    private void sendToStart() {
        Intent gotoSignIn = new Intent(Category.this, Start.class);
        startActivity(gotoSignIn);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.drawer_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.home:
                Intent homeIntent = new Intent(Category.this, Home.class);
                startActivity(homeIntent);
                break;
            case R.id.profile:
                Intent profileIntent = new Intent(Category.this, Profile.class);
                startActivity(profileIntent);
                break;
            case R.id.appSetting:
                Intent settingIntent = new Intent(Category.this, Settings.class);
                startActivity(settingIntent);
                break;
            case R.id.contactUs:
                Intent contactIntent = new Intent(Category.this, ContactUs.class);
                startActivity(contactIntent);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                break;
        }
        return true;
    }
}
