package com.himanshumauri.learning.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.himanshumauri.learning.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView Image;
    private EditText Name, Phone, Status;
    private CheckBox checkBox;
    private Button UpdateBtn;
    private static final int IMAGE_PICK_GALLERY_CODE = 100;
    private ProgressDialog pd;

    private FirebaseAuth mAuth;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        final String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String status = getIntent().getStringExtra("status");
        String phone = getIntent().getStringExtra("phone");
        String teacher = getIntent().getStringExtra("teacher");
        String image = getIntent().getStringExtra("image");

        Image = findViewById(R.id.UpdateProfileIv);
        Name = findViewById(R.id.UpdateNameTv);
        Phone = findViewById(R.id.UpdateMobTv);
        Status = findViewById(R.id.UpdateStatusTv);
        checkBox = findViewById(R.id.TeacherCb);
        UpdateBtn = findViewById(R.id.UpdateDetailsBtn);
        toolbar = findViewById(R.id.UpdateProfileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Profile");

        pd = new ProgressDialog(this);
        pd.setMessage("Updating..");

        assert image != null;
        if (!image.equals("no_image")) {
            Glide.with(UpdateProfile.this).load(image).fitCenter().placeholder(R.drawable.ic_image_holder).into(Image);
        }

        Name.setText(name);
        Phone.setText(phone);
        Status.setText(status);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        final String mCurrentUser = mUser.getUid();

        final DatabaseReference Datareference = FirebaseDatabase.getInstance().getReference("users").child(mCurrentUser);

        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
            }
        });

        assert teacher != null;
        if (teacher.equals("true")) {
            checkBox.setVisibility(View.GONE);
        } else {
            DatabaseReference FRRef = FirebaseDatabase.getInstance().getReference("Request").child("teacher");
            FRRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(mCurrentUser)){
                        checkBox.setChecked(true);
                        checkBox.setVisibility(View.VISIBLE);
                    } else {
                        checkBox.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                final String newName = Name.getText().toString();
                final String newPhone = Phone.getText().toString();
                final String newStatus = Status.getText().toString();

                final StorageReference reference = FirebaseStorage.getInstance().getReference().child("user_image").child(mCurrentUser + "_" + name + ".jpg");
                final String timeStamp = String.valueOf(System.currentTimeMillis());

                Image.setDrawingCacheEnabled(true);
                Image.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) Image.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = reference.putBytes(data);

                DatabaseReference RequestDatabase = FirebaseDatabase.getInstance().getReference("Request").child("teacher");
                if (checkBox.isChecked()) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("user", mCurrentUser);
                    hashMap.put("name", newName);
                    RequestDatabase.child(mCurrentUser).setValue(hashMap);
                } else {
                    RequestDatabase.child(mCurrentUser).removeValue();
                }

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String download_uri = uri.toString();

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("name", newName);
                                        map.put("phone", newPhone);
                                        map.put("status", newStatus);
                                        map.put("image", download_uri);
                                        Datareference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    pd.dismiss();
                                                    Toast.makeText(UpdateProfile.this, "Updated Successful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.hide();
                                        Toast.makeText(UpdateProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        //------------------Banner Ads---------------------------------------------------------

        MobileAds.initialize(UpdateProfile.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(UpdateProfile.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));

        mAdView = findViewById(R.id.adViewUpdateProfile);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK) {
            assert data != null;
            Uri uri = data.getData();
            Image.setImageURI(uri);
        }
    }
}
