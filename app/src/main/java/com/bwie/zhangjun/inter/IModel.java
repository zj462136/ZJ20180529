package com.bwie.zhangjun.inter;

import java.util.Map;

/**
 * Created by lenovo on 2018/4/29.
 */

public interface IModel {
    void getProductData(Map<String, String> map);
    void getShowsData(Map<String, String> map);
    void getAddData(Map<String, String> map);
    void getCartsData(Map<String, String> map);
    void getDeleteData(Map<String, String> map);
    void getDingdanData(Map<String, String> map);
}