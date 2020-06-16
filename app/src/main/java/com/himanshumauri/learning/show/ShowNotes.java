package com.himanshumauri.learning.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.menu.Profile;

public class ShowNotes extends YouTubeBaseActivity {
    private Toolbar toolbar;
    private TextView Uploader,Title,Body;
    private AdView mAdView;
    private Button VideoIntentBtn;

    private static final String TAG = "Notes";
    private YouTubePlayerView playerView;
    private Button playBtn;
    private YouTubePlayer.OnInitializedListener initializedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_notes);

        final String title = getIntent().getStringExtra("title");
        final String video = getIntent().getStringExtra("video");
        final String body = getIntent().getStringExtra("body");
        final  String uploader = getIntent().getStringExtra("uploader");

        Title = findViewById(R.id.showNotesTitle);
        Body = findViewById(R.id.ShowNotesBody);
        Uploader = findViewById(R.id.NotesUploaderName);


        Title.setText(title);
        Body.setText(body);

        assert uploader != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uploader);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                Intent uploaderIntent = new Intent(ShowNotes.this, Profile.class);
                uploaderIntent.putExtra("uploader",uploader);
                startActivity(uploaderIntent);
            }
        });

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(ShowNotes.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(ShowNotes.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewNotesShow);
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

        //-----------------Video Play Section----------------------------

        VideoIntentBtn = findViewById(R.id.VideoIntentBtn);
        LinearLayout VideoContainer = (LinearLayout) findViewById(R.id.VideoContainer);

        if (video != null){
            if (!video.equals("")){
                VideoContainer.setVisibility(View.VISIBLE);
            }else {
                VideoContainer.setVisibility(View.GONE);
            }
        }else {
            VideoContainer.setVisibility(View.GONE);
        }

        playerView = findViewById(R.id.YoutubeNotesVideoPlay);

        initializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG,"onClick: Done Initializing");
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.setManageAudioFocus(true);
                youTubePlayer.loadVideo(video);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,"onClick: Failed to Initializing");
            }
        };

        VideoIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: Initializing YouTube Player");
                playerView.initialize(YouTubeConfig.getApiKey(), initializedListener);
                Log.d(TAG,"onClick: Done Initializing");
            }
        });


    }
}
