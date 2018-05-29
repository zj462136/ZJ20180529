package com.bwie.zhangjun.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjun.R;
import com.bwie.zhangjun.bean.AddBean;
import com.bwie.zhangjun.bean.ShowsBean;
import com.bwie.zhangjun.inter.IView;
import com.bwie.zhangjun.presenter.CartPresenter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowsActivity extends AppCompatActivity {
    @BindView(R.id.regist_back)
    ImageView registBack;
    @BindView(R.id.shows_img)
    SimpleDraweeView showsImg;
    @BindView(R.id.shows_title)
    TextView showsTitle;
    @BindView(R.id.shows_price)
    TextView showsPrice;
    @BindView(R.id.cart)
    Button cart;
    @BindView(R.id.add_cart)
    Button addCart;
    private CartPresenter presenter;
    private HashMap<String, String> map;
    private String pid;
    private CartPresenter presenter1;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);
        ButterKnife.bind(this);
        pid = getIntent().getStringExtra("pid");
        uid = getIntent().getStringExtra("uid");
        Log.i("fff", "onCreate: "+ pid);
        presenter = new CartPresenter();
        map = new HashMap<>();
        map.put("pid", pid);
        map.put("source", "android");
        presenter.getShowsData(map);
        presenter.attachView(new IView() {
            @Override
            public void onSuccess(Object o) {
                if (o != null) {
                    ShowsBean bean = (ShowsBean) o;
                    Log.i("fff", "onSuccess: "+bean.getMsg());
                    String[] split = bean.getData().getImages().split("[|]");
                    showsImg.setImageURI(split[0]);
                    showsTitle.setText(bean.getData().getTitle());
                    showsPrice.setText("￥"+bean.getData().getPrice());
                }
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dattachView();
        }
    }

    @OnClick({R.id.regist_back, R.id.cart, R.id.add_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.regist_back:
                finish();
                break;
            case R.id.cart:
                Intent intent = new Intent(ShowsActivity.this, CartActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                break;
            case R.id.add_cart:
                HashMap<String, String> map = new HashMap<>();
                map.put("uid",uid);
                map.put("pid",pid);
                map.put("source","android");
                presenter.getAddData(map);
                presenter.attachView(new IView() {
                    @Override
                    public void onSuccess(Object o) {
                        if (o!=null){
                            AddBean bean = (AddBean) o;
                            Toast.makeText(ShowsActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });
                break;
        }
    }
}
