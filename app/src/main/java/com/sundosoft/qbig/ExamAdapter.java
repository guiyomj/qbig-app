package com.sundosoft.qbig;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExamAdapter extends BaseAdapter {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_exam, null, false);

            holder = new CustomViewHolder();
            holder.textTitle = (TextView) convertView.findViewById(R.id.title);
            holder.qnum = (TextView) convertView.findViewById(R.id.qnum);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.bogi1 = (TextView) convertView.findViewById(R.id.bogi1);
            holder.bogi2 = (TextView) convertView.findViewById(R.id.bogi2);
            holder.bogi3 = (TextView) convertView.findViewById(R.id.bogi3);
            holder.bogi4 = (TextView) convertView.findViewById(R.id.bogi4);
            holder.ck1=(ImageView) convertView.findViewById(R.id.ck1);
            holder.ck2=(ImageView) convertView.findViewById(R.id.ck2);
            holder.ck3=(ImageView) convertView.findViewById(R.id.ck3);
            holder.ck4=(ImageView) convertView.findViewById(R.id.ck4);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        CustomDTO dto = listCustom.get(position);
        holder.textTitle.setText(dto.getTitle());
        holder.qnum.setText(dto.getDate());
        holder.bogi1.setText(dto.getBogi1());
        holder.bogi2.setText(dto.getBogi2());
        holder.bogi3.setText(dto.getBogi3());
        holder.bogi4.setText(dto.getBogi4());
        holder.ck1.setVisibility(dto.getCk1());
        holder.ck2.setVisibility(dto.getCk2());
        holder.ck3.setVisibility(dto.getCk3());
        holder.ck4.setVisibility(dto.getCk4());
        holder.img.setImageBitmap(dto.getImg());

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }});

        holder.bogi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(listCustom.get(position).getCk1());
                if (listCustom.get(position).getCk1()!=0) {
                    listCustom.get(position).setCk1(View.VISIBLE);
                    listCustom.get(position).setCk2(View.INVISIBLE);
                    listCustom.get(position).setCk3(View.INVISIBLE);
                    listCustom.get(position).setCk4(View.INVISIBLE);
                }
                else {
                    listCustom.get(position).setCk1(View.INVISIBLE);
                    listCustom.get(position).setCk2(View.INVISIBLE);
                    listCustom.get(position).setCk3(View.INVISIBLE);
                    listCustom.get(position).setCk4(View.INVISIBLE);
                }

                ((TestActivity2)TestActivity2.context).test(position,1);
                holder.ck1.setVisibility(View.VISIBLE);
                holder.ck2.setVisibility(View.INVISIBLE);
                holder.ck3.setVisibility(View.INVISIBLE);
                holder.ck4.setVisibility(View.INVISIBLE);
            }
        });
        holder.bogi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(listCustom.get(position).getCk2());
                if (listCustom.get(position).getCk2()!=0) {
                    listCustom.get(position).setCk1(View.INVISIBLE);
                    listCustom.get(position).setCk2(View.VISIBLE);
                    listCustom.get(position).setCk3(View.INVISIBLE);
                    listCustom.get(position).setCk4(View.INVISIBLE);
                }
                else {
                    listCustom.get(position).setCk1(View.INVISIBLE);
                    listCustom.get(position).setCk2(View.INVISIBLE);
                    listCustom.get(position).setCk3(View.INVISIBLE);
                    listCustom.get(position).setCk4(View.INVISIBLE);
                }

                ((TestActivity2)TestActivity2.context).test(position,2);
                holder.ck1.setVisibility(View.INVISIBLE);
                holder.ck2.setVisibility(View.VISIBLE);
                holder.ck3.setVisibility(View.INVISIBLE);
                holder.ck4.setVisibility(View.INVISIBLE);
            }
        });
        holder.bogi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(listCustom.get(position).getCk3());
                if (listCustom.get(position).getCk3()!=0) {
                    listCustom.get(position).setCk1(View.INVISIBLE);
                    listCustom.get(position).setCk2(View.INVISIBLE);
                    listCustom.get(position).setCk3(View.VISIBLE);
                    listCustom.get(position).setCk4(View.INVISIBLE);
                }
                else {
                    listCustom.get(position).setCk1(View.INVISIBLE);
                    listCustom.get(position).setCk2(View.INVISIBLE);
                    listCustom.get(position).setCk3(View.INVISIBLE);
                    listCustom.get(position).setCk4(View.INVISIBLE);
                }

                ((TestActivity2)TestActivity2.context).test(position,3);
                holder.ck1.setVisibility(View.INVISIBLE);
                holder.ck2.setVisibility(View.INVISIBLE);
                holder.ck3.setVisibility(View.VISIBLE);
                holder.ck4.setVisibility(View.INVISIBLE);
            }
        });
        holder.bogi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(listCustom.get(position).getCk4());
                if (listCustom.get(position).getCk4()!=0) {
                    listCustom.get(position).setCk1(View.INVISIBLE);
                    listCustom.get(position).setCk2(View.INVISIBLE);
                    listCustom.get(position).setCk3(View.INVISIBLE);
                    listCustom.get(position).setCk4(View.VISIBLE);
                }
                else {
                    listCustom.get(position).setCk1(View.INVISIBLE);
                    listCustom.get(position).setCk2(View.INVISIBLE);
                    listCustom.get(position).setCk3(View.INVISIBLE);
                    listCustom.get(position).setCk4(View.INVISIBLE);
                }

                ((TestActivity2)TestActivity2.context).test(position,4);
                holder.ck1.setVisibility(View.INVISIBLE);
                holder.ck2.setVisibility(View.INVISIBLE);
                holder.ck3.setVisibility(View.INVISIBLE);
                holder.ck4.setVisibility(View.VISIBLE);
            }
        });


        return convertView;
    }

    class CustomViewHolder {
        TextView textTitle;
        ImageView img;

        TextView qnum, bogi1, bogi2, bogi3, bogi4; // Only exam
        ImageView ck1 ,ck2, ck3, ck4;
    }

    public void addItem(CustomDTO dto) {
        listCustom.add(dto);
    }
}
