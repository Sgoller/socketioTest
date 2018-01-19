package com.boshim.www.sockettest;

import android.content.Context;
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

    private List<String> mStringList;

    private LayoutInflater mInflater;


    public LogAdapter(Context context,List<String> list) {
        mStringList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_log,parent,false);
        LogViewHolder logViewHolder = new LogViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        holder.tv.setText(mStringList.get(position));
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
