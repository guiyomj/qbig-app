package com.sundosoft.qbig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private BackPressHandler backPressHandler;
    private BottomNavigationView botNavi;
    private ViewPager viewPager;
    private Class_ViewAdapter vocAdapter;
    private MenuItem prevMenuItem;

    TextView exam;
    public String user_email, user_name, user_test, uid;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    public String mJsonString="";
    public static Context context;
    public Boolean isPush, isPush2, isCk;
    public String cor, inc;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_1:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_2:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_3:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_4:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("user");
        backPressHandler = new BackPressHandler(this);
        exam=(TextView)findViewById(R.id.exam_name);

        final FirebaseUser user;
        user = mAuth.getCurrentUser();
        uid=user.getUid();
        user_email=user.getEmail();
        //user_name=user.getDisplayName();
        isPush = loadData("val");
        isPush2 = loadData("val2");
        isCk = loadData("ck");

        ExamGet2 task2 = new ExamGet2();
        task2.execute(user_email);

        GetExam task = new GetExam();
        task.execute("","","");

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        botNavi = (BottomNavigationView) findViewById(R.id.botnavi);
        botNavi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        vocAdapter = new Class_ViewAdapter(getSupportFragmentManager());
        vocAdapter.AddFragment(new Fragment_1(),"frag1");
        vocAdapter.AddFragment(new Fragment_2(),"frag2");
        vocAdapter.AddFragment(new Fragment_3(),"freg3");
        vocAdapter.AddFragment(new Fragment_4(),"freg4");

        viewPager.setAdapter(vocAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    botNavi.getMenu().getItem(0).setChecked(false);

                botNavi.getMenu().getItem(i).setChecked(true);
                prevMenuItem = botNavi.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        if (!isCk) NoticeDialog();
    }

    @Override public void onBackPressed() {
        backPressHandler.onBackPressed();
    }

    public void SelectExam(View view) {
        Intent examIntent = new Intent(getApplicationContext(), SelectExam.class);
        examIntent.putExtra("uid",uid);
        examIntent.putExtra("email",user_email);
        startActivity(examIntent);
    }

    public void gotoStudy() {
        viewPager.setCurrentItem(1);
    }

    /*
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    */

    public void PushClick(View view) {
        Intent setIntent = new Intent(getApplicationContext(), SettingActivity.class);
        setIntent.putExtra("email",user_email);
        startActivity(setIntent);
    }

    public void googlesignOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public void googlerevokeAccess() {
        mAuth.getCurrentUser().delete();
    }

    public void emailchangeps(String ps) {
        mAuth.getCurrentUser().updatePassword(ps);
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib") && !s.equals("files")){
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public void finishMain() {
        finish();
    }

    @Override
    public void onResume() {
        isCk = loadData("ck");

        ExamGet2 task2 = new ExamGet2();
        task2.execute(user_email);
/*
        dbRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("email").getValue().toString().equals(user_email))
                    user_test=dataSnapshot.child("current_test").getValue().toString();
                user_name=dataSnapshot.child("name").getValue().toString();

                if (user_test!=null && user_test.length()>0) {
                    exam.setText(user_test);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        super.onResume();
    }

    public class GetExam extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mJsonString = result;      // 서버로부터 받은 메시지(JSON)
                Log.d("position check",mJsonString);
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

    public class ExamGet2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("webnautes");
                    JSONObject item = jsonArray.getJSONObject(0);
                    user_name = item.getString("name");
                    user_test = item.getString("test");

                    if (user_test!=null && user_test.length()>0) {
                        exam.setText(user_test);
                    }
                } catch (JSONException e) {
                    Log.d("TAG", "showResult : ", e);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String serverURL = "http://54.180.159.44/user_exam2.php";
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

    public void saveData(String str, Boolean val){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(str, val);
        editor.apply();
    }

    public Boolean loadData(String str) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        return sharedPreferences.getBoolean(str, false);
    }

    public void NoticeDialog() {
        View dlgView = View.inflate(this,R.layout.dialog_notice,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok;
        final CheckBox ck;

        ck = (CheckBox)dlgView.findViewById(R.id.ck);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ck.isChecked()) {
                    saveData("ck", true);
                }
                else {
                    saveData("ck", false);
                }
                dlg.dismiss();
            }
        });

        dlg.show();
    }
}
