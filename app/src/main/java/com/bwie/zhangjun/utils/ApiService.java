package com.bwie.zhangjun.utils;

import com.bwie.zhangjun.bean.AddBean;
import com.bwie.zhangjun.bean.CartsBean;
import com.bwie.zhangjun.bean.DeleteBean;
import com.bwie.zhangjun.bean.DinganBean;
import com.bwie.zhangjun.bean.ProductsBean;
import com.bwie.zhangjun.bean.ShowsBean;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by lenovo on 2018/4/29.
 */

public interface ApiService {

    //https://www.zhaoapi.cn/product/getProducts?pscid=39&page=1
    @GET("product/getProducts")
    Flowable<ProductsBean> getProduct(@QueryMap Map<String, String> map);

    //https://www.zhaoapi.cn/product/getProductDetail?pid=45&source=android
    @GET("product/getProductDetail")
    Flowable<ShowsBean> getShows(@QueryMap Map<String, String> map);

    //http://120.27.23.105/product/addCart?uid=101&pid=45&source=android
    @GET("product/addCart")
    Flowable<AddBean> getAdd(@QueryMap Map<String, String> map);
    // http://120.27.23.105/product/getCarts?uid=75&source=android
    @GET("product/getCarts")
    Flowable<CartsBean> getCarts(@QueryMap Map<String, String> map);
    //http://120.27.23.105/product/deleteCart?uid=101&pid=45&source=android
    @GET("product/deleteCart")
    Flowable<DeleteBean> getDelete(@QueryMap Map<String, String> map);

    //http://120.27.23.105/product/getOrders?uid=71
    @GET("product/getOrders")
    Flowable<DinganBean> getDingdan(@QueryMap Map<String, String> map);
}
