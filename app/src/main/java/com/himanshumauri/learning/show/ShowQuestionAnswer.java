package com.himanshumauri.learning.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.menu.Profile;

public class ShowQuestionAnswer extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView Uploader,Question,Answer;
    private Button SaveBtn;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_question_answer);

        String question = getIntent().getStringExtra("question");
        String answer = getIntent().getStringExtra("answer");
        final String uId = getIntent().getStringExtra("ID");

        mToolbar = findViewById(R.id.LongAQ_show_toolbar);
        Uploader = findViewById(R.id.SLAQUploaderName);
        Question = findViewById(R.id.ShowLongQ);
        Answer = findViewById(R.id.ShowLongA);
        //SaveBtn = findViewById(R.id.LongAQSave);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Question & Answer");
        Question.setText(question);
        Answer.setText(answer);

        assert uId != null;
        DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference("users").child(uId);

        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                Uploader.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploader = new Intent(ShowQuestionAnswer.this, Profile.class);
                uploader.putExtra("uploader",uId);
                startActivity(uploader);
            }
        });

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(ShowQuestionAnswer.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(ShowQuestionAnswer.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewAQ);
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
