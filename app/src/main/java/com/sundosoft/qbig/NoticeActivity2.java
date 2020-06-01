package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NoticeActivity2 extends AppCompatActivity {
    Intent pintent;
    TextView title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice2);
        pintent = getIntent();
        title=(TextView)findViewById(R.id.title);
        content=(TextView)findViewById(R.id.content);

        title.setText(pintent.getExtras().getString("title"));
        content.setText(pintent.getExtras().getString("content"));
    }

    public void back(View v) {
        finish();
    }
}
