package com.himanshumauri.learning.upload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.himanshumauri.learning.R;
import com.himanshumauri.learning.login.Login;
import com.himanshumauri.learning.login.Start;

public class ContentsUpload extends AppCompatActivity {
    private TextView Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contents_upload);

        final String type = getIntent().getStringExtra("Type");
        final String pId = getIntent().getStringExtra("pId");
        final  String uID = getIntent().getStringExtra("uID");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                assert type != null;
                if (type.equals("LongAQ")){
                    Intent go = new Intent(ContentsUpload.this, UploadLongAQ.class);
                    go.putExtra("Type",type);
                    go.putExtra("pId",pId);
                    go.putExtra("uID",uID);
                    startActivity(go);
                    finish();
                }else if (type.equals("MCQ")){
                    Intent go = new Intent(ContentsUpload.this, UploadMCQ.class);
                    go.putExtra("Type",type);
                    go.putExtra("pId",pId);
                    go.putExtra("uID",uID);
                    startActivity(go);
                    finish();
                }else if (type.equals("Notes")){
                    Intent go = new Intent(ContentsUpload.this, UploadNotes.class);
                    go.putExtra("Type",type);
                    go.putExtra("pId",pId);
                    go.putExtra("uID",uID);
                    startActivity(go);
                    finish();
                }else if (type.equals("ShortAQ")){
                    Intent go = new Intent(ContentsUpload.this, UploadShortAQ.class);
                    go.putExtra("Type",type);
                    go.putExtra("pId",pId);
                    go.putExtra("uID",uID);
                    startActivity(go);
                    finish();
                }
            }
        },2000);

    }
}
