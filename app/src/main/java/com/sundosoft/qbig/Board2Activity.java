package com.sundosoft.qbig;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.List;

import static com.sundosoft.qbig.SelectExam.setListViewHeightBasedOnChildren;

public class Board2Activity extends AppCompatActivity {
    Intent pintent;
    TextView name, date, title, content, like_num;
    String name_p, date_p, title_p, content_p, email_p;
    ImageView comment_write, photo, like, delete;
    EditText comment;
    LinearLayout likes;

    String user_email, user_name, user_test;
    int bno, like_p, user_like;

    public static Context context;

    ListView cmt_list;
    TextView cmt_text;

    private CommentAdapter adapter;

    String mJsonString="";
    private static final String TAG_JSON="webnautes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board2);

        name=(TextView)findViewById(R.id.name);
        date=(TextView)findViewById(R.id.date);
        title=(TextView)findViewById(R.id.title);
        content=(TextView)findViewById(R.id.content);
        comment=(EditText)findViewById(R.id.comment);
        cmt_list=(ListView)findViewById(R.id.cmt_list);
        comment_write=(ImageView)findViewById(R.id.write);
        cmt_text=(TextView)findViewById(R.id.cmt_text);
        like = (ImageView)findViewById(R.id.like);
        delete = (ImageView)findViewById(R.id.delete);
        likes = (LinearLayout)findViewById(R.id.likes);
        like_num = (TextView)findViewById(R.id.like_num);

        photo=(ImageView)findViewById(R.id.photo);
        photo.setBackground(new ShapeDrawable(new OvalShape()));
        photo.setClipToOutline(true);

        context=this;

        pintent=getIntent();
        name_p=pintent.getExtras().getString("name");
        date_p=pintent.getExtras().getString("date");
        title_p=pintent.getExtras().getString("title");
        content_p=pintent.getExtras().getString("content");
        user_email=pintent.getExtras().getString("email");
        user_name=pintent.getExtras().getString("uname");
        user_test=pintent.getExtras().getString("test");
        bno=pintent.getExtras().getInt("no");
        like_p=Integer.parseInt(pintent.getExtras().getString("likes"));
        email_p=pintent.getExtras().getString("b_email");

        name.setText(name_p);
        date.setText(date_p);
        title.setText(title_p);
        content.setText(content_p);
        System.out.println(like_p+"aaaa");
        like_num.setText(like_p+"");

        LikeSelect ls = new LikeSelect();
        ls.execute(user_email,bno+"");

        if (email_p.equals(user_email)) delete.setVisibility(View.VISIBLE);

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_like==0) {
                    like.setImageResource(R.drawable.ic_heartf);
                    LikeInsert l1 = new LikeInsert();
                    l1.execute(user_email,bno+"");
                    LikePlus lp = new LikePlus();
                    lp.execute(bno+"");
                    user_like++;

                    like_num.setText(++like_p+"");
                    Toast.makeText(Board2Activity.this,"좋아요가 설정되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else {
                    like.setImageResource(R.drawable.ic_heart);
                    LikeDelete l2 = new LikeDelete();
                    l2.execute(user_email,bno+"");
                    System.out.println("aaaa"+user_email+bno);
                    LikeMinus lm = new LikeMinus();
                    lm.execute(bno+"");
                    user_like--;

                    like_num.setText(--like_p+"");
                    Toast.makeText(Board2Activity.this,"좋아요가 해제되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        comment_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getText().toString().equals("")) {
                    Toast.makeText(Board2Activity.this,"댓글을 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    CommentWrite task = new CommentWrite();
                    task.execute(bno+"",user_email,user_name,comment.getText().toString());

                    CommentRead g = new CommentRead();
                    g.execute(bno+"");
                    comment.setText("");
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog2(bno);
            }
        });
    }

    @Override
    public void onResume() {
        CommentRead g = new CommentRead();
        g.execute(bno+"");

        super.onResume();
    }

    public class CommentRead extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mJsonString=result;
                Log.d("position check",mJsonString);

                try {
                    adapter = new CommentAdapter();

                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        CustomDTO my = new CustomDTO();
                        my.setContent(item.getString("content"));
                        my.setName(item.getString("user_name"));
                        my.setDate(item.getString("date"));
                        my.setCk2(item.getInt("cno"));
                        if (item.getString("email").equals(user_email)) {
                            my.setCk1(View.VISIBLE);
                        }
                        else {
                            my.setCk1(View.INVISIBLE);
                        }
                        adapter.addItem(my);
                    }
                    cmt_text.setText("총 "+jsonArray.length()+"개의 댓글이 있습니다");
                    cmt_list.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(cmt_list);

                } catch (JSONException e) {
                    adapter = new CommentAdapter();
                    cmt_text.setText("총 0개의 댓글이 있습니다");
                    cmt_list.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(cmt_list);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String serverURL = "http://54.180.159.44/comment.php";
            String postParameters = "bno=" + searchKeyword1;

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
            }
                return null;
        }
    }

    public class LikeSelect extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    JSONObject item = jsonArray.getJSONObject(0);
                    user_like=item.getInt("cnt");
                    Log.d("좋아요 여부", user_like+"");

                    if (user_like>0) {
                        like.setImageResource(R.drawable.ic_heartf);
                    }

                } catch (Exception e) {
                    Log.d("TAG", "InsertData: Error ", e);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String serverURL = "http://54.180.159.44/like_select.php";
            String postParameters = "email=" + searchKeyword1 + "&bno=" + searchKeyword2;

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


    public void DeleteDialog(final int cno) {
        View dlgView = View.inflate(this,R.layout.dialog_login,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok,cancel,text;

        text = (TextView)dlgView.findViewById(R.id.text);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
        text.setText("댓글을 삭제할까요?");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDelete task = new CommentDelete();
                task.execute(cno+"");
                dlg.dismiss();

                CommentRead g = new CommentRead();
                g.execute(bno+"");
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



    public void DeleteDialog2(final int bno) {
        View dlgView = View.inflate(this,R.layout.dialog_login,null);
        final Dialog dlg = new Dialog(this);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setContentView(dlgView);
        TextView ok,cancel,text;

        text = (TextView)dlgView.findViewById(R.id.text);
        ok = (TextView)dlgView.findViewById(R.id.ok_bt);
        cancel = (TextView)dlgView.findViewById(R.id.cancel_bt);
        text.setText("게시물을 삭제할까요?");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardDelete task = new BoardDelete();
                task.execute(bno+"");

                CommentDelete2 g = new CommentDelete2();
                g.execute(bno+"");

                dlg.dismiss();
                Toast.makeText(Board2Activity.this,"삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
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
