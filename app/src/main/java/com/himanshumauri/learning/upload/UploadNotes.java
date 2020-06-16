package com.himanshumauri.learning.upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.login.Start;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UploadNotes extends AppCompatActivity {
    private Toolbar mToolBar;
    private EditText mTitle,mBody,VideoSource;
    private Button Upload;
    private ProgressDialog pd;

    private FirebaseAuth mAuth;
    private DatabaseReference DataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_notes);

        final String type = getIntent().getStringExtra("Type");
        final String pId = getIntent().getStringExtra("pId");
        final  String uID = getIntent().getStringExtra("uID");

        //----------------Finding Layouts-------------------------------
        mToolBar = findViewById(R.id.notes_upload_toolbar);
        mTitle = findViewById(R.id.notes_title);
        mBody = findViewById(R.id.notes_body);
        Upload = findViewById(R.id.uploadNotes);
        VideoSource = findViewById(R.id.VideoSource);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");

        //----------setting Toolbar-------------------------
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("UPLOAD Notes");

        //-------------Getting Current User--------------------
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        final String mCurrentUser = mUser.getUid();
        //------------Database Ref--------------------------
        assert type != null;
        assert pId != null;
        DataBase = FirebaseDatabase.getInstance().getReference("Contents").child(type).child(pId);

        //--------------Implementing Upload Button----------------
        if (mCurrentUser.equals(uID)){
            Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd.show();
                    String title = mTitle.getText().toString();
                    String body = mBody.getText().toString();
                    String link = VideoSource.getText().toString();

                    if (!title.equals("") && !body.equals("")){
                        UploadWithoutImage(DataBase,title,body,link,mCurrentUser);
                    }else{
                        Toast.makeText(UploadNotes.this, "All input is mandatory", Toast.LENGTH_SHORT).show();
                        pd.hide();
                    }
                }
            });
        }

    }

    private void UploadWithoutImage(DatabaseReference dataBase, String title, String body, String link, String mCurrentUser) {
        final String timeStamp = String.valueOf(System.currentTimeMillis());

        HashMap<Object,String> hashMap = new HashMap<>();
        hashMap.put("title",title);
        hashMap.put("body",body);
        hashMap.put("video",link);
        hashMap.put("uploader",mCurrentUser);
        hashMap.put("verified","false");

        dataBase.child(timeStamp).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(UploadNotes.this, "Uploaded Successfully you can add another Notes", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            }
        });
    }

    public void onStart() {
        super.onStart();
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
        Intent gotoSignIn = new Intent(UploadNotes.this, Start.class);
        startActivity(gotoSignIn);
        finish();
    }
}
