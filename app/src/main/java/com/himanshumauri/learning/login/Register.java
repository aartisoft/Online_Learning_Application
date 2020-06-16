package com.himanshumauri.learning.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshumauri.learning.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private Toolbar mToolBar;
    private EditText mDisplayName, mEmailID, mPassword;
    private Button mRegBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private ProgressDialog mRegprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mToolBar = findViewById(R.id.reg_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register new Account");


        //Progress Bar
        mRegprogress = new ProgressDialog(this);
        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mDisplayName = findViewById(R.id.reg_DisplayName);
        mEmailID = findViewById(R.id.reg_EmailId);
        mPassword = findViewById(R.id.reg_Password);

        mRegBtn = findViewById(R.id.reg_CreateAcc);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = mDisplayName.getText().toString();
                String email = mEmailID.getText().toString();
                String password = mPassword.getText().toString();

                if (display_name.equals("")) {
                    Toast.makeText(Register.this, "Enter name", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } else if (email.equals("")) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } else if (password.equals("")) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } else if (!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    mRegprogress.setTitle("Registering User");
                    mRegprogress.setMessage("Please Wait...");
                    mRegprogress.setCanceledOnTouchOutside(true);
                    mRegprogress.show();
                    registerNewUser(display_name, email, password);
                } else {
                    Toast.makeText(Register.this, "Please fill all information", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerNewUser(final String display_name, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    //taking User ID
                    String uID = null;
                    if (currentUser != null) {
                        uID = currentUser.getUid();
                    }
                    mDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(uID);
                    HashMap<String, String> userMap = new HashMap<>();
                    if (!display_name.equals("")) {
                        userMap.put("name", display_name);
                    } else {
                        userMap.put("name", "User Name");
                    }

                    userMap.put("email", email);
                    userMap.put("status", "Missing School? Don't worry\\nJoin Self Study \uD83D\uDCDA✏ app \uD83D\uDCF2 and Boost your Study");
                    userMap.put("image", "https://firebasestorage.googleapis.com/v0/b/learning-engin.appspot.com/o/user_image%2FcJhpnFq9ueVbeo56WyGDHHhKEty2_Test%201.jpg?alt=media&token=502a486d-e54c-4c91-ad43-493a43be8279");
                    userMap.put("teacher", "false");
                    userMap.put("phone", "+91 XXXX-XXXXXX");

                    mDataBase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Send Email Verification link
                                assert currentUser != null;
                                currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mRegprogress.dismiss();
                                            Toast.makeText(Register.this, "Registration Successful, Please check your email for verification.", Toast.LENGTH_LONG).show();
                                            Intent goforMain = new Intent(Register.this, Login.class);
                                            goforMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(goforMain);
                                        } else {
                                            Toast.makeText(Register.this, "Can't Register. ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    mRegprogress.hide();
                    Toast.makeText(Register.this, "Can't Register. Please Check form & Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
