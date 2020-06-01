package com.sundosoft.qbig;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CustomViewHolder holder;

        if (1 == 1) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_comment, null, false);

            holder = new CustomViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.delete=(ImageView) convertView.findViewById(R.id.delete);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        final CustomDTO dto = listCustom.get(position);
        holder.content.setText(dto.getContent());
        holder.date.setText(dto.getDate());
        holder.name.setText(dto.getName());
        holder.delete.setVisibility(dto.getCk1());
        holder.img.setImageBitmap(dto.getImg());

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }});

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Board2Activity)Board2Activity.context).DeleteDialog(dto.getCk2());
            }
        });

        return convertView;
    }

    class CustomViewHolder {
        ImageView img;

        TextView content, name, date;
        ImageView delete;
    }

    public void addItem(CustomDTO dto) {
        listCustom.add(dto);
    }
}
