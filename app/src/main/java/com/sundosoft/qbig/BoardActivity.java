package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
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

public class BoardActivity extends AppCompatActivity {
    Intent pintent;
    ImageView write, non1;
    TextView title, non2;
    String t, user_email, user_name;
    private BoardAdapter adapter;

    String mJsonString="";
    private static final String TAG_JSON="webnautes";

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        non1=(ImageView)findViewById(R.id.non1);
        non2=(TextView)findViewById(R.id.non2);
        list=(ListView)findViewById(R.id.list);
        write = (ImageView)findViewById(R.id.write);

        pintent=getIntent();
        t=pintent.getExtras().getString("title");
        user_email=pintent.getExtras().getString("email");
        user_name=pintent.getExtras().getString("name");

        title = (TextView)findViewById(R.id.exam_name);
        title.setText(t);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, WriteActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("test",t);
                intent.putExtra("name",user_name);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BoardActivity.this, Board2Activity.class);
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

    public class BoardGet extends AsyncTask<String, Void, String> {
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
                    }

                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String serverURL = "http://54.180.159.44/board.php";
            String postParameters = "b_name=" + searchKeyword1;

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
        BoardGet task = new BoardGet();
        task.execute(t);

        super.onResume();
    }

    public void back(View view) {
        finish();
    }
}
