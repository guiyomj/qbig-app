package com.sundosoft.qbig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import java.util.List;

import static com.sundosoft.qbig.SelectExam.setListViewHeightBasedOnChildren;

public class Login_Email extends AppCompatActivity {
    CheckBox yes;
    TextView signin;
    EditText pass, pass2, id, name;
    List email_list;
    FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__email);

        dbRef = FirebaseDatabase.getInstance().getReference("user");
        firebaseAuth = FirebaseAuth.getInstance();

        pass=(EditText)findViewById(R.id.pass);
        pass2=(EditText)findViewById(R.id.pass2);
        id=(EditText)findViewById(R.id.email);
        name=(EditText)findViewById(R.id.name);
        yes=(CheckBox)findViewById(R.id.checkBox);
        signin=(TextView)findViewById(R.id.signin);
        //email_list = new ArrayList();
        EmailSelect es = new EmailSelect();
        es.execute();

        /*
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    email_list.add(snapshot.child("email").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("히히"+email_list);
                if (id.getText().toString().contains("@")&&id.getText().toString().contains(".")&&!email_list.contains(id.getText().toString())) {
                    id.getBackground().setColorFilter(getResources().getColor(R.color.colorYes), PorterDuff.Mode.SRC_ATOP);
                }
                else if (id.getText().toString().equals("")) {
                    id.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    id.getBackground().setColorFilter(getResources().getColor(R.color.colorNo), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pass.getText().toString().equals(pass2.getText().toString())&&pass.getText().toString().length()>=8) {
                    pass.getBackground().setColorFilter(getResources().getColor(R.color.colorYes), PorterDuff.Mode.SRC_ATOP);
                    pass2.getBackground().setColorFilter(getResources().getColor(R.color.colorYes), PorterDuff.Mode.SRC_ATOP);
                }
                else if (pass.getText().equals("")||pass2.getText().equals("")) {
                    pass.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    pass.getBackground().setColorFilter(getResources().getColor(R.color.colorNo), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pass.getText().toString().equals(pass2.getText().toString())&&pass.getText().toString().length()>=8) {
                    pass.getBackground().setColorFilter(getResources().getColor(R.color.colorYes), PorterDuff.Mode.SRC_ATOP);
                    pass2.getBackground().setColorFilter(getResources().getColor(R.color.colorYes), PorterDuff.Mode.SRC_ATOP);
                }
                else if (pass.getText().equals("")||pass2.getText().equals("")) {
                    pass2.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    pass2.getBackground().setColorFilter(getResources().getColor(R.color.colorNo), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yes.isChecked()!=true) {
                    Toast.makeText(Login_Email.this, "회원가입 동의에 체크를 하셔야 가입이 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                else if (email_list.contains(id.getText().toString())) {
                    Toast.makeText(Login_Email.this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (pass.getText().equals(pass)) {
                    Toast.makeText(Login_Email.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (name.getText().toString().length()>6 && name.getText().equals("")) {
                    Toast.makeText(Login_Email.this, "닉네임을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(id.getText().toString(),pass.getText().toString());
                    Toast.makeText(Login_Email.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void registerUser(final String email, final String password){
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            dbRef.child(user.getUid()).child("email").setValue(email);
                            //dbRef.child(user.getUid()).child("name").setValue(name.getText().toString());
                            //dbRef.child(user.getUid()).child("current_test").setValue("");
                            SignIn task2 = new SignIn();
                            task2.execute(email,name.getText().toString());

                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });

    }

    public void back(View view) {
        finish();
    }

    public class EmailSelect extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    email_list = new ArrayList();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("webnautes");

                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            email_list.add(item.getString("email"));
                        }
                    }

                } catch (JSONException e) {
                    Log.d("TAG", "InsertData: Error ", e);
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://54.180.159.44/email_select.php";
            String postParameters = "";

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
}
