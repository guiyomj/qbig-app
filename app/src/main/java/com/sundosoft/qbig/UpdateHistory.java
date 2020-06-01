package com.sundosoft.qbig;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateHistory extends AsyncTask<String, Void, String> {
    Date d = new Date(System.currentTimeMillis());
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    String today = sf.format(d);

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            Log.d("position check",result);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String searchKeyword1 = params[0];
        String searchKeyword2 = params[1];
        String searchKeyword3 = params[2];
        String searchKeyword4 = params[3];
        String searchKeyword5 = params[4];
        String searchKeyword6 = params[5];
        String searchKeyword7 = params[6];
        String serverURL = "http://54.180.159.44/history_upload.php";
        String postParameters = "email=" + searchKeyword1 + "&q_name=" + searchKeyword2 + "&pre_question_year=" + searchKeyword3 + "&pre_question_year_no=" + searchKeyword4 + "&question_count=" + searchKeyword5 + "&correct_count=" + searchKeyword6 + "&wrong_count=" + searchKeyword7;

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