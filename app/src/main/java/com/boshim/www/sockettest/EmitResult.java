package com.boshim.www.sockettest;

/**
 * @version 1.0
 * @Description:
 * @Author: zhh
 * @Date: 2018/1/20 2:22
 */

public class EmitResult {

    public static final String TIME_OUT = "timeout";
    public static final String NOT_CONNECT = "notconnect";

    private int id;
    private Boolean response = true;
    private String code;
    private String data;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
