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
        mSocket.connect();
    }

    public void emit(String event,String data, final EmitCallBack ack){
        final long start = System.currentTimeMillis();
        final boolean timeout = false;
        if (mSocket.connected()){
            mSocket.emit(event,data, new Ack() {
                @Override
                public void call(Object... args) {
                    if (!ack.getCalled()){
                        EmitResult result = new EmitResult();
                        result.setResponse(true);
                        result.setData(args[0].toString());
                        ack.onSuccess(result);
                    }
                    else {
                        EmitResult result = new EmitResult();
                        result.setResponse(false);
                        result.setData(args[0].toString());
                        result.setMessage(EmitResult.TIME_OUT);
                        ack.onSuccess(result);
                    }
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(TIMEOUT);
                        if (!ack.getCalled()){
                            EmitResult result = new EmitResult();
                            result.setResponse(false);
                            result.setMessage(EmitResult.TIME_OUT);
                            result.setData(EmitResult.TIME_OUT);
                            ack.onFailure(result);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        else{
            if (!ack.getCalled()){
                EmitResult result = new EmitResult();
                result.setResponse(false);
                result.setMessage(EmitResult.NOT_CONNECT);
                result.setData(EmitResult.NOT_CONNECT);
                ack.onFailure(result);
                ack.onFailure(result);
            }
        }
    }
}
