package com.boshim.www.sockettest;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * @version 1.0
 * @Description:
 * @Author: zhh
 * @Date: 2018/1/19 17:18
 */

public class SocketUtil {

    private static final String TAG = SocketUtil.class.getSimpleName();

    private static final int TIMEOUT = 20000;

    private Socket mSocket;
    public void createSocket(String url){
        try {
            HttpTokenBus httpTokenBus = new HttpTokenBus();
            httpTokenBus.setUpINSECURESSLContext();
            IO.setDefaultHostnameVerifier(httpTokenBus.getHostnameVerifier());
            IO.setDefaultSSLContext(httpTokenBus.getSslContext());
            mSocket = IO.socket(url);
        } catch (URISyntaxException e) {
            Log.d(TAG, "createSocket: error");
            throw new RuntimeException(e);
        }
        mSocket.connected();
    }

    public void send(String message,final EmitCallBack ack){
        final long start = System.currentTimeMillis();
        final boolean timeout = false;
        if (mSocket.connected()){
            mSocket.send(message, new Ack() {
                @Override
                public void call(Object... args) {
                    if (!ack.getCalled())
                        ack.onSuccess(args[0].toString());
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(TIMEOUT);
                        if (!ack.getCalled())
                            ack.onFailure("timeout");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        else{
            ack.onFailure("not connected");
        }
    }
}
