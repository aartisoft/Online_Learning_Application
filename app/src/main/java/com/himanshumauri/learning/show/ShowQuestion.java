package com.himanshumauri.learning.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshumauri.learning.R;

public class ShowQuestion extends AppCompatActivity {
    private Toolbar mToolBar;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private AQListViewAdapter listViewAdapter;
    private AdView mAdView;
    private FloatingActionButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_short_aq);

        final String type = getIntent().getStringExtra("Type");
        final String pId = getIntent().getStringExtra("pId");
        final  String uID = getIntent().getStringExtra("uID");

        mToolBar = findViewById(R.id.ShortAQ_toolbar);
        recyclerView = findViewById(R.id.SAQRecyclerView);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Questions");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();
        assert User != null;
        String mCurrentUser = User.getUid();
        assert type != null;
        assert pId != null;
        final DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference("Contents").child(type).child(pId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<AQModel> options = new FirebaseRecyclerOptions.Builder<AQModel>().setQuery(fDatabase,AQModel.class).build();
        listViewAdapter = new AQListViewAdapter(options);
        recyclerView.setAdapter(listViewAdapter);

        //----------------Adding New Item----------------------------
        button = findViewById(R.id.addItemQA);
        if (mCurrentUser.equals(uID)){
            button.setVisibility(View.VISIBLE);
        }else {
            button.setVisibility(View.GONE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UploadNotes = new Intent(ShowQuestion.this, com.himanshumauri.learning.upload.UploadNotes.class);
                UploadNotes.putExtra("Type",type);
                UploadNotes.putExtra("pId",pId);
                UploadNotes.putExtra("uID",uID);
                startActivity(UploadNotes);
            }
        });

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(ShowQuestion.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(ShowQuestion.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewnShowQuestionList);
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
        listViewAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listViewAdapter.stopListening();
    }
}
