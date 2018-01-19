package com.boshim.www.sockettest;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * @version 1.0
 * @Description:
 * @Author: zhh
 * @Date: 2018/1/20 1:50
 */

public class SocketData extends HashMap{
//    String userID;唯一识别码
//    String userName用户名
//    Int clientType客户端类型；0表示PC端，1表示安卓端
//    Int userType用户类型；0表示老师，1表示学生，-1表示白板
//    String roomCode房间号
//    Int interactStatus申请互动状态；0表示未申请状态，1表示申请状态，2表示发言状态
//    Boolean micEnable麦克风是否可用
//    Boolean cameraEnable摄像头是否可用
//    Int micStatus 麦克风状态；0表示未打开，1表示打开
//    Int cameraStatus摄像头状态；0表示未打开，1表示打开
//    Int screenPublishStatus屏幕共享状态；0表示未共享，1表示共享中
//    Int whitebroadStatus白板互动状态；0未互动，1表示互动中
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
