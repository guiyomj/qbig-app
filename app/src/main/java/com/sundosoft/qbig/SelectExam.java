package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SelectExam extends AppCompatActivity {
    ListView it_list, elec_list, elec2_list, che_list, mec_list;
    ImageView search;
    String testName, user_email;
    ArrayList<String> it_name = new ArrayList<>();
    ArrayList<String> elec_name = new ArrayList<>();
    ArrayList<String> elec2_name = new ArrayList<>();
    ArrayList<String> che_name = new ArrayList<>();
    ArrayList<String> mec_name = new ArrayList<>();

    ArrayAdapter it_adapter, elec_adapter, elec2_adapter, che_adapter, mec_adapter;
    String mJsonString="";
    private static final String TAG_JSON="webnautes";
    private DatabaseReference dbRef;
    Intent pintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exam);
        dbRef = FirebaseDatabase.getInstance().getReference("user");
        pintent=getIntent();

        it_list = (ListView)findViewById(R.id.it_list);
        elec_list = (ListView)findViewById(R.id.elec_list);
        elec2_list = (ListView)findViewById(R.id.elec2_list);
        che_list = (ListView)findViewById(R.id.che_list);
        mec_list = (ListView)findViewById(R.id.mec_list);
        search = (ImageView)findViewById(R.id.search);
        user_email=pintent.getExtras().getString("email");

        GetData task = new GetData();
        task.execute("","","");

        it_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                testName=it_name.get(position);
                TestDialog();
            }
        });

        elec_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                testName=elec_name.get(position);
                TestDialog();
            }
        });

        elec2_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                testName=elec2_name.get(position);
                TestDialog();
            }
        });

        che_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                testName=che_name.get(position);
                TestDialog();
            }
        });

        mec_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                testName=mec_name.get(position);
                TestDialog();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    public void TestDialog() {
        View dlgView = View.inflate(this,R.layout.dialog_login,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok,cancel,text;

        text = (TextView)dlgView.findViewById(R.id.text);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
        text.setText(testName+"로\n학습할까요?");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExamGet etask = new ExamGet();
                etask.execute(user_email,testName);
                //dbRef.child(pintent.getExtras().getString("uid")).child("current_test").setValue(testName);
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
            String serverURL = "http://54.180.159.44/exam_list.php";
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
                String e_category = item.getString("e_category");
                String e_name = item.getString("e_name");
                if (e_category.equals("IT"))
                    it_name.add(e_name);
                else if (e_category.equals("전기"))
                    elec_name.add(e_name);
                else if (e_category.equals("전자"))
                    elec2_name.add(e_name);
                else if (e_category.equals("화학"))
                    che_name.add(e_name);
                else if (e_category.equals("기계"))
                    mec_name.add(e_name);
            }

            it_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, it_name);
            elec_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, elec_name);
            elec2_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, elec2_name);
            che_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, che_name);
            mec_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mec_name);

            it_list.setAdapter(it_adapter);
            elec_list.setAdapter(elec_adapter);
            elec2_list.setAdapter(elec2_adapter);
            che_list.setAdapter(che_adapter);
            mec_list.setAdapter(mec_adapter);

            setListViewHeightBasedOnChildren(it_list);
            setListViewHeightBasedOnChildren(elec_list);
            setListViewHeightBasedOnChildren(elec2_list);
            setListViewHeightBasedOnChildren(che_list);
            setListViewHeightBasedOnChildren(mec_list);

        } catch (JSONException e) {
            Log.d("TAG", "showResult : ", e);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}