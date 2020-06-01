package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sundosoft.qbig.SelectExam.setListViewHeightBasedOnChildren;

public class ReviewActivity extends AppCompatActivity {
    ImageView non1;
    TextView non2;
    ScrollView is;
    ListView list1, list2;

    String user_email, user_test;
    Intent pintent;
    private CustomAdapter2 adapter, adapter2;
    String mJsonString="";
    String remain, year, yearno, date;

    Date d = new Date(System.currentTimeMillis());
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    String today = sf.format(d);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        non1=(ImageView)findViewById(R.id.non1);
        non2=(TextView)findViewById(R.id.non2);
        list1=(ListView)findViewById(R.id.list1);
        list2=(ListView)findViewById(R.id.list2);
        is=(ScrollView)findViewById(R.id.is);

        pintent=getIntent();
        user_email=pintent.getExtras().getString("email");
        user_test=pintent.getExtras().getString("test");

        adapter = new CustomAdapter2();
        adapter2 = new CustomAdapter2();
        GetData a = new GetData();
        a.execute(user_email,user_test);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                remain=((CustomDTO)adapter.getItem(position)).getDate().substring(0,((CustomDTO)adapter.getItem(position)).getDate().indexOf("개"));
                year=((CustomDTO)adapter.getItem(position)).getBogi1();
                yearno=((CustomDTO)adapter.getItem(position)).getBogi2();
                date=((CustomDTO)adapter.getItem(position)).getBogi3();

                if (!remain.equals("0")) ReviewDialog();
                else Toast.makeText(ReviewActivity.this, "이미 오답 풀이를 완료한 테스트입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                remain=((CustomDTO)adapter2.getItem(position)).getDate().substring(0,((CustomDTO)adapter2.getItem(position)).getDate().indexOf("개"));
                year=((CustomDTO)adapter.getItem(position)).getBogi1();
                yearno=((CustomDTO)adapter.getItem(position)).getBogi2();
                date=((CustomDTO)adapter.getItem(position)).getBogi3();

                if (!remain.equals("0")) ReviewDialog();
                else Toast.makeText(ReviewActivity.this, "이미 오답 풀이를 완료한 테스트입니다.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mJsonString = result;      // 서버로부터 받은 메시지(JSON)
            Log.d("position check",result);

            if (result != null) {

                try {
                    non1.setVisibility(View.GONE);
                    non2.setVisibility(View.GONE);
                    is.setVisibility(View.VISIBLE);

                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String title = item.getString("q_name")+" "+item.getString("year")+"년도 "+item.getString("no")+"회차";
                        String date = item.getString("wrong")+"개 남음";

                        CustomDTO dto = new CustomDTO();
                        dto.setTitle(title);
                        dto.setDate(date);
                        dto.setBogi1(item.getString("year"));
                        dto.setBogi2(item.getString("no"));
                        dto.setBogi3(item.getString("date"));

                        if (item.getString("date").equals(today))
                            adapter.addItem(dto);
                        else adapter2.addItem(dto);
                    }

                    list1.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(list1);
                    list2.setAdapter(adapter2);
                    setListViewHeightBasedOnChildren(list2);

                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String serverURL = "http://54.180.159.44/review.php";
            String postParameters = "email=" + searchKeyword1 + "&q_name=" + searchKeyword2;

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

    public void ReviewDialog() {
        View dlgView = View.inflate(this,R.layout.dialog_login,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok,cancel,text;

        text = (TextView)dlgView.findViewById(R.id.text);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
        text.setText("오답 풀이를 시작할까요?\n남은 문제는 "+remain+"개 입니다.");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, TestActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("test",user_test);
                intent.putExtra("year",year);
                intent.putExtra("yearno",yearno);
                intent.putExtra("date",date);
                startActivity(intent);

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

    public void back(View view) {
        finish();
    }
}
