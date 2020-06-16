package com.himanshumauri.learning.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.himanshumauri.learning.Home;
import com.himanshumauri.learning.R;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private Toolbar mToolBar;
    private EditText mEmailId,mPassword;
    private Button mLoginBtn;
    private TextView mForgetPass,mRegisterAccount;
    private ProgressDialog progressBar;
    private FirebaseAuth mAuth;

    private static final String TAG ="FacebookAuth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        mToolBar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Sign In");

        mEmailId = findViewById(R.id.login_EmailId);
        mPassword = findViewById(R.id.login_Password);
        mLoginBtn = findViewById(R.id.login_Button);
        mForgetPass = findViewById(R.id.SendToForgetPasswordPage);
        mRegisterAccount = findViewById(R.id.SendToRegPage);

        progressBar= new ProgressDialog(this);

        mForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SentForgetPassword = new Intent(Login.this, ForgotPassword.class);
                startActivity(SentForgetPassword);
            }
        });
        mRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SentForgetPassword = new Intent(Login.this,Register.class);
                startActivity(SentForgetPassword);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailId.getText().toString();
                String password = mPassword.getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    progressBar.setTitle("Logging In");
                    progressBar.setMessage("Please wait while we check your credentials");
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.show();

                    loginUser(email,password);
                }
            }
        });

    }


    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //Check User is verified or Not
                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                        progressBar.dismiss();
                        Intent mainintent = new Intent(Login.this, Home.class);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainintent);
                        finish();
                    }else {
                        progressBar.hide();
                        Toast.makeText(Login.this,"Please Verify your Email",Toast.LENGTH_LONG).show();
                    }
                }else {
                    progressBar.hide();
                    Toast.makeText(Login.this,"Can't Sign In. Please Check form & Try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
