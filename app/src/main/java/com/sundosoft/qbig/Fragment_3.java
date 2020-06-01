package com.sundosoft.qbig;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sundosoft.qbig.SelectExam.setListViewHeightBasedOnChildren;


public class Fragment_3 extends Fragment {
    View view;
    private CustomAdapter adapter1,adapter2;
    private ListView mylistView,yourlistView;
    LinearLayout menu1, menu2, menu3;
    String user_email, user_test, user_name;
    private static final String TAG_JSON="webnautes";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_3, container, false);
        user_email=((MainActivity)getActivity()).user_email;
        user_name=((MainActivity)getActivity()).user_name;
        user_test=((MainActivity)getActivity()).user_test;

        mylistView = (ListView)view.findViewById(R.id.myboard);
        yourlistView = (ListView)view.findViewById(R.id.board);

        menu1 = (LinearLayout)view.findViewById(R.id.menu1);
        menu2 = (LinearLayout)view.findViewById(R.id.menu2);
        menu3 = (LinearLayout)view.findViewById(R.id.menu3);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Board3Activity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("name",user_name);
                intent.putExtra("title","내가 쓴 글");
                startActivity(intent);
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Board3Activity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("name",user_name);
                intent.putExtra("title","댓글단 글");
                startActivity(intent);
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Board3Activity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("name",user_name);
                intent.putExtra("title","좋아요한 글");
                startActivity(intent);
            }
        });

        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), BoardActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("name",user_name);
                intent.putExtra("title",((CustomDTO)adapter1.getItem(position)).getTitle());
                startActivity(intent);
            }
        });

        yourlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), BoardActivity.class);
                intent.putExtra("email",user_email);
                intent.putExtra("name",user_name);
                intent.putExtra("title",((CustomDTO)adapter2.getItem(position)).getTitle());
                startActivity(intent);
            }
        });
        //SetAdt();

        return view;
    }

    @Override
    public void onResume() {
        user_email=((MainActivity)getActivity()).user_email;
        user_test=((MainActivity)getActivity()).user_test;
        user_name=((MainActivity)getActivity()).user_name;
        SetAdt();

        super.onResume();
    }

    private void SetAdt() {
        adapter1 = new CustomAdapter();
        adapter2 = new CustomAdapter();
        CustomDTO free = new CustomDTO();
        free.setTitle("자유게시판");
        adapter1.addItem(free);

        if (user_test!=null && user_test.length()>1 ) {
            CustomDTO my = new CustomDTO();
            my.setTitle(user_test);
            adapter1.addItem(my);
        }

        try {
            JSONObject jsonObject = new JSONObject(((MainActivity)getActivity()).mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                CustomDTO dto = new CustomDTO();
                JSONObject item = jsonArray.getJSONObject(i);
                String e_name = item.getString("e_name");
                dto.setTitle(e_name);
                adapter2.addItem(dto);
            }

        } catch (JSONException e) {
            Log.d("TAG", "showResult : ", e);
        }

        setListViewHeightBasedOnChildren(mylistView);
        setListViewHeightBasedOnChildren(yourlistView);

        mylistView.setAdapter(adapter1);
        yourlistView.setAdapter(adapter2);

    }

}
