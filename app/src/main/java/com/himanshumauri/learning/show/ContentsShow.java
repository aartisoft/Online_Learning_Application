package com.himanshumauri.learning.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.himanshumauri.learning.R;

public class ContentsShow extends AppCompatActivity {
    private TextView Text,Text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contents_show);

        final String pId = getIntent().getStringExtra("pId");
        final String type = getIntent().getStringExtra("pType");
        final  String uID = getIntent().getStringExtra("uID");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                assert type != null;
                if (type.equals("LongAQ")){
                    Intent go = new Intent(ContentsShow.this, ShowQuestion.class);
                    go.putExtra("Type",type);
                    go.putExtra("pId",pId);
                    go.putExtra("uID",uID);
                    startActivity(go);
                    finish();
                }else if (type.equals("MCQ")){
                    Intent go = new Intent(ContentsShow.this, ShowMCQ.class);
                    go.putExtra("Type",type);
                    go.putExtra("pId",pId);
                    go.putExtra("uID",uID);
                    startActivity(go);
                    finish();
                }else if (type.equals("Notes")){
                    Intent go = new Intent(ContentsShow.this, NotesTitle.class);
                    go.putExtra("Type",type);
                    go.putExtra("pId",pId);
                    go.putExtra("uID",uID);
                    startActivity(go);
                    finish();
                }else if (type.equals("ShortAQ")){
                    Intent go = new Intent(ContentsShow.this, ShowQuestion.class);
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
