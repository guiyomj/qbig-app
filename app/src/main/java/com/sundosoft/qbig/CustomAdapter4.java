package com.sundosoft.qbig;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter4 extends BaseAdapter {
    private ArrayList<CustomDTO> listCustom = new ArrayList<>();

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
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_test_list, null, false);

            holder = new CustomViewHolder();
            holder.textTitle = (TextView) convertView.findViewById(R.id.title);
            holder.textDate = (TextView) convertView.findViewById(R.id.isStudy);


            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        CustomDTO dto = listCustom.get(position);

        holder.textTitle.setText(dto.getTitle());
        if (dto.getIsStudy().equals("")) {
            holder.textDate.setTextColor(Color.parseColor("#aaaaaa"));
        }
        //holder.textDate.setText(dto.getIsStudy());

        return convertView;
    }

    class CustomViewHolder {
        TextView textTitle;
        TextView textDate;
    }

    public void addItem(CustomDTO dto) {
        listCustom.add(dto);
    }
}
