package com.sundosoft.qbig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CorAdapter extends BaseAdapter {
    public ArrayList<CustomDTO> listCustom = new ArrayList<>();

    @Override
    public int getCount() {
        return listCustom.size();
    }

    @Override
    public Object getItem(int position) {
        return listCustom.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;

        if (1 == 1) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_exam2, parent, false);

            holder = new CustomViewHolder();
            holder.textTitle = (TextView) convertView.findViewById(R.id.title);
            holder.textContent = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        CustomDTO dto = listCustom.get(position);
        holder.textTitle.setText(dto.getTitle());
        holder.textContent.setText(dto.getContent());

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }});


        return convertView;
    }

    class CustomViewHolder {
        TextView textTitle, textContent;
    }

    public void addItem(CustomDTO dto) {
        listCustom.add(dto);
    }
}
