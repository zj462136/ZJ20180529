package com.bwie.zhangjun.inter;

/**
 * Created by lenovo on 2018/4/29.
 */

public interface IView {
    void onSuccess(Object o);
    void onFailed(Exception e);
}