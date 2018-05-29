package com.bwie.zhangjun.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjun.R;
import com.bwie.zhangjun.bean.CartsBean;
import com.bwie.zhangjun.bean.MessageEvent;
import com.bwie.zhangjun.bean.PriceAndCountEvent;
import com.bwie.zhangjun.inter.IView;
import com.bwie.zhangjun.presenter.CartPresenter;
import com.bwie.zhangjun.view.adapter.ShopCartExListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends AppCompatActivity implements IView {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subtitle)
    TextView subtitle;
    @BindView(R.id.top_bar)
    LinearLayout topBar;
    @BindView(R.id.exListView)
    ExpandableListView exListView;
    @BindView(R.id.all_chekbox)
    CheckBox allChekbox;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_go_to_pay)
    TextView tvGoToPay;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_shar)
    LinearLayout llShar;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;

    private int count = 0;
    private List<CartsBean.DataBean> list = new ArrayList<>();
    private ShopCartExListAdapter adapter;
    private boolean flag = false;
    private int totalCount = 0;
    private double totalPrice = 0.00;
    private CartPresenter presenter;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        uid = getIntent().getStringExtra("uid");
        getData();
    }


    private void getData() {
        presenter = new CartPresenter();
        presenter.attachView(this);
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("source", "android");
        presenter.getCartsData(map);
        adapter = new ShopCartExListAdapter(this, list);
        exListView.setAdapter(adapter);
    }

    @Override
    public void onSuccess(Object o) {
        if (o !=null) {
            CartsBean cartsBean = (CartsBean) o;
            List<CartsBean.DataBean> dataBean = cartsBean.getData();
            list.addAll(dataBean);
            adapter.notifyDataSetChanged();
            for (int i = 0; i < adapter.getGroupCount(); i++) {
                exListView.expandGroup(i);
            }
        }
    }


    @Override
    public void onFailed(Exception e) {

    }

    public void delete() {

        for (int i = 0; i < list.size(); i++) {
            List<CartsBean.DataBean.ListBean> listbean = list.get(i).getList();
            for (int j = 0; j < listbean.size(); j++) {
                CartsBean.DataBean.ListBean listBean = listbean.get(j);
                if (listBean.isChecked()) {
                    listbean.remove(j);
                    j--;
                    if (listbean.size() == 0) {
                        list.remove(i);
                        i--;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.all_chekbox, R.id.tv_share, R.id.tv_save, R.id.tv_delete, R.id.subtitle, R.id.tv_go_to_pay,R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.all_chekbox:
                adapter.changeAllListCbState(allChekbox.isChecked());
                break;
            case R.id.tv_share:
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_save:
                Toast.makeText(this, "加入成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_delete:
                AlertDialog dialog;
                if (totalCount == 0) {
                    Toast.makeText(CartActivity.this, "请选择要删除的商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog = new AlertDialog.Builder(CartActivity.this).create();
                dialog.setTitle("操作提示");
                dialog.setMessage("您确定要将这些商品从购物车中移除吗？");
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                        Toast.makeText(CartActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case R.id.subtitle:
                flag = !flag;
                if (flag) {
                    subtitle.setText("完成");
                    llShar.setVisibility(View.VISIBLE);
                    llInfo.setVisibility(View.GONE);
                } else {
                    subtitle.setText("编辑");
                    llShar.setVisibility(View.GONE);
                    llInfo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_go_to_pay:
                AlertDialog alert;
                if (totalCount == 0) {
                    Toast.makeText(this, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(this).create();
                alert.setTitle("操作提示");
                alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(CartActivity.this, DingdanActivity.class);
                                startActivity(intent);
                                Toast.makeText(CartActivity.this,"创建订单",Toast.LENGTH_SHORT).show();
                            }
                        });
                alert.show();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        allChekbox.setChecked(event.isChecked());
    }

    @Subscribe
    public void onMessageEvent(PriceAndCountEvent event) {
        tvGoToPay.setText("结算(" + event.getCount() + ")");
        tvTotalPrice.setText("￥" + event.getPrice());
        totalCount = event.getCount();
        totalPrice = event.getPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.dattachView();
        }
    }
} 
