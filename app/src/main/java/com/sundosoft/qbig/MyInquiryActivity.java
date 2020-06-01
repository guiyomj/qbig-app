package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class MyInquiryActivity extends AppCompatActivity {
    ImageView non1;
    TextView non2;
    ListView inquery_list;

    Intent pintent;
    private CustomAdapter2 adapter;

    public String mJsonString="";
    private static final String TAG_JSON="webnautes";
    String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inquiry);

        non1=(ImageView)findViewById(R.id.non1);
        non2=(TextView)findViewById(R.id.non2);
        inquery_list=(ListView)findViewById(R.id.exam);

        adapter = new CustomAdapter2();

        pintent = getIntent();
        user_email=pintent.getExtras().getString("email");

        GetInquery task=new GetInquery();
        task.execute(user_email);
    }

    public class GetInquery extends AsyncTask<String, Void, String> {
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
                            String content = item.getString("content");
                            String date = item.getString("date");

                            CustomDTO dto = new CustomDTO();
                            dto.setTitle(content);
                            dto.setDate(date);
                            adapter.addItem(dto);
                        }
                        inquery_list.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(inquery_list);

                    }

                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String serverURL = "http://54.180.159.44/my_inquery.php";
            String postParameters = "email=" + searchKeyword1;

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
}
