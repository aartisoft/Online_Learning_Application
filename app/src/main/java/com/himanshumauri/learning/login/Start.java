package com.himanshumauri.learning.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.himanshumauri.learning.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent go = new Intent(Start.this,Login.class);
                startActivity(go);
                finish();
            }
        },3000);
    }
}
