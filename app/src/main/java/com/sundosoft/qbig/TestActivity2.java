package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class TestActivity2 extends AppCompatActivity {
    ListView exam, cor;
    Intent pintent;
    String user_email, user_test, year, year_no, mode;
    Boolean ck1, ck2, ck3, ck4, ck5;
    TextView submit;
    ImageView exit;
    private static final String TAG_JSON="webnautes";

    public static Context context;

    String[][] test;
    String[] correct;
    ArrayList random, r;
    int cnt=0, munje=0, result=0;
    String mJsonString="";

    public ExamAdapter adapter1;
    public CorAdapter adapter2;

    Bitmap bmImg;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        context=this;
        munje=100;
        progressDialog = new ProgressDialog(TestActivity2.this);

        adapter1 = new ExamAdapter();
        adapter2 = new CorAdapter();

        pintent=getIntent();
        user_email=pintent.getExtras().getString("email");
        user_test=pintent.getExtras().getString("test");
        year=pintent.getExtras().getString("year");
        year_no=pintent.getExtras().getString("yearno");
        mode = pintent.getExtras().getString("mode");

        if (mode.equals("2")) {
            ck1=pintent.getExtras().getBoolean("ck1");
            ck2=pintent.getExtras().getBoolean("ck2");
            ck3=pintent.getExtras().getBoolean("ck3");
            ck4=pintent.getExtras().getBoolean("ck4");
            ck5=pintent.getExtras().getBoolean("ck5");

            munje=((ck1?20:0)+(ck2?20:0)+(ck3?20:0)+(ck4?20:0)+(ck5?20:0));

            random = new ArrayList();
            if (ck1) {
                for(int i=1;i<=20;i++)
                    random.add(i);
            }

            if (ck2) {
                for(int i=21;i<=40;i++)
                    random.add(i);
            }

            if (ck3) {
                for(int i=41;i<=60;i++)
                    random.add(i);
            }

            if (ck4) {
                for(int i=61;i<=80;i++)
                    random.add(i);
            }

            if (ck5) {
                for(int i=81;i<=100;i++)
                    random.add(i);
            }
        }

        exam=(ListView)findViewById(R.id.exam);
        cor=(ListView)findViewById(R.id.correct);
        submit=(TextView)findViewById(R.id.submit);
        exit=(ImageView)findViewById(R.id.exit);

        progressDialog.setMessage("문제 로드중...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        progressDialog.show();

        if (mode.equals("1")) {
            TestGet task = new TestGet();
            task.execute(user_test,year,year_no);
        }
        else {
            TestGet2 task2 = new TestGet2();
            task2.execute(user_test);
        }

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitDialog();
            }
        });

    }

    public void test(int pos, int n) {
        String tmp="initial";

        if(n==1) {
            tmp=((CustomDTO)adapter1.getItem(pos)).getBogi1().substring(0,1);
        }
        else if(n==2) {
            tmp=((CustomDTO)adapter1.getItem(pos)).getBogi2().substring(0,1);
        }
        else if(n==3) {
            tmp=((CustomDTO)adapter1.getItem(pos)).getBogi3().substring(0,1);
        }
        else  {
            tmp=((CustomDTO)adapter1.getItem(pos)).getBogi4().substring(0,1);
        }

        ((CustomDTO)adapter2.getItem(pos)).setContent(tmp);
        adapter2.notifyDataSetChanged();
        correct[pos]=tmp;
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
        text.setText("학습을 종료하시겠습니까?\n결과는 저장되지 않습니다.");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private int chaezum() {
        for(int i=0;i<munje;i++) {
            if(correct[i].equals("")) {
                return 1;
            }
        }
        return 0;
    }

    private void update() {
        for(int i=0;i<munje;i++) {
            System.out.println(user_email+user_test+result+"eee"+i);
            if (test[i][6].substring(0,1).equals(correct[i])) {
                result++;
            }
            else {
                // 디비에 틀린거 저장
                UpdateTest a=new UpdateTest();
                a.execute(user_email,user_test,(i+1)+"",year,year_no,correct[i]);
            }
        }

        UpdateHistory b=new UpdateHistory();
        b.execute(user_email,user_test,year,year_no,munje+"",result+"",(munje-result)+"");
    }

    public void SubmitDialog() {
        View dlgView = View.inflate(this,R.layout.dialog_login,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok,cancel,text;

        text = (TextView)dlgView.findViewById(R.id.text);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
        text.setText("답안을 제출하시겠습니까?");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chaezum()==1) {
                    dlg.dismiss();
                    SubmitDialog2();
                }
                else {
                    update();
                    dlg.dismiss();
                    finish();

                    Intent intent = new Intent(TestActivity2.this, TestResultActivity.class);
                    intent.putExtra("email",user_email);
                    intent.putExtra("exam",user_test);
                    intent.putExtra("correct",result);
                    intent.putExtra("munje",munje);
                    intent.putExtra("year",year);
                    intent.putExtra("yearno",year_no);
                    intent.putExtra("mode",mode);
                    startActivity(intent);
                }
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


    public void SubmitDialog2() {
        View dlgView = View.inflate(this,R.layout.dialog_login,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok,cancel,text;

        text = (TextView)dlgView.findViewById(R.id.text);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
        text.setText("아직 체크하지 않은 문제가 있습니다!\n그래도 답안을 제출하시겠습니까?");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                dlg.dismiss();
                finish();

                Intent intent = new Intent(TestActivity2.this, TestResultActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("exam",user_test);
                intent.putExtra("correct",result);
                intent.putExtra("munje",munje);
                intent.putExtra("year",year);
                intent.putExtra("yearno",year_no);
                intent.putExtra("mode",mode);
                startActivity(intent);
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
                correct=new String[munje];

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
                        test[i][7]="no-image";
                        correct[i]="";

                        CustomDTO dto = new CustomDTO();
                        dto.setTitle(item.getString("question").trim());
                        dto.setQnum(item.getString("qno")+". ");
                        dto.setBogi1(item.getString("bogi1").trim());
                        dto.setBogi2(item.getString("bogi2").trim());
                        dto.setBogi3(item.getString("bogi3").trim());
                        dto.setBogi4(item.getString("bogi4").trim());
                        dto.setCk1(View.INVISIBLE);
                        dto.setCk2(View.INVISIBLE);
                        dto.setCk3(View.INVISIBLE);
                        dto.setCk4(View.INVISIBLE);
                        CustomDTO dto2 = new CustomDTO();
                        dto2.setTitle(item.getString("qno")+". ");
                        dto2.setContent("");

                        if (item.getString("img").contains("O")) {
                            back t = new back();
                            Bitmap tmp=t.execute("http://54.180.159.44/"+user_test+"/"+year+"/"+year_no+"/"+item.getString("qno")+".png").get();
                            int width=tmp.getWidth();
                            int height=tmp.getHeight();
                            tmp=Bitmap.createScaledBitmap(tmp,exam.getWidth()/4*3,height*exam.getWidth()/width/4*3,true);
                            dto.setImg(tmp);
                        }

                        adapter1.addItem(dto);
                        adapter2.addItem(dto2);
                    }
                    exam.setAdapter(adapter1);
                    cor.setAdapter(adapter2);

                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                progressDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            String serverURL = "http://54.180.159.44/test.php";
            String postParameters = "q_name=" + searchKeyword1 + "&year=" + searchKeyword2 + "&year_no=" + searchKeyword3;

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

    private class TestGet2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                r = new ArrayList();
                mJsonString = result;      // 서버로부터 받은 메시지(JSON)
                Log.d("position check",mJsonString);

                test=new String[munje][8];
                correct=new String[munje];

                try {
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    int tt;
                    for (int i = 0; i < munje; i++) {
                        do {
                            tt = (int) (Math.random() * jsonArray.length()) + 1;
                        } while (r.contains(tt));
                        JSONObject item = jsonArray.getJSONObject(tt);
                        test[i][0]="Q"+(i+1);
                        //test[i][0]="Q"+item.getString("qno");
                        test[i][1]=item.getString("question");
                        test[i][2]=item.getString("bogi1");
                        test[i][3]=item.getString("bogi2");
                        test[i][4]=item.getString("bogi3");
                        test[i][5]=item.getString("bogi4");
                        test[i][6]=item.getString("ans");
                        test[i][7]="no-image";
                        correct[i]="";

                        CustomDTO dto = new CustomDTO();
                        dto.setTitle((i+1)+item.getString("question").trim().substring(item.getString("question").trim().indexOf(".")));
                        dto.setQnum((i+1)+". ");
                        //dto.setQnum(item.getString("qno")+". ");
                        dto.setBogi1(item.getString("bogi1").trim());
                        dto.setBogi2(item.getString("bogi2").trim());
                        dto.setBogi3(item.getString("bogi3").trim());
                        dto.setBogi4(item.getString("bogi4").trim());
                        dto.setCk1(View.INVISIBLE);
                        dto.setCk2(View.INVISIBLE);
                        dto.setCk3(View.INVISIBLE);
                        dto.setCk4(View.INVISIBLE);
                        CustomDTO dto2 = new CustomDTO();
                        dto2.setTitle((i+1)+". ");
                        //dto2.setTitle(item.getString("qno")+". ");
                        dto2.setContent("");

                        if (item.getString("img").contains("O")) {
                            back t = new back();
                            Bitmap tmp=t.execute("http://54.180.159.44/"+user_test+"/"+item.getString("year")+"/"+item.getString("year_no")+"/"+item.getString("qno")+".png").get();
                            //Bitmap tmp=t.execute("http://54.180.159.44/"+user_test+"/"+year+"/"+year_no+"/"+item.getString("qno")+".png").get();
                            int width=tmp.getWidth();
                            int height=tmp.getHeight();
                            tmp=Bitmap.createScaledBitmap(tmp,exam.getWidth()/4*3,height*exam.getWidth()/width/4*3,true);
                            dto.setImg(tmp);
                        }

                        adapter1.addItem(dto);
                        adapter2.addItem(dto2);
                    }
                    exam.setAdapter(adapter1);
                    cor.setAdapter(adapter2);

                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                progressDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String serverURL = "http://54.180.159.44/test2222.php";
            String postParameters = "q_name=" + searchKeyword1;

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

}
