package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriteActivity extends AppCompatActivity {
    EditText title, content;
    Button submit;
    Intent pintent;
    String user_name, user_test, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        title=(EditText)findViewById(R.id.title);
        content=(EditText)findViewById(R.id.content);
        submit=(Button)findViewById(R.id.submit);

        pintent=getIntent();
        user_test=pintent.getExtras().getString("test");
        user_email=pintent.getExtras().getString("email");
        user_name=pintent.getExtras().getString("name");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().equals("")) {
                    Toast.makeText(WriteActivity.this,"제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if (content.getText().toString().equals("")) {
                    Toast.makeText(WriteActivity.this,"내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    //저장
                    BoardWrite task = new BoardWrite();
                    task.execute(title.getText().toString(),content.getText().toString(),user_test,user_email,user_name);
                    Toast.makeText(WriteActivity.this,"작성이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public void back(View v) {
        finish();
    }
}
