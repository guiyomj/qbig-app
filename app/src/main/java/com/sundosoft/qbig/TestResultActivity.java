package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestResultActivity extends AppCompatActivity {
    Button goview, gomain;
    String user_email, user_test, mode;
    Intent pintent;
    TextView score,cor,exam,comment;

    int correct, total;
    String year, year_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        goview=(Button)findViewById(R.id.goview);
        gomain=(Button)findViewById(R.id.gomain);
        score=(TextView)findViewById(R.id.score);
        exam=(TextView)findViewById(R.id.exam);
        comment=(TextView)findViewById(R.id.comment);
        cor=(TextView)findViewById(R.id.cor);

        pintent=getIntent();
        correct=pintent.getExtras().getInt("correct");
        total=pintent.getExtras().getInt("munje");
        user_email=pintent.getExtras().getString("email");
        user_test=pintent.getExtras().getString("exam");
        year=pintent.getExtras().getString("year");
        mode=pintent.getExtras().getString("mode");
        year_no=pintent.getExtras().getString("yearno");

        score.setText(total+"문제 중 "+correct+"문제 정답");
        if (mode.equals("1"))
            exam.setText(" "+user_test+" "+year+"년도 "+year_no+"회차 ");
        else
            exam.setText(user_test+" 랜덤 모드");
        cor.setText("정답률 "+(int)((correct)/(total)*100)+"%");

        //Spannable span = (Spannable)exam.getText();
        //span.setSpan(new BackgroundColorSpan(Color.parseColor("#fef36b")), 0, exam.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(TestResultActivity.this, ReviewActivity.class);
                intent.putExtra("email",user_email);
                startActivity(intent);
            }
        });

        gomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
