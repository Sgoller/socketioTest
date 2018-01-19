package com.boshim.www.sockettest;

/**
 * @version 1.0
 * @Description:
 * @Author: zhh
 * @Date: 2018/1/19 17:25
 */

public  class EmitCallBack {

    private static int sId = 0;

    private int id;

    public EmitCallBack() {
        id =sId++;
    }

    public int getId() {
        return id;
    }

    private Boolean isCalled = false;

    public synchronized  Boolean getCalled() {
        return isCalled;
    }

    public synchronized  void onSuccess(EmitResult arg){
        isCalled = true;
    };
    public synchronized  void onFailure(EmitResult arg){
        isCalled = true;
    };
}
