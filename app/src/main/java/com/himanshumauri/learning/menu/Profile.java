package com.himanshumauri.learning.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshumauri.learning.Home;
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.login.Start;

import java.util.Objects;

public class Profile extends AppCompatActivity {
    private ImageView ProfileImage;
    private TextView UName,UEmail,UPhone,UStatus;
    private Button UpdateProfile,PerformanceBtn;
    private LinearLayout layout;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        String UserID = getIntent().getStringExtra("uploader");

        ProfileImage = findViewById(R.id.UserProfileIv);
        UName = findViewById(R.id.UserNameTv);
        UStatus = findViewById(R.id.UserStatusTv);
        UPhone = findViewById(R.id.UserPhoneTv);
        UEmail = findViewById(R.id.UserEmailTv);
        UpdateProfile = findViewById(R.id.UpdateProfile);
        layout = findViewById(R.id.BtnContainer);
        PerformanceBtn = findViewById(R.id.PerformanceBtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        final String mCurrentUser = mUser.getUid();

        if (UserID != null && !UserID.equals(mCurrentUser)){
            reference = FirebaseDatabase.getInstance().getReference("users").child(UserID);
        }else {
            reference = FirebaseDatabase.getInstance().getReference("users").child(mCurrentUser);
        }

        if (UserID != null && !UserID.equals(mCurrentUser)){
            UpdateProfile.setVisibility(View.GONE);
            PerformanceBtn.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }else {
            layout.setVisibility(View.VISIBLE);
            UpdateProfile.setText("Update Profile");
            UpdateProfile.setVisibility(View.VISIBLE);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                final String phone = Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString();
                final String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                final String status = Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString();
                final String teacher = Objects.requireNonNull(dataSnapshot.child("teacher").getValue()).toString();
                final String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                if (!image.equals("no_image")){
                    Glide.with(Profile.this).load(image).fitCenter().placeholder(R.drawable.ic_image_holder).into(ProfileImage);
                }
                UName.setText(name);
                UStatus.setText(status);
                UPhone.setText(phone);
                UEmail.setText(email);

                if (teacher.equals("true")){
                    PerformanceBtn.setText("MCQ Result");
                    PerformanceBtn.setVisibility(View.VISIBLE);
                }else {
                    PerformanceBtn.setVisibility(View.GONE);
                }

                UpdateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent UpdateProfile = new Intent(Profile.this, com.himanshumauri.learning.menu.UpdateProfile.class);
                        UpdateProfile.putExtra("name",name);
                        UpdateProfile.putExtra("phone",phone);
                        UpdateProfile.putExtra("email",email);
                        UpdateProfile.putExtra("status",status);
                        UpdateProfile.putExtra("teacher",teacher);
                        UpdateProfile.putExtra("image",image);
                        startActivity(UpdateProfile);
                    }
                });

                PerformanceBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent PerformanceIntent = new Intent(Profile.this,StudentsPerformance.class);
                        startActivity(PerformanceIntent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(Profile.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(Profile.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewProfile);
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
            }
        }else{
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent gotoSignIn = new Intent(Profile.this, Start.class);
        startActivity(gotoSignIn);
        finish();
    }
}
