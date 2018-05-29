package com.bwie.zhangjun.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjun.R;
import com.bwie.zhangjun.bean.CartsBean;
import com.bwie.zhangjun.bean.MessageEvent;
import com.bwie.zhangjun.bean.PriceAndCountEvent;
import com.bwie.zhangjun.custom.AddDeleteView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/4/29.
 */

public class ShopCartExListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<CartsBean.DataBean> list = new ArrayList<>();
    private boolean isShow = true;
    private boolean flag = true;
    private int totalCount=0;

    public ShopCartExListAdapter(Context context, List<CartsBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getList().size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            view = View.inflate(context, R.layout.item_shopcart_group, null);
            holder.determine_chekbox = (CheckBox) view.findViewById(R.id.determine_chekbox);
            holder.tv_source_name = (TextView) view.findViewById(R.id.tv_source_name);
            holder.tv_store_edtor = (TextView) view.findViewById(R.id.tv_store_edtor);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (GroupViewHolder) view.getTag();
        }
        final CartsBean.DataBean dataBean = list.get(groupPosition);

        holder.determine_chekbox.setChecked(dataBean.isChecked());

        holder.tv_source_name.setText(dataBean.getSellerName());
        //一级checkbox
        holder.determine_chekbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCount++;
                dataBean.setChecked(holder.determine_chekbox.isChecked());
                changeChildCbState(groupPosition, holder.determine_chekbox.isChecked());
                EventBus.getDefault().post(compute());
                changeAllCbState(isAllGroupCbSelected());
                notifyDataSetChanged();
            }
        });
        if (dataBean.isEdtor()) {
            holder.tv_store_edtor.setText("完成");
        } else {
            holder.tv_store_edtor.setText("编辑");
        }
        holder.tv_store_edtor.setOnClickListener(new GroupViewClick(groupPosition, holder.tv_store_edtor, dataBean));
        notifyDataSetChanged();
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            view = View.inflate(context,R.layout.item_shopcart_product, null);
            holder.cbChild = (CheckBox) view.findViewById(R.id.check_box);
            holder.tv_intro = (TextView) view.findViewById(R.id.tv_intro);
            holder.imgIcon = (SimpleDraweeView) view.findViewById(R.id.iv_adapter_list_pic);
            holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            holder.tv_del = (TextView) view.findViewById(R.id.tv_goods_delete);
            //            holder.bt_add = (ImageView) view.findViewById(R.id.bt_add);
            //            holder.bt_del = (ImageView) view.findViewById(R.id.bt_reduce);
            //            holder.et_num = (EditText) view.findViewById(R.id.et_num);
            holder.tv_num = (TextView) view.findViewById(R.id.tv_buy_num);
            holder.ll_edtor = (LinearLayout) view.findViewById(R.id.ll_edtor);
            holder.rl_no_edtor = (RelativeLayout) view.findViewById(R.id.rl_no_edtor);
            holder.adv = view.findViewById(R.id.adv_main);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ChildViewHolder) view.getTag();
        }
        final CartsBean.DataBean.ListBean datasBean = list.get(groupPosition).getList().get(childPosition);



        holder.cbChild.setChecked(datasBean.isChecked());

        holder.tv_intro.setText(datasBean.getTitle());
        holder.tv_price.setText("￥"+datasBean.getPrice() );
        holder.tv_num.setText(datasBean.getNum() + "");
        //        holder.et_num.setText(datasBean.getNum() + "");
        holder.adv.setNumber(datasBean.getNum());
        String[] split = datasBean.getImages().split("[|]");
        holder.imgIcon.setImageURI(Uri.parse(split[0]));

        //二级checkbox
        holder.cbChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置该条目对象里的checked属性值
                datasBean.setChecked(holder.cbChild.isChecked());
                PriceAndCountEvent priceAndCountEvent = compute();
                EventBus.getDefault().post(priceAndCountEvent);
                if (holder.cbChild.isChecked()) {
                    //当前checkbox是选中状态
                    if (isAllChildCbSelected(groupPosition)) {
                        changGroupCbState(groupPosition, true);
                        changeAllCbState(isAllGroupCbSelected());
                    }
                    totalCount++;
                } else {
                    changGroupCbState(groupPosition, false);
                    changeAllCbState(isAllGroupCbSelected());
                }
                notifyDataSetChanged();
            }
        });
        holder.adv.setOnAddDelClickListener(new AddDeleteView.OnAddDelClickListener() {
            @Override
            public void onAddClick(View v) {
                Log.i("zxz", "onAddClick: 执行");
                int origin = holder.adv.getNumber();
                origin++;
                //                holder.adv.setNumber(origin);
                int num = datasBean.getNum();
                num++;
                holder.adv.setNumber(num);
                datasBean.setNum(num);
                if (holder.cbChild.isChecked()) {
                    EventBus.getDefault().post(compute());
                }
            }

            @Override
            public void onDelClick(View v) {
                int origin = holder.adv.getNumber();
                //                int num = datasBean.getNum();

                origin--;
                if (origin == 0) {
                    Toast.makeText(context,"最小数量为1", Toast.LENGTH_SHORT).show();
                    return ;
                }
                holder.adv.setNumber(origin);
                datasBean.setNum(origin);
                if (holder.cbChild.isChecked()) {

                    EventBus.getDefault().post(compute());
                }

            }
        });
        //        //加号
        //        holder.bt_add.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                int num = datasBean.getNum();
        //                ++num;
        //                holder.tv_num.setText(num + "");
        //                holder.et_num.setText(num +"");
        //                datasBean.setNum(num);
        //                if (holder.cbChild.isChecked()) {
        //                    PriceAndCountEvent priceAndCountEvent = compute();
        //                    EventBus.getDefault().post(priceAndCountEvent);
        //                }
        //            }
        //        });
        //        //减号
        //        holder.bt_del.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                int num = datasBean.getNum();
        //                if (num == 1) {
        //                    return;
        //                }
        //                --num;
        //                holder.tv_num.setText(num + "");
        //                holder.et_num.setText(num + "");
        //                datasBean.setNum(num);
        //                if (holder.cbChild.isChecked()) {
        //                    PriceAndCountEvent priceAndCountEvent = compute();
        //                    EventBus.getDefault().post(priceAndCountEvent);
        //                }
        //            }
        //        });
        //删除
        holder.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert;
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
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
                                List<CartsBean.DataBean.ListBean> datasBeen = list.get(groupPosition).getList();
                                CartsBean.DataBean.ListBean remove = datasBeen.remove(childPosition);
                                if (datasBeen.size() == 0) {
                                    list.remove(groupPosition);
                                }
                                EventBus.getDefault().post(compute());
                                notifyDataSetChanged();
                            }
                        });
                alert.show();
            }
        });
        //编辑
        //判断是否在编辑状态下(标题中的编辑)
        if (list.get(groupPosition).isEdtor() == true) {
            holder.ll_edtor.setVisibility(View.VISIBLE);
            holder.rl_no_edtor.setVisibility(View.GONE);
        } else {
            holder.ll_edtor.setVisibility(View.GONE);
            holder.rl_no_edtor.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        CheckBox determine_chekbox;
        TextView tv_source_name,tv_store_edtor;
    }
    /**
     * 使某个组处于编辑状态
     * <p/>
     * groupPosition组的位置
     */
    class GroupViewClick implements View.OnClickListener {
        private int groupPosition;
        private TextView edtor;
        private CartsBean.DataBean group;

        public GroupViewClick(int groupPosition, TextView edtor, CartsBean.DataBean group) {
            this.groupPosition = groupPosition;
            this.edtor = edtor;
            this.group = group;
        }
        @Override
        public void onClick(View v) {
            int groupId = v.getId();
            if (groupId == edtor.getId()) {
                if (group.isEdtor()) {
                    group.setEdtor(false);
                } else {
                    group.setEdtor(true);

                }
                notifyDataSetChanged();
            }
        }
    }
    /**
     * 监听编辑状态
     */
    public interface GroupEdtorListener {
        void groupEdit(int groupPosition);
    }

    class ChildViewHolder {
        CheckBox cbChild;
        TextView tv_intro;
        SimpleDraweeView imgIcon;
        TextView tv_price;
        TextView tv_del;
        ImageView bt_del,bt_add;
        EditText et_num;
        //        TextView tv_num;
        LinearLayout ll_edtor;
        RelativeLayout rl_no_edtor;
        AddDeleteView adv;
        ImageView iv_del;
        ImageView iv_add;
        TextView tv_num;
    }

    /**
     * 改变全选的状态
     *
     * @param flag
     */
    private void changeAllCbState(boolean flag) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setChecked(flag);
        EventBus.getDefault().post(messageEvent);
    }

    /**
     * 改变一级列表checkbox状态
     *
     * @param groupPosition
     */
    private void changGroupCbState(int groupPosition, boolean flag) {
        CartsBean.DataBean dataBean = list.get(groupPosition);
        dataBean.setChecked(flag);
    }

    /**
     * 改变二级列表checkbox状态
     *
     * @param groupPosition
     * @param flag
     */
    private void changeChildCbState(int groupPosition, boolean flag) {
        List<CartsBean.DataBean.ListBean> datasBeen = list.get(groupPosition).getList();

        for (int i = 0; i < datasBeen.size(); i++) {
            CartsBean.DataBean.ListBean datasBean = datasBeen.get(i);

            datasBean.setChecked(flag);
        }
    }

    /**
     * 判断一级列表是否全部选中
     *
     * @return
     */
    private boolean isAllGroupCbSelected() {
        for (int i = 0; i < list.size(); i++) {
            CartsBean.DataBean dataBean = list.get(i);
            if (!dataBean.isChecked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断二级列表是否全部选中
     *
     * @param groupPosition
     * @return
     */
    private boolean isAllChildCbSelected(int groupPosition) {
        List<CartsBean.DataBean.ListBean> datasBeen = list.get(groupPosition).getList();
        for (int i = 0; i < datasBeen.size(); i++) {
            CartsBean.DataBean.ListBean datasBean = datasBeen.get(i);
            if (!datasBean.isChecked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算列表中，选中的钱和数量
     */
    private PriceAndCountEvent compute() {
        int count = 0;
        int price = 0;
        for (int i = 0; i < list.size(); i++) {
            List<CartsBean.DataBean.ListBean> datasBeen = list.get(i).getList();
            for (int j = 0; j < datasBeen.size(); j++) {
                CartsBean.DataBean.ListBean datasBean = datasBeen.get(j);
                if (datasBean.isChecked()) {
                    price += datasBean.getNum() * datasBean.getPrice();
                    count += datasBean.getNum();
                }
            }
        }
        PriceAndCountEvent priceAndCountEvent = new PriceAndCountEvent();
        priceAndCountEvent.setCount(count);
        priceAndCountEvent.setPrice(price);
        return priceAndCountEvent;
    }

    /**
     * 设置全选、反选
     *
     * @param flag
     */
    public void changeAllListCbState(boolean flag) {
        for (int i = 0; i < list.size(); i++) {
            changGroupCbState(i, flag);
            changeChildCbState(i, flag);
        }
        EventBus.getDefault().post(compute());
        notifyDataSetChanged();
    }
    /**
     * 是否显示可编辑
     *
     * @param flag
     */
    public void isShow(boolean flag) {
        isShow = flag;
        notifyDataSetChanged();
    }
}