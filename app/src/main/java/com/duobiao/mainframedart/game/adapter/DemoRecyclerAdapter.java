package com.duobiao.mainframedart.game.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.game.bean.DartBean;

import java.util.ArrayList;

/**
 * Author:Admin
 * Time:2019/9/9 10:20
 * 描述：
 */
public class DemoRecyclerAdapter extends RecyclerView.Adapter<DemoRecyclerAdapter.DemoViewHolder> {
    ArrayList<DartBean> dartBeans;

    public DemoRecyclerAdapter(ArrayList<DartBean> list){
        dartBeans = list;
    }

    class DemoViewHolder extends RecyclerView.ViewHolder{

        TextView dart_text;
        CheckBox img_check;
        public DemoViewHolder(@NonNull View itemView) {
            super(itemView);
            dart_text = itemView.findViewById(R.id.dart_text);
            img_check = itemView.findViewById(R.id.img_check);
        }
    }
    @NonNull
    @Override
    public DemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dart_layout,parent,false);
        DemoViewHolder viewHolder = new DemoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DemoViewHolder holder, int position) {
        holder.dart_text.setText(dartBeans.get(position).getDartBleString());
        holder.img_check.setChecked(dartBeans.get(position).isEffective());
    }

    @Override
    public int getItemCount() {
        return dartBeans.size();
    }
}
