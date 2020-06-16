package com.himanshumauri.learning.show;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshumauri.learning.Home;
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.menu.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowMCQ extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView question,UploaderName,TotalQuestions;
    private LinearLayout OptionContainer;
    private Button NextBtn;
    private int count = 0;
    private DatabaseReference DatabaseRef;
    private List<MCQModel> list;
    private int position = 0;
    private int score = 0;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_mcq);

        final String type = getIntent().getStringExtra("Type");
        final String pId = getIntent().getStringExtra("pId");
        final  String uID = getIntent().getStringExtra("uID");

        mToolbar = findViewById(R.id.mcq_show_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("MCQ Test");

        question = findViewById(R.id.MCQQuestion);
        UploaderName = findViewById(R.id.UploaderName);
        TotalQuestions = findViewById(R.id.TotalQuestions);
        OptionContainer = findViewById(R.id.options_container);
        NextBtn = findViewById(R.id.nextBtn);

        //------------------------------Uploader------------------------------
        assert uID != null;
        DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference("users").child(uID);

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
                Intent uploader = new Intent(ShowMCQ.this, Profile.class);
                uploader.putExtra("uploader",uID);
                startActivity(uploader);
            }
        });

        //------------------------Firebase -----------------------------------
        assert type != null;
        assert pId != null;
        DatabaseRef = FirebaseDatabase.getInstance().getReference("Contents").child(type);
        DatabaseRef.keepSynced(false);

        list = new ArrayList<>();

        DatabaseRef.child(pId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(snapshot.getValue(MCQModel.class));
                }
                if (list.size() > 0){

                    for (int i =0; i< 4;i++){
                        OptionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {
                                checkAnswer(((Button)v));
                            }
                        });
                    }

                    playMCQ(question,0,list.get(position).getQuestion());
                    NextBtn.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {
                            NextBtn.setEnabled(false);
                            NextBtn.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if (position == list.size()){
                                SaveResult(score,list.size(),DatabaseRef,pId,uID);
                                Intent scoreIntent = new Intent(ShowMCQ.this,MCQResult.class);
                                scoreIntent.putExtra("Score",score);
                                scoreIntent.putExtra("total",list.size());
                                scoreIntent.putExtra("uploader",uID);
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }
                            count = 0;
                            playMCQ(question,0,list.get(position).getQuestion());
                        }
                    });

                }else {
                    Toast.makeText(ShowMCQ.this, "No Questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowMCQ.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(ShowMCQ.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(ShowMCQ.this);
        adView.setAdSize(AdSize.FULL_BANNER);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewShowMCQ);
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

    private void SaveResult(int score, int size, DatabaseReference databaseRef, String pId, String uID) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        final String mCurrentUser = mUser.getUid();
        final String timeStamp = String.valueOf(System.currentTimeMillis());

        Map<String, Object> map = new HashMap<>();
        map.put("student", mCurrentUser);
        map.put("score", score);

        databaseRef.child("topic").child(uID).child(pId).child("students").child(timeStamp).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ShowMCQ.this, "Test Over", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void playMCQ(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4){
                    String option = "";
                    if (count == 0){
                        option = list.get(position).getOptionA();
                    }else if (count == 1){
                        option = list.get(position).getOptionB();
                    }else if (count == 2){
                        option = list.get(position).getOptionC();
                    }else if (count == 3){
                        option = list.get(position).getOptionD();
                    }
                    playMCQ(OptionContainer.getChildAt(count),0, option);
                    count ++;
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationEnd(Animator animation) {
                //-------------------data change----------------------------

                if (value == 0){
                    try {
                        ((TextView)view).setText(data);
                        TotalQuestions.setText(position + 1 + "/" + list.size());
                    }catch (ClassCastException e){
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playMCQ(view,1,data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(Button selectedOption) {
        enableOption(false);
        NextBtn.setEnabled(true);
        NextBtn.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getAnswer())){
            //-----Correct---------
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }else {
            //------wrong----------
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctoption = OptionContainer.findViewWithTag(list.get(position).getAnswer());
            correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){
        for (int i =0; i< 4;i++){
            OptionContainer.getChildAt(i).setEnabled(enable);
            if (enable){
                OptionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#67FFEB3B")));
            }
        }
    }
}
