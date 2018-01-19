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

    private List<EmitResult> mLogList = new ArrayList<EmitResult>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        logview.setLayoutManager(linearLayoutManager);
        mAdapter = new LogAdapter(mLogList);
        logview.setAdapter(mAdapter);
    }

    @OnClick({R.id.start,R.id.create,R.id.stop})
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
        if (mSocketUtil==null)
            return;
        toSend = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (toSend){
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){

                    }
                    SocketData data = new SocketData();
                    data.put("userID","test");
                    data.put("userType",1);
                    data.put("roomCode","ssssssssssssssss");
                    sendMessage("toServer:user:enterRoom",data);
                }
            }
        }).start();
    };

    private void sendMessage(String event,SocketData message){
        String sender = message.toString();
        mSocketUtil.emit(event,sender,new EmitCallBack(){
            @Override
            public synchronized void onSuccess(EmitResult arg) {
                super.onSuccess(arg);
                arg.setId(this.getId());
                printLog(arg);
            }

            @Override
            public synchronized void onFailure(EmitResult arg) {
                super.onFailure(arg);
                arg.setId(this.getId());
                printLog(arg);
            }
        });

    }

    private void printLog(final EmitResult log){
        if (!log.getResponse() && log.getMessage().equals(EmitResult.TIME_OUT)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogList.add(log);
                    mAdapter.notifyItemInserted(mLogList.size());
                    logview.scrollToPosition(mLogList.size()-1);
                }
            });
        }
    }
}
