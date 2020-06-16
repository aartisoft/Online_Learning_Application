package com.himanshumauri.learning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.himanshumauri.learning.login.Start;
import com.himanshumauri.learning.menu.ContactUs;
import com.himanshumauri.learning.menu.Profile;
import com.himanshumauri.learning.menu.Settings;

public class LastCategory extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mviewPager;
    private SectionPageAdapter mSectionPagerAdapter;
    private TabLayout mtabLayout;

    private ProgressDialog pd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_category);

        //--------------------Main App function---------------------------------------------

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");

        //--------ToolBar-----------
        mToolbar = findViewById(R.id.topicToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Topics");

        //---------------------Section Page Adapter-----------------------------------------
        //Tabs
        mviewPager = findViewById(R.id.main_tab_pager);
        mSectionPagerAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(mSectionPagerAdapter);

        mtabLayout = findViewById(R.id.main_tabs);
        mtabLayout.setupWithViewPager(mviewPager);

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
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
        Intent gotoSignIn = new Intent(LastCategory.this, Start.class);
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
                Intent homeIntent = new Intent(LastCategory.this,Home.class);
                startActivity(homeIntent);
                break;
            case R.id.profile:
                Intent profileIntent = new Intent(LastCategory.this, Profile.class);
                startActivity(profileIntent);
                break;
            case R.id.appSetting:
                Intent settingIntent = new Intent(LastCategory.this, Settings.class);
                startActivity(settingIntent);
                break;
            case R.id.contactUs:
                Intent contactIntent = new Intent(LastCategory.this, ContactUs.class);
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
