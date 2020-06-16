package com.himanshumauri.learning.show;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshumauri.learning.Category;
import com.himanshumauri.learning.Home;
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.menu.Profile;

public class MCQResult extends AppCompatActivity {
    private TextView Score, Total, UploaderName;
    private Button DoneBtn;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcq_result);

        final String total = String.valueOf(getIntent().getIntExtra("total", 0));
        final String score = String.valueOf(getIntent().getIntExtra("Score", 0));
        final String uploader = getIntent().getStringExtra("uploader");

        Score = findViewById(R.id.Score);
        Total = findViewById(R.id.TotalNo);
        UploaderName = findViewById(R.id.ResultUploaderName);
        DoneBtn = findViewById(R.id.DoneBtn);

        Score.setText(score);
        Total.setText(total);

        //------------------------------Uploader------------------------------
        assert uploader != null;
        DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference("users").child(uploader);

        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                UploaderName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UploaderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MCQResultUploader = new Intent(MCQResult.this, Profile.class);
                MCQResultUploader.putExtra("uploader", uploader);
                startActivity(MCQResultUploader);
            }
        });

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(MCQResult.this, Home.class);
                startActivity(goHome);
                finish();
            }
        });

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(MCQResult.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(MCQResult.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewMCQResult);
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
}
