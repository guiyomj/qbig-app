package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class Board3Activity extends AppCompatActivity {
    Intent pintent;
    TextView title, non2;
    ImageView non1;
    String user_email, t, user_name;
    private BoardAdapter adapter;
    ListView list;

    String mJsonString="";
    private static final String TAG_JSON="webnautes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inquiry);

        title = (TextView)findViewById(R.id.title);
        list=(ListView)findViewById(R.id.exam);
        non1=(ImageView)findViewById(R.id.non1);
        non2=(TextView)findViewById(R.id.non2);

        pintent = getIntent();
        user_email=pintent.getExtras().getString("email");
        t=pintent.getExtras().getString("title");
        user_name=pintent.getExtras().getString("name");
        title.setText(t);

        adapter = new BoardAdapter();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Board3Activity.this, Board2Activity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("uname",user_name);

                intent.putExtra("test",t);
                intent.putExtra("title",((CustomDTO)adapter.getItem(position)).getTitle());
                intent.putExtra("content",((CustomDTO)adapter.getItem(position)).getContent());
                intent.putExtra("date",((CustomDTO)adapter.getItem(position)).getDate());
                intent.putExtra("name",((CustomDTO)adapter.getItem(position)).getName());
                intent.putExtra("no",((CustomDTO)adapter.getItem(position)).getCk1());
                intent.putExtra("likes",((CustomDTO)adapter.getItem(position)).getQnum());
                intent.putExtra("b_email",((CustomDTO)adapter.getItem(position)).getBogi1());
                startActivity(intent);
            }
        });
    }
    public class MyGet extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mJsonString=result;
                Log.d("position check",mJsonString);

                try {
                    adapter = new BoardAdapter();
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    if (jsonArray.length()!=0) {
                        non1.setVisibility(View.GONE);
                        non2.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            CustomDTO my = new CustomDTO();
                            my.setBogi1(item.getString("email"));
                            my.setTitle(item.getString("title"));
                            my.setContent(item.getString("content"));
                            my.setDate(item.getString("date"));
                            my.setName(item.getString("name"));
                            my.setQnum(item.getString("likes"));
                            my.setCk1(item.getInt("no"));
                            adapter.addItem(my);
                        }
                        list.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(list);
                    }

                } catch (JSONException e) {
                    non1.setVisibility(View.VISIBLE);
                    non2.setVisibility(View.VISIBLE);

                    adapter = new BoardAdapter();
                    list.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(list);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String str = params[1];
            String serverURL = "http://54.180.159.44/board"+str+".php";
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

    @Override
    public void onResume() {
        if (t.contains("내가 쓴 글")) {
            MyGet task = new MyGet();
            task.execute(user_email,"1");
        }
        else if (t.contains("댓글단 글")) {
            MyGet task = new MyGet();
            task.execute(user_email,"2");
        }
        else {
            MyGet task = new MyGet();
            task.execute(user_email,"3");
        }

        super.onResume();
    }

    public void back(View v) {
        finish();
    }
}
