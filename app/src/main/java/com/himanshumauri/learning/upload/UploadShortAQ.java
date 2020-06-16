package com.himanshumauri.learning.upload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.login.Start;

import java.util.HashMap;

public class UploadShortAQ extends AppCompatActivity {
    private Toolbar mToolBar;
    private EditText Question,Answer;
    private Button Upload,Clear;
    private String question,answer;
    private ProgressDialog pd;

    private FirebaseAuth mAuth;
    private DatabaseReference DataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_short_aq);

        final String type = getIntent().getStringExtra("Type");
        final String pId = getIntent().getStringExtra("pId");
        final  String uID = getIntent().getStringExtra("uID");

        //-----------------Finding Layouts----------------------------
        mToolBar = findViewById(R.id.ShortAQ_upload_toolbar);
        Question = findViewById(R.id.InputShortAQQuestion);
        Answer = findViewById(R.id.InputShortAQAnswer);
        Clear = findViewById(R.id.SAQclearBtn);
        Upload = findViewById(R.id.SAQuploadBtn);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");

        //----------setting Toolbar-------------------------
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("UPLOAD Short AQ");

        //-------------Getting Current User--------------------
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        final String mCurrentUser = mUser.getUid();
        //------------Database Ref--------------------------
        assert type != null;
        assert pId != null;
        DataBase = FirebaseDatabase.getInstance().getReference("Contents").child(type).child(pId);

        assert uID != null;
        if (uID.equals(mCurrentUser)){
            Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd.show();
                    question = Question.getText().toString();
                    answer = Answer.getText().toString();

                    if (!question.equals("") && !answer.equals("")){
                        uploadLongQA(DataBase,mCurrentUser,question,answer);
                        clearField(Question,Answer);
                    }else {
                        Toast.makeText(UploadShortAQ.this, "All input is mandatory", Toast.LENGTH_SHORT).show();
                        pd.hide();
                    }
                }
            });
        }else {
            Toast.makeText(UploadShortAQ.this, "Unable to Identify you", Toast.LENGTH_SHORT).show();
            pd.hide();
        }
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearField(Question,Answer);
                Toast.makeText(UploadShortAQ.this, "Clear Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clearField(EditText question, EditText answer) {
        question.setText("");
        answer.setText("");
        pd.dismiss();
    }

    private void uploadLongQA(DatabaseReference dataBase, String mCurrentUser, String question, String answer) {
        final String timeStamp = String.valueOf(System.currentTimeMillis());

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("question",question);
        hashMap.put("answer",answer);
        hashMap.put("uploader",mCurrentUser);
        hashMap.put("verified","false");

        dataBase.child(timeStamp).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UploadShortAQ.this, "Uploaded Successfully you can add another Question", Toast.LENGTH_SHORT).show();
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
        Intent gotoSignIn = new Intent(UploadShortAQ.this, Start.class);
        startActivity(gotoSignIn);
        finish();
    }
}
