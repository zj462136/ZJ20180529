package com.bwie.zhangjun.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.bwie.zhangjun.R;
import com.bwie.zhangjun.bean.ProductsBean;
import com.bwie.zhangjun.inter.IView;
import com.bwie.zhangjun.presenter.CartPresenter;
import com.bwie.zhangjun.view.adapter.ShowAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowActivity extends AppCompatActivity implements IView {
    private int num = 1;
    @BindView(R.id.xrecyclerView)
    XRecyclerView xRecyclerView;
    private CartPresenter presenter;
    private HashMap<String, String> map;
    private ShowAdapter adapter;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);

        uid = getIntent().getStringExtra("uid");
        getData(num);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        num = 1;
                        getData(num);
                        adapter.notifyDataSetChanged();
                        xRecyclerView.refreshComplete();
                    }

                }, 2000);

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        num++;
                        getData(num);
                        adapter.notifyDataSetChanged();
                        xRecyclerView.refreshComplete();
                    }
                }, 2000);

            }
        });


    }

    public void getData(int count) {
        presenter = new CartPresenter();
        map = new HashMap<>();
        //pscid=39&page=1
        map.put("pscid", "39");
        map.put("page", count + "");
        presenter.getProductData(map);
        presenter.attachView(this);
    }

    @Override
    public void onSuccess(Object o) {
        if (o != null) {
            ProductsBean bean = (ProductsBean) o;
            Log.i("zzz", "onSuccess: " + bean.getMsg());
            final List<ProductsBean.DataBean> data = bean.getData();
            adapter = new ShowAdapter(this, data);
            xRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ShowAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(ShowActivity.this, ShowsActivity.class);
                    intent.putExtra("pid",data.get(position).getPid()+"");
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(int position) {

                }
            });
        }
    }

    @Override
    public void onFailed(Exception e) {

    }
}
