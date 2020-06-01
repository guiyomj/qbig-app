package com.sundosoft.qbig;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler, null, false);

            holder = new CustomViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.no = (TextView) convertView.findViewById(R.id.cnt);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        CustomDTO dto = listCustom.get(position);

        holder.title.setText(dto.getTitle());
        holder.content.setText(dto.getContent());
        holder.date.setText(dto.getDate());
        holder.name.setText(dto.getName());
        holder.no.setText(dto.getQnum());

        return convertView;
    }

    class CustomViewHolder {
        TextView title, content, date, name, no;
    }

    public void addItem(CustomDTO dto) {
        listCustom.add(dto);
    }
}
