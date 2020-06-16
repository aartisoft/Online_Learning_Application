package com.himanshumauri.learning.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.TopicModel;
import com.himanshumauri.learning.TopicViewAdapter;
import com.himanshumauri.learning.upload.ContentsUpload;

import java.util.HashMap;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShortAQ extends Fragment {
    private View mSAQView;
    private TextView pathText;

    private LinearLayout UploadSection;
    private EditText UploadText;
    private Button UploadBtn;

    private ProgressDialog pd;
    private RecyclerView recyclerView;
    private TopicViewAdapter topicViewAdapter;

    private FirebaseAuth mAuth;
    private AdView mAdView;
    public ShortAQ() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSAQView = inflater.inflate(R.layout.fragment_shortaq, container, false);

        final String pID = getActivity().getIntent().getExtras().getString("pId");
        final String ParentId = getActivity().getIntent().getExtras().getString("ParentKey");
        final String GrandId = getActivity().getIntent().getExtras().getString("GrandKey");
        final String PathText = GrandId + ">>" + ParentId + ">>" + pID + ">>" + "Short AQ";

        pathText = mSAQView.findViewById(R.id.ShortAQPathView);
        pathText.setSelected(true);
        pathText.setText(PathText);

        pd = new ProgressDialog(getContext());

        //--------------------AdMob Initialization---------------------
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(Objects.requireNonNull(getContext()));
        adView.setAdSize(AdSize.FULL_BANNER);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = mSAQView.findViewById(R.id.adViewShortAQ);
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

        //------------------finding by ID------------------------
        UploadSection = mSAQView.findViewById(R.id.UploadTopicShortAQ);
        UploadText = mSAQView.findViewById(R.id.topicTitleShortAQEt);
        UploadBtn = mSAQView.findViewById(R.id.topicUploadShortAQBtn);

        //------------------Firebase------------------------------
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        final String mCurrentUser = mUser.getUid();
        DatabaseReference UserData = FirebaseDatabase.getInstance().getReference("users").child(mCurrentUser);
        UserData.keepSynced(false);

        assert GrandId != null;
        assert ParentId != null;
        assert pID != null;
        final DatabaseReference Database = FirebaseDatabase.getInstance().getReference("Category")
                .child(GrandId).child("subCategory").child(ParentId).child("Subject").child(pID).child("Topic");
        Database.keepSynced(false);

        //----------------Implementing Upload Section-------------------
        UserData.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String teacher = Objects.requireNonNull(dataSnapshot.child("teacher").getValue()).toString();

                if (teacher.equals("true")){
                    UploadSection.setVisibility(View.VISIBLE);
                    UploadText.setVisibility(View.VISIBLE);
                    UploadBtn.setVisibility(View.VISIBLE);
                }else {
                    UploadSection.setVisibility(View.GONE);
                    UploadText.setVisibility(View.GONE);
                    UploadBtn.setVisibility(View.GONE);
                }
                UploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd.show();
                        String Title = UploadText.getText().toString();
                        final String timeStamp = String.valueOf(System.currentTimeMillis());
                        if (!Title.equals("")){
                            HashMap<Object , String> hashMap = new HashMap<>();
                            hashMap.put("Title", Title);
                            hashMap.put("Uid", mCurrentUser);
                            hashMap.put("approved","false");

                            Database.child("ShortAQ").child(timeStamp).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        UploadText.setText("");
                                        Toast.makeText(getContext(), "Topic Uploaded Successfully,Now add Questions", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        String send = "ShortAQ";
                                        Intent questionUpload = new Intent(getContext(), ContentsUpload.class);
                                        questionUpload.putExtra("Type",send);
                                        questionUpload.putExtra("pId",timeStamp);
                                        questionUpload.putExtra("uID",mCurrentUser);
                                        startActivity(questionUpload);
                                    }
                                }
                            });
                        }else {
                            pd.hide();
                            Toast.makeText(getContext(), "Please enter topic of your QUSETIONS", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //----------------------------Recycler View-----------------------------------
        recyclerView = mSAQView.findViewById(R.id.ShortAQTopicRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference topicData = Database.child("ShortAQ");
        topicData.keepSynced(false);
        FirebaseRecyclerOptions<TopicModel> options = new FirebaseRecyclerOptions.Builder<TopicModel>().setQuery(topicData, TopicModel.class).build();
        topicViewAdapter = new TopicViewAdapter(options);
        recyclerView.setAdapter(topicViewAdapter);

        return mSAQView;
    }
    @Override
    public void onStart() {
        super.onStart();
        topicViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        topicViewAdapter.stopListening();
    }
}
