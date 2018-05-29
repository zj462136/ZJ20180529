package com.bwie.zhangjun.presenter;

import android.util.Log;

import com.bwie.zhangjun.inter.IView;
import com.bwie.zhangjun.bean.AddBean;
import com.bwie.zhangjun.bean.CartsBean;
import com.bwie.zhangjun.bean.DeleteBean;
import com.bwie.zhangjun.bean.DinganBean;
import com.bwie.zhangjun.bean.ProductsBean;
import com.bwie.zhangjun.bean.ShowsBean;
import com.bwie.zhangjun.inter.BasePresenter;
import com.bwie.zhangjun.model.CartModel;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by lenovo on 2018/4/29.
 */

public class CartPresenter implements BasePresenter {
    private IView iv;
    private DisposableSubscriber subscriber2;
    private DisposableSubscriber subscriber3;
    private DisposableSubscriber subscriber4;
    private DisposableSubscriber subscriber5;
    private DisposableSubscriber subscriber6;
    private DisposableSubscriber subscriber7;

    public void attachView(IView iv) {
        this.iv = iv;
    }

    public void dattachView() {
        if (iv != null) {
            iv = null;
        }
        if (subscriber2!=null){
            if (subscriber2.isDisposed()){
                subscriber2.dispose();
            }
        }
        if (subscriber3!=null){
            if (subscriber3.isDisposed()){
                subscriber3.dispose();
            }
        }
        if (subscriber4!=null){
            if (subscriber4.isDisposed()){
                subscriber4.dispose();
            }
        }
        if (subscriber5!=null){
            if (subscriber5.isDisposed()){
                subscriber5.dispose();
            }
        }
        if (subscriber6!=null){
            if (subscriber6.isDisposed()){
                subscriber6.dispose();
            }
        }
        if (subscriber7!=null){
            if (subscriber7.isDisposed()){
                subscriber7.dispose();
            }
        }
    }

    @Override
    public void getProductData(Map<String, String> map) {
        CartModel model = new CartModel(this);
        model.getProductData(map);
    }

    @Override
    public void getShowsData(Map<String, String> map) {
        CartModel model = new CartModel(this);
        model.getShowsData(map);
    }

    @Override
    public void getAddData(Map<String, String> map) {
        CartModel model = new CartModel(this);
        model.getAddData(map);
    }

    @Override
    public void getCartsData(Map<String, String> map) {
        CartModel model = new CartModel(this);
        model.getCartsData(map);
    }

    @Override
    public void getDeleteData(Map<String, String> map) {
        CartModel model = new CartModel(this);
        model.getDeleteData(map);
    }

    @Override
    public void getDingdanData(Map<String, String> map) {
        CartModel model = new CartModel(this);
        model.getDingdanData(map);
    }
    public void getDingdanNews(Flowable<DinganBean> flowable){
        subscriber7 = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DinganBean>() {
                    @Override
                    public void onNext(DinganBean dinganBean) {
                        if (dinganBean != null) {
                            iv.onSuccess(dinganBean);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        iv.onFailed((Exception) t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void getDeleteNews(Flowable<DeleteBean> flowable) {
        subscriber6 = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DeleteBean>() {
                    @Override
                    public void onNext(DeleteBean deleteBean) {
                        if (deleteBean != null) {
                            iv.onSuccess(deleteBean);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        iv.onFailed((Exception) t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    public void getCartsNews(Flowable<CartsBean> flowable) {
        subscriber5 = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CartsBean>() {
                    @Override
                    public void onNext(CartsBean cartsBean) {
                        if (cartsBean != null) {
                            iv.onSuccess(cartsBean);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        iv.onFailed((Exception) t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    public void getProductNews(Flowable<ProductsBean> flowable) {
        subscriber2 = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ProductsBean>() {
                    @Override
                    public void onNext(ProductsBean productsBean) {
                        if (productsBean != null) {
                            iv.onSuccess(productsBean);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        iv.onFailed((Exception) t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    public void getShowsNews(Flowable<ShowsBean> flowable) {
        subscriber3 = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ShowsBean>() {
                    @Override
                    public void onNext(ShowsBean showsBean) {
                        if (showsBean != null) {
                            iv.onSuccess(showsBean);
                            Log.i("fff", "onSuccess: "+showsBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        iv.onFailed((Exception) t);
                        Log.i("fff", "onSuccess: "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    public void getAddNews(Flowable<AddBean> flowable) {
        subscriber4 = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<AddBean>() {
                    @Override
                    public void onNext(AddBean addBean) {
                        if (addBean != null) {
                            iv.onSuccess(addBean);

                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        iv.onFailed((Exception) t);
                        Log.i("fff", "onSuccess: " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
