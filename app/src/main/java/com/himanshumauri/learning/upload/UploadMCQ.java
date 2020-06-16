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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.himanshumauri.learning.LastCategory;
import com.himanshumauri.learning.R;
import com.himanshumauri.learning.login.Start;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UploadMCQ extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText QuestionEt,InputOptionA,InputOptionB,InputOptionC,InputOptionD;
    private RadioGroup Answer;
    private RadioButton SelectedAnswer;
    private Button ClearBtn,Upload;
    private ProgressDialog pd;

    private String Question,InputA,InputB,InputC,InputD,CorrectOption,Option;

    private FirebaseAuth mAuth;
    private DatabaseReference MCQDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_mcq);

        final String type = getIntent().getStringExtra("Type");
        final String pId = getIntent().getStringExtra("pId");
        final  String uID = getIntent().getStringExtra("uID");

        //------------Finding Layouts---------------------
        mToolbar = findViewById(R.id.mcq_upload_toolbar);
        //MCQImage = findViewById(R.id.InputMCQImage);
        QuestionEt = findViewById(R.id.InputMCQQuestion);
        InputOptionA = findViewById(R.id.InputOptionA);
        InputOptionB = findViewById(R.id.InputOptionB);
        InputOptionC = findViewById(R.id.InputOptionC);
        InputOptionD = findViewById(R.id.InputOptionD);
        Answer = findViewById(R.id.Answer);
        ClearBtn = findViewById(R.id.clearBtn);
        Upload = findViewById(R.id.uploadBtn);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");

        //----------setting Toolbar-------------------------
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("UPLOAD MCQ");

        //-------------Getting Current User--------------------
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        final String mCurrentUser = mUser.getUid();
        //------------Database Ref--------------------------
        assert type != null;
        assert pId != null;
        MCQDataBase = FirebaseDatabase.getInstance().getReference("Contents").child(type).child(pId);



        assert uID != null;
        if (uID.equals(mCurrentUser)){
            Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd.show();
                    //-----------------------Getting Data-----------------

                    Question = QuestionEt.getText().toString();
                    InputA = InputOptionA.getText().toString();
                    InputB = InputOptionB.getText().toString();
                    InputC = InputOptionC.getText().toString();
                    InputD = InputOptionD.getText().toString();

                    int selectedId = Answer.getCheckedRadioButtonId();
                    SelectedAnswer = findViewById(selectedId);
                    String no = "";

                    if (!Question.equals("") && !InputA.equals("") && !InputB.equals("")
                            && !InputC.equals("") && !InputD.equals("") && SelectedAnswer != null){

                        Option = SelectedAnswer.getText().toString();
                        switch (Option) {
                            case "1st":
                                CorrectOption = InputA;
                                break;
                            case "2nd":
                                CorrectOption = InputB;
                                break;
                            case "3rd":
                                CorrectOption = InputC;
                                break;
                            case "4th":
                                CorrectOption = InputD;
                                break;
                            default:
                                CorrectOption = no;
                                break;
                        }
                        uploadWithoutImage();
                    }else {

                            pd.dismiss();
                            Toast.makeText(UploadMCQ.this, "All input is mandatory", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        ClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputOptionA.setText("");
                InputOptionB.setText("");
                InputOptionC.setText("");
                InputOptionD.setText("");
                QuestionEt.setText("");
                Toast.makeText(UploadMCQ.this, "Clear Successful", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

    }

    private void uploadWithoutImage() {
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        //-------------Getting Current User--------------------
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        final String mCurrentUser = mUser.getUid();

        HashMap<Object,String> hashMap = new HashMap<>();
        hashMap.put("uploader",mCurrentUser);
        hashMap.put("question",Question);
        hashMap.put("optionA",InputA);
        hashMap.put("optionB",InputB);
        hashMap.put("optionC",InputC);
        hashMap.put("optionD",InputD);
        hashMap.put("answer",CorrectOption);

        MCQDataBase.child(timeStamp).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UploadMCQ.this, "Uploaded Successfully Add Another Question", Toast.LENGTH_SHORT).show();
                    finish();
                    pd.dismiss();
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
        Intent gotoSignIn = new Intent(UploadMCQ.this, Start.class);
        startActivity(gotoSignIn);
        finish();
    }

}
