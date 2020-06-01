package com.sundosoft.qbig;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Fragment_2 extends Fragment {
    View view;
    ConstraintLayout study1, study2;
    String user_email, user_test;
    Button btn, btn2;
    TextView cc;
    public String cor, inc;

    Date d = new Date(System.currentTimeMillis());
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    String today = sf.format(d);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        user_email=((MainActivity)getActivity()).user_email;
        user_test=((MainActivity)getActivity()).user_test;

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_2, container, false);
        study1 = (ConstraintLayout)view.findViewById(R.id.study1);
        study2 = (ConstraintLayout)view.findViewById(R.id.study2);
        btn=(Button)view.findViewById(R.id.btn);
        btn2=(Button)view.findViewById(R.id.btn2);
        cc=(TextView)view.findViewById(R.id.cc);

        //user_email=((MainActivity)getActivity()).user_email;
        //user_test=((MainActivity)getActivity()).user_test;

        study1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeDialog();
            }
        });

        study2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeDialog2();
            }
        });

        Frag2 f = new Frag2();
        f.execute(user_email,user_test,today);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email=((MainActivity)getActivity()).user_email;
                user_test=((MainActivity)getActivity()).user_test;

                Intent intent = new Intent(view.getContext(), ReviewActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("test",user_test);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email=((MainActivity)getActivity()).user_email;
                user_test=((MainActivity)getActivity()).user_test;

                Toast.makeText(view.getContext(),"준비중입니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), SelectActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("test",user_test);
                //startActivity(intent);
            }
        });

        return view;
    }

    public class Frag2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.d("TAG", "aaaa : "+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("webnautes");
                    JSONObject item = jsonArray.getJSONObject(0);
                    cor = item.getString("cor");
                    inc = item.getString("inc");

                    cc.setText(cor+"개\n"+inc+"개");
                } catch (JSONException e) {
                    cor="0";
                    inc="0";
                    Log.d("TAG", "showResult : ", e);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];
            String serverURL = "http://54.180.159.44/frag2.php";
            String postParameters = "email=" + searchKeyword1 + "&q_name=" + searchKeyword2 + "&date=" + searchKeyword3;

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

    public void ModeDialog() {
        View dlgView = View.inflate(view.getContext(),R.layout.dialog_mode,null);
        final Dialog dlg = new Dialog(view.getContext());
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        ImageView exit;
        LinearLayout mode1, mode2;

        user_email=((MainActivity)getActivity()).user_email;
        user_test=((MainActivity)getActivity()).user_test;

        mode1 = (LinearLayout)dlgView.findViewById(R.id.mode1);
        mode2 = (LinearLayout)dlgView.findViewById(R.id.mode2);

        mode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(), "mode 1", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), SelectActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("test",user_test);
                intent.putExtra("mode","1");
                startActivity(intent);
                dlg.dismiss();
            }
        });

        mode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(view.getContext(), TestActivity2.class);
                //intent.putExtra("email",user_email);
                //intent.putExtra("test",user_test);
                //intent.putExtra("mode","2");
                //startActivity(intent);
                //dlg.dismiss();
            }
        });

        dlg.show();
    }

    public void ModeDialog2() {
        View dlgView = View.inflate(view.getContext(),R.layout.dialog_mode2,null);
        final Dialog dlg = new Dialog(view.getContext());
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        final CheckBox ck1, ck2, ck3 ,ck4, ck5;
        TextView ok_bt;

        user_email=((MainActivity)getActivity()).user_email;
        user_test=((MainActivity)getActivity()).user_test;

        ck1 = (CheckBox)dlgView.findViewById(R.id.ck1);
        ck2 = (CheckBox)dlgView.findViewById(R.id.ck2);
        ck3 = (CheckBox)dlgView.findViewById(R.id.ck3);
        ck4 = (CheckBox)dlgView.findViewById(R.id.ck4);
        ck5 = (CheckBox)dlgView.findViewById(R.id.ck5);
        ok_bt = (TextView)dlgView.findViewById(R.id.ok_bt);

        ok_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ck1.isChecked() && !ck2.isChecked() && !ck3.isChecked() && !ck4.isChecked() && !ck5.isChecked()) {
                    Toast.makeText(view.getContext(), "과목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(view.getContext(), TestActivity2.class);
                    intent.putExtra("email", user_email);
                    intent.putExtra("test", user_test);
                    intent.putExtra("mode", "2");
                    intent.putExtra("ck1", ck1.isChecked());
                    intent.putExtra("ck2", ck2.isChecked());
                    intent.putExtra("ck3", ck3.isChecked());
                    intent.putExtra("ck4", ck4.isChecked());
                    intent.putExtra("ck5", ck5.isChecked());
                    startActivity(intent);
                    dlg.dismiss();
                }
            }
        });

        dlg.show();
    }

    @Override
    public void onResume() {
        user_email=((MainActivity)getActivity()).user_email;
        user_test=((MainActivity)getActivity()).user_test;

        super.onResume();
    }
}
