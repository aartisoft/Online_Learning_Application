package com.himanshumauri.learning.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import com.himanshumauri.learning.menu.Profile;
import com.himanshumauri.learning.upload.UploadNotes;

public class NotesTitle extends AppCompatActivity {

    private Toolbar mToolBar;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private NotesAdapter adapter;
    private AdView mAdView;
    private FloatingActionButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_title);

        final String type = getIntent().getStringExtra("Type");
        final String pId = getIntent().getStringExtra("pId");
        final  String uID = getIntent().getStringExtra("uID");

        mToolBar = findViewById(R.id.NotesList_toolbar);
        recyclerView = findViewById(R.id.NotesRecyclerView);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Notes List");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();
        String mCurrentUser = User.getUid();
        assert type != null;
        assert pId != null;
        final DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference("Contents").child(type).child(pId);
        fDatabase.keepSynced(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<NotesModel> options = new FirebaseRecyclerOptions.Builder<NotesModel>().setQuery(fDatabase,NotesModel.class).build();
        adapter = new NotesAdapter(options);
        recyclerView.setAdapter(adapter);

        //----------------Adding New Item----------------------------
        button = findViewById(R.id.addItemNotes);
        if (mCurrentUser.equals(uID)){
            button.setVisibility(View.VISIBLE);
        }else {
            button.setVisibility(View.GONE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UploadNotes = new Intent(NotesTitle.this, com.himanshumauri.learning.upload.UploadNotes.class);
                UploadNotes.putExtra("Type",type);
                UploadNotes.putExtra("pId",pId);
                UploadNotes.putExtra("uID",uID);
                startActivity(UploadNotes);
            }
        });

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(NotesTitle.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(NotesTitle.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewnNotesTitleList);
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
