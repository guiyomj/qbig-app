package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class TestActivity extends AppCompatActivity {
    TextView exam_name, qnum, content, bogi1, bogi2, bogi3, bogi4, next;
    ImageView img, cor, inc, exit;
    LinearLayout s1,s2,s3,s4;
    String mJsonString="";
    int cnt=0, munje=100;
    Boolean ck1, ck2, ck3, ck4, ck5;

    Intent pintent;
    String user_email, user_test, year, yearno, date;

    String[][] test;
    Boolean[] correct;
    Bitmap bmImg;

    answerClickListener al;

    private static final String TAG_JSON="webnautes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        pintent=getIntent();
        user_email=pintent.getExtras().getString("email");
        user_test=pintent.getExtras().getString("test");
        year=pintent.getExtras().getString("year");
        yearno=pintent.getExtras().getString("yearno");
        date=pintent.getExtras().getString("date");

        exam_name=(TextView)findViewById(R.id.exam_name);
        exam_name.setText(user_test);
        qnum=(TextView)findViewById(R.id.qnum);
        content=(TextView)findViewById(R.id.test);
        bogi1=(TextView)findViewById(R.id.bogi1);
        bogi2=(TextView)findViewById(R.id.bogi2);
        bogi3=(TextView)findViewById(R.id.bogi3);
        bogi4=(TextView)findViewById(R.id.bogi4);
        next=(TextView)findViewById(R.id.next);

        img=(ImageView)findViewById(R.id.img);
        cor=(ImageView)findViewById(R.id.res);
        inc=(ImageView)findViewById(R.id.res2);

        exit=(ImageView)findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitDialog();
            }
        });

        s1=(LinearLayout)findViewById(R.id.select1);
        s2=(LinearLayout)findViewById(R.id.select2);
        s3=(LinearLayout)findViewById(R.id.select3);
        s4=(LinearLayout)findViewById(R.id.select4);

        al = new answerClickListener();
        s1.setOnClickListener(al);
        s2.setOnClickListener(al);
        s3.setOnClickListener(al);
        s4.setOnClickListener(al);

        TestGet task = new TestGet();
        task.execute(user_email,user_test,year,yearno);
        System.out.println("aaaa"+date);
    }

    private class answerClickListener implements View.OnClickListener{
        boolean correct = false;
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.select1:
                    if(test[cnt][6].contains(test[cnt][2]) == true) {
                        s1.setBackgroundColor(0xffAFD485);
                        correct = true;
                    }
                    else {
                        s1.setBackgroundColor(0xfff54949);
                        correct = false;
                    }
                    break;
                case R.id.select2:
                    if(test[cnt][6].contains(test[cnt][3]) == true) {
                        s2.setBackgroundColor(0xffAFD485);
                        correct = true;
                    }
                    else {
                        s2.setBackgroundColor(0xfff54949);
                        correct = false;
                    }
                    break;
                case R.id.select3:
                    if(test[cnt][6].contains(test[cnt][4]) == true) {
                        s3.setBackgroundColor(0xffAFD485);
                        correct = true;
                    }
                    else {
                        s3.setBackgroundColor(0xfff54949);
                        correct = false;
                    }
                    break;
                case R.id.select4:
                    if(test[cnt][6].contains(test[cnt][5]) == true) {
                        s4.setBackgroundColor(0xffAFD485);
                        correct = true;
                    }
                    else {
                        s4.setBackgroundColor(0xfff54949);
                        correct = false;
                    }
                    break;
            }

            if(!correct) {
                if(test[cnt][6].contains(test[cnt][2]) == true) {
                    s1.setBackgroundColor(0xffAFD485);
                }
                else if(test[cnt][6].contains(test[cnt][3]) == true) {
                    s2.setBackgroundColor(0xffAFD485);
                }
                else if(test[cnt][6].contains(test[cnt][4]) == true) {
                    s3.setBackgroundColor(0xffAFD485);
                }
                else {
                    s4.setBackgroundColor(0xffAFD485);
                }
                inc.setVisibility(View.VISIBLE);
            }
            else {
                cor.setVisibility(View.VISIBLE);
            }

            Handler delayHandler = new Handler();
            if (cnt<10) {
                delayHandler.postDelayed(new Runnable() {
                    @Override public void run()
                    {
                        cnt++;
                        renderTest();
                    }}, 1500);
            }
            else finish();
        }
    }

    public void renderTest() {
        s1.setBackgroundColor(0xff111111);
        s2.setBackgroundColor(0xffffffff);
        s3.setBackgroundColor(0xff111111);
        s4.setBackgroundColor(0xffffffff);

        cor.setVisibility(View.INVISIBLE);
        inc.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        qnum.setText(test[cnt][0]);
        content.setText(test[cnt][1]);
        bogi1.setText(test[cnt][2]);
        bogi2.setText(test[cnt][3]);
        bogi3.setText(test[cnt][4]);
        bogi4.setText(test[cnt][5]);

        if (test[cnt][7].contains("O")) {
            try {
                back t = new back();
                Bitmap tmp = t.execute("http://54.180.159.44/" + user_test + "/" + year + "/" + yearno + "/" + test[cnt][0].substring(1) + ".png").get();
                int width = tmp.getWidth();
                int height = tmp.getHeight();
                tmp=Bitmap.createScaledBitmap(tmp,content.getWidth()/4*3,height*content.getWidth()/width/4*3,true);
                img.setImageBitmap(tmp);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public class back extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);

            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }

        protected void onPostExecute(Bitmap img){
            //a.setImageBitmap(bmImg);
        }
    }

    public void ExitDialog() {
        View dlgView = View.inflate(this,R.layout.dialog_login,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok,cancel,text;

        text = (TextView)dlgView.findViewById(R.id.text);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
        text.setText("학습을 종료하시겠습니까?");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //디비에 반영해야됨
                finish();
                dlg.dismiss();
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.show();
    }

    private class TestGet extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mJsonString = result;      // 서버로부터 받은 메시지(JSON)
                Log.d("position check",mJsonString);

                test=new String[munje][8];
                correct=new Boolean[munje];
                try {
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    for (int i = 0; i < munje; i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        test[i][0]="Q"+item.getString("qno");
                        test[i][1]=item.getString("question");
                        test[i][2]=item.getString("bogi1");
                        test[i][3]=item.getString("bogi2");
                        test[i][4]=item.getString("bogi3");
                        test[i][5]=item.getString("bogi4");
                        test[i][6]=item.getString("ans");
                        test[i][7]=item.getString("img");
                    }

                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                }
                renderTest();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            String searchKeyword4 = params[3];
            String serverURL = "http://54.180.159.44/test2.php";
            String postParameters = "email=" + searchKeyword1 + "&q_name=" + searchKeyword2 + "&pre_question_year=" + searchKeyword3 + "&pre_question_year_no=" + searchKeyword4;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("TAG", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d("TAG", "InsertData: Error ", e);

                return null;
            }
        }
    }

}
