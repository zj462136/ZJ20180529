package com.bwie.zhangjun.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjun.R;
import com.bwie.zhangjun.application.MyApplication;
import com.bwie.zhangjun.bean.DataBean;
import com.bwie.zhangjun.dao.DataBeanDao;
import com.bwie.zhangjun.view.adapter.SearchViewGreenDaoAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.search_greendao_flowlayout)
    TagFlowLayout searchGreendaoFlowlayout;
    @BindView(R.id.search_greendao_lv)
    ListView searchGreendaoLv;
    @BindView(R.id.search_greendao_rl)
    RelativeLayout searchGreendaoRl;
    @BindView(R.id.search_greendao_delete)
    Button searchGreendaoDelete;
    int id = 0;
    String name = "";
    String[] names = {"android", "HTML", "java", "PHP", "C", "C++", "NodeJs", "Hexo", "Github"};
    @BindView(R.id.search_ok)
    Button searchOk;
    QueryBuilder qb;
    Context mContext;
    SearchViewGreenDaoAdapter adapter;
    @BindView(R.id.search_main)
    RelativeLayout mSearchMain;
    private DataBeanDao dataBeanDao;
    private List<DataBean> list;
    DataBean dataBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = MainActivity.this;
        searchview.setQueryHint("请输入");
        searchview.onActionViewExpanded();
        searchview.clearFocus();
        delectUnderline();
        //初始化数据库
        dataBeanDao = MyApplication.getDaoSession().getDataBeanDao();
        initDate();
        searchGreendaoLv.setTextFilterEnabled(true);
    }

    private void initDate() {
        //搜索历史列表
        updateList();
        //热门搜索
        searchGreendaoFlowlayout.setAdapter(new TagAdapter<String>(names) {
            @Override
            public View getView(FlowLayout parent, int position, final String s) {
                final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(
                        R.layout.search_page_flowlayout_tv, searchGreendaoFlowlayout, false);
                tv.setText(s);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "" + s, Toast.LENGTH_SHORT).show();

                    }
                });
                return tv;
            }
        });

        //搜索文本监听
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //点击搜索
            public boolean onQueryTextSubmit(String query) {
                name = query;
                Log.e("name--------", name + "");
                insertDB();
                return false;
            }

            @Override
            //当搜索内容改变
            public boolean onQueryTextChange(String newText) {
                name = newText;
                Log.e("newText---------", newText);
                if (name.equals("")) {
                } else {
                }
                return false;
            }
        });

    }


    /**
     * 初始化adapter，更新list，重新加载列表
     */
    private void updateList() {
        //查询所有
        list = dataBeanDao.queryBuilder().list();
        //这里用于判断是否有数据
        if (list.size() == 0) {
            searchGreendaoRl.setVisibility(View.VISIBLE);
            searchGreendaoDelete.setVisibility(View.GONE);
        } else {
            searchGreendaoRl.setVisibility(View.GONE);
            searchGreendaoDelete.setVisibility(View.VISIBLE);
        }
        //list倒序排列
        Collections.reverse(list);
        adapter = new SearchViewGreenDaoAdapter(mContext, list);
        searchGreendaoLv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    //增
    private void insertDB() {
        try {
            if (list.size() < 8) {
                //删除已经存在重复的搜索历史
                List<DataBean> list2 = dataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Title.eq(name)).build().list();
                dataBeanDao.deleteInTx(list2);
                //添加
                if (!name.equals(""))
                    dataBeanDao.insert(new DataBean(null, name));
                Toast.makeText(mContext, "插入数据成功:" + name, Toast.LENGTH_SHORT).show();
            } else {
                //删除第一条数据，用于替换最后一条搜索
                dataBeanDao.delete(dataBeanDao.queryBuilder().list().get(0));
                //删除已经存在重复的搜索历史
                List<DataBean> list3 = dataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Title.eq(name)).build().list();
                dataBeanDao.deleteInTx(list3);
                //添加
                if (!name.equals(""))
                    dataBeanDao.insert(new DataBean(null, name));
            }
            updateList();
        } catch (Exception e) {
            Toast.makeText(mContext, "插入失败", Toast.LENGTH_SHORT).show();
        }

    }

    //清空数据库
    private void delectAllDB() {
        try {
            dataBeanDao.deleteAll();
            list.clear();
            adapter.notifyDataSetChanged();
            searchGreendaoRl.setVisibility(View.VISIBLE);
            searchGreendaoDelete.setVisibility(View.GONE);
            Toast.makeText(mContext, "清空数据库", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("exception-----delete", dataBean + "message:" + e.getMessage() + "");
        }
    }


    @OnClick({R.id.search_greendao_delete,
            R.id.search_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_greendao_delete:
                delectAllDB();
                break;
            case R.id.search_ok:
                insertDB();
                Intent intent = new Intent(this, ShowActivity.class);
                intent.putExtra("uid","2797");
                startActivity(intent);
                break;

        }
    }

    /**
     * 去掉searchview下划线
     */
    public void delectUnderline() {
        if (searchview != null) {
            try {        //--拿到字节码
                Class<?> argClass = searchview.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchMain");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchview);
                //--设置背景
                mView.setBackgroundColor(R.drawable.shape_search);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}