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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import static com.sundosoft.qbig.SelectExam.setListViewHeightBasedOnChildren;

public class SelectActivity extends AppCompatActivity {
    ListView test_list;
    private CustomAdapter4 adapter;
    ImageView non1;
    TextView non2;

    Intent pintent;
    String user_email, user_test, mode;
    int year, year_no;
    String[][] user_year_list;

    public String mJsonString="";
    private static final String TAG_JSON="webnautes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        test_list=(ListView)findViewById(R.id.test_list);
        non1=(ImageView)findViewById(R.id.non1);
        non2=(TextView)findViewById(R.id.non2);

        adapter = new CustomAdapter4();

        pintent=getIntent();
        user_email=pintent.getExtras().getString("email");
        user_test=pintent.getExtras().getString("test");
        mode=pintent.getExtras().getString("mode");

        GetSelect2 task2 = new GetSelect2();
        task2.execute(user_test,user_email);

        GetSelect task = new GetSelect();
        task.execute(user_test);

        test_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                year=((CustomDTO)adapter.getItem(position)).getCk1();
                year_no=((CustomDTO)adapter.getItem(position)).getCk2();
                TestStartDialog();
            }
        });
    }

    public void TestStartDialog() {
        View dlgView = View.inflate(this,R.layout.dialog_login,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok,cancel,text;

        text = (TextView)dlgView.findViewById(R.id.text);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
        text.setText(user_test+" "+year+"년도 "+year_no+"회차로\n테스트를 시작할까요?");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("1")) {
                    Intent intent = new Intent(SelectActivity.this, TestActivity2.class);
                    intent.putExtra("email", user_email);
                    intent.putExtra("test", user_test);
                    intent.putExtra("year", year + "");
                    intent.putExtra("yearno", year_no + "");
                    intent.putExtra("mode","1");
                    finish();
                    startActivity(intent);
                    dlg.dismiss();
                }
                else {
                    Intent intent = new Intent(SelectActivity.this, TestActivity2.class);
                    intent.putExtra("email", user_email);
                    intent.putExtra("test", user_test);
                    intent.putExtra("year", year + "");
                    intent.putExtra("yearno", year_no + "");
                    intent.putExtra("ck1",pintent.getExtras().getBoolean("ck1"));
                    intent.putExtra("ck2",pintent.getExtras().getBoolean("ck2"));
                    intent.putExtra("ck3",pintent.getExtras().getBoolean("ck3"));
                    intent.putExtra("ck4",pintent.getExtras().getBoolean("ck4"));
                    intent.putExtra("ck5",pintent.getExtras().getBoolean("ck5"));
                    intent.putExtra("mode","2");
                    finish();
                    startActivity(intent);
                    dlg.dismiss();
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

    public class GetSelect extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mJsonString=result;
                Log.d("position check", "succ");
                try {
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    if (jsonArray.length()!=0) {
                        non1.setVisibility(View.GONE);
                        non2.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String year1 = item.getString("year");
                            String year_no1 = item.getString("year_no");

                            CustomDTO dto = new CustomDTO();
                            dto.setTitle(user_test+" "+year1+"년도 "+year_no1+"회차");
                            dto.setCk1(Integer.parseInt(year1));
                            dto.setCk2(Integer.parseInt(year_no1));
                            dto.setIsStudy("date");
                            adapter.addItem(dto);
                        }
                        test_list.setAdapter(adapter);

                    }

                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String serverURL = "http://54.180.159.44/select.php";
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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


    public class GetSelect2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mJsonString=result;
                Log.d("position check", "succ");
                try {
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    if (jsonArray.length()!=0) {
                        user_year_list = new String[jsonArray.length()][2];
                        non1.setVisibility(View.GONE);
                        non2.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            user_year_list[i][0] = item.getString("year");
                            user_year_list[i][1] = item.getString("year_no");
                        }
                    }

                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String serverURL = "http://54.180.159.44/select2.php";
            String postParameters = "q_name=" + searchKeyword1 + "&email=" + searchKeyword2;

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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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

    public void back(View view) {
        finish();
    }

    public void Mode2Dialog() {
        View dlgView = View.inflate(this,R.layout.dialog_mode2,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        CheckBox ck1, ck2, ck3, ck4, ck5;
        TextView ok_bt;

        ck1=(CheckBox)dlgView.findViewById(R.id.ck1);
        ck2=(CheckBox)dlgView.findViewById(R.id.ck2);
        ck3=(CheckBox)dlgView.findViewById(R.id.ck3);
        ck4=(CheckBox)dlgView.findViewById(R.id.ck4);
        ck5=(CheckBox)dlgView.findViewById(R.id.ck5);
        ok_bt=(TextView)dlgView.findViewById(R.id.ok_bt);

        ok_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        dlg.show();
    }
}
