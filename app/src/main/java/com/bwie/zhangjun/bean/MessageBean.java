package com.bwie.zhangjun.bean;

public class MessageBean<T> {

    private String msg;
    private String code;
    private String page;
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public MessageBean(String msg, String code, String page, T data) {

        this.msg = msg;
        this.code = code;
        this.page = page;
        this.data = data;
    }
}