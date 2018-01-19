package com.boshim.www.sockettest;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @version 1.0
 * @Description:
 * @Author: zhh
 * @Date: 2018/1/19 18:34
 */

public class LogAdapter extends Adapter<LogAdapter.LogViewHolder> {

    private List<EmitResult> mStringList;


    public LogAdapter(List<EmitResult> list) {
        mStringList = list;
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log,parent,false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        EmitResult emitResult = mStringList.get(position);
        if (!emitResult.getResponse()){
            holder.tv.setTextColor(Color.rgb(255,0, 0));
        }else {
            holder.tv.setTextColor(Color.rgb(0,0, 255));
        }
        holder.tv.setText(emitResult.getId()+emitResult.getData());

    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public LogViewHolder(View view) {
            super(view);
            tv=(TextView) view.findViewById(R.id. log_message);
        }

    }
}
