package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class NoticeActivity extends AppCompatActivity {
    String mJsonString="";
    private static final String TAG_JSON="webnautes";
    ListView notice;
    private CustomAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        adapter = new CustomAdapter2();
        notice=(ListView)findViewById(R.id.notice);

        GetData task = new GetData();
        task.execute("","","");

        notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    Intent intent = new Intent(NoticeActivity.this, NoticeActivity2.class);

                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    JSONObject item = jsonArray.getJSONObject(position);
                    String title = item.getString("title");
                    String content = item.getString("content");
                    String date = item.getString("date");

                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    intent.putExtra("date", date);
                    startActivity(intent);
                } catch(JSONException e) {}
            }
        });
    }

    public void back(View v) {
        finish();
    }

    private class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mJsonString = result;      // 서버로부터 받은 메시지(JSON)
                Log.d("position check",mJsonString);
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            String serverURL = "http://54.180.159.44/notice.php";
            String postParameters = "category=" + searchKeyword1 + "&class=" + searchKeyword2 + "&name=" + searchKeyword3;

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

    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String title = item.getString("title");
                String content = item.getString("content");
                String date = item.getString("date");

                CustomDTO dto = new CustomDTO();
                dto.setTitle(title);
                //dto.setContent(content);
                dto.setDate(date);
                adapter.addItem(dto);
            }

            notice.setAdapter(adapter);
            setListViewHeightBasedOnChildren(notice);

        } catch (JSONException e) {
            Log.d("TAG", "showResult : ", e);
        }
    }

}
