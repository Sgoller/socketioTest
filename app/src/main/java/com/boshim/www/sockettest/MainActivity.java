package com.boshim.www.sockettest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    SocketUtil mSocketUtil;

    @BindView(R.id.url)
    EditText url;

    @BindView(R.id.create)
    Button create;

    @BindView(R.id.start)
    Button start;

    @BindView(R.id.logview)
    RecyclerView logview;

    private Boolean toSend = true;

    private LogAdapter mAdapter;

    private List<String> mLogList = new ArrayList<String>(){{
        add("请创建");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new LogAdapter(this,mLogList);
        logview.setAdapter(mAdapter);
    }

    @OnClick({R.id.start,R.id.create})
    protected void onClick(View view){
        switch(view.getId()){
            case  R.id.create:
                if (mSocketUtil== null){
                    mSocketUtil = new SocketUtil();
                    mSocketUtil.createSocket(url.getText().toString());
                }
                break;
            case  R.id.start:
                sendTest();
                break;
            case  R.id.stop:
                toSend = false;
                break;
        }
    }

    private void sendTest(){
        toSend = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (toSend){
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){

                    }
                    sendMessage("12");
                }
            }
        }).start();
    };

    private void sendMessage(String message){
        mSocketUtil.send(message,new EmitCallBack(){
            @Override
            public synchronized void onSuccess(String arg) {
                super.onSuccess(arg);
                printLog(this.getId()+arg);
            }

            @Override
            public synchronized void onFailure(String arg) {
                super.onFailure(arg);
                printLog(this.getId()+arg);
            }
        });

    }

    private void printLog(final String log){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogList.add(log);
                mAdapter.notifyItemInserted(mLogList.size());
            }
        });
    }
}
