package com.himanshumauri.learning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
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

import java.util.Arrays;
import java.util.Collections;

public class SubCategory extends AppCompatActivity {
    private Toolbar mToolBar;
    private TextView mWelcome;

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    SubCategoryAdapter subCategoryAdapter;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);
        final String pID = getIntent().getStringExtra("pId");
        final String ParentId = getIntent().getStringExtra("ParentKey");

        //--------ToolBar-----------
        mToolBar = findViewById(R.id.homeToolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(ParentId);

        //-------Welcome Text----------
        mWelcome = findViewById(R.id.TitleName);
        mWelcome.setSelected(true);
        String TitleShow = ParentId +">>"+ pID;
        mWelcome.setText(TitleShow);
        //---------Firebase Initialization------------------------
        mAuth = FirebaseAuth.getInstance();
        assert pID != null;
        assert ParentId != null;
        final DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference("Category")
                .child(ParentId).child("subCategory").child(pID);
        fDatabase.keepSynced(true);
        //----------------------Sub Category Recycler View---------------------------------
        recyclerView = findViewById(R.id.SubCategoryRecyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference database = fDatabase.child("Subject");
        FirebaseRecyclerOptions<ListModel> options = new FirebaseRecyclerOptions.Builder<ListModel>().setQuery(database, ListModel.class).build();
        subCategoryAdapter = new SubCategoryAdapter(options);
        recyclerView.setAdapter(subCategoryAdapter);

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(SubCategory.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(SubCategory.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewSubCategory);
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
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            if (!currentUser.isEmailVerified()){
                sendToStart();
            }else{
                subCategoryAdapter.startListening();
            }
        }else{
            sendToStart();
            subCategoryAdapter.stopListening();
        }
    }

    private void sendToStart() {
        Intent gotoSignIn = new Intent(SubCategory.this, Start.class);
        startActivity(gotoSignIn);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.drawer_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.home:
                Intent homeIntent = new Intent(SubCategory.this,Home.class);
                startActivity(homeIntent);
                break;
            case R.id.profile:
                Intent profileIntent = new Intent(SubCategory.this, Profile.class);
                startActivity(profileIntent);
                break;
            case R.id.appSetting:
                Intent settingIntent = new Intent(SubCategory.this, Settings.class);
                startActivity(settingIntent);
                break;
            case R.id.contactUs:
                Intent contactIntent = new Intent(SubCategory.this, ContactUs.class);
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
