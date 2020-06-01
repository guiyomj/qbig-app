package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InquiryActivity extends AppCompatActivity {
    Intent pintent;
    String user_email;
    TextView email;
    EditText content;
    Button submit;
    Date d = new Date(System.currentTimeMillis());
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    String today = sf.format(d);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        pintent=getIntent();
        user_email=pintent.getExtras().getString("email");

        email = (TextView)findViewById(R.id.email);
        content = (EditText)findViewById(R.id.content);
        submit = (Button)findViewById(R.id.submit);

        email.setText(user_email);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.getText().toString().length()>0) {
                    SubmitInquery task = new SubmitInquery();
                    task.execute(user_email, content.getText().toString(), today);
                    Toast.makeText(InquiryActivity.this, "문의가 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(InquiryActivity.this, "문의 내용을 입력해주세요..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View v) {
        finish();
    }
}
