package com.bwie.zhangjun.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.zhangjun.R;
import com.bwie.zhangjun.bean.DinganBean;

import java.util.List;

/**
 * Created by lenovo on 2018/4/29.
 */

public class DingdanAdapter extends RecyclerView.Adapter<DingdanAdapter.ViewHolder> {
    private Context context;
    private List<DinganBean.DataBean> list;
    private View v;

    public DingdanAdapter(Context context, List<DinganBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = View.inflate(context, R.layout.item_main, null);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.price.setText("价格: "+list.get(position).getPrice());
        holder.time.setText("创建时间: "+list.get(position).getCreatetime());
        final DinganBean.DataBean dataBean = list.get(position);
        int status = list.get(position).getStatus();
        if(status==2){
            holder.wen.setText("已取消");
            holder.wen.setTextColor(Color.BLACK);
        }else if(status == 1){
            holder.wen.setText("已支付");
            holder.wen.setTextColor(Color.BLACK);
        }else{
            holder.wen.setTextColor(Color.RED);
            holder.wen.setText("待支付");
            holder.btn.setText("取消支付");
        }
        final DialogInterface.OnClickListener click1 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //  Toast.makeText(context,"否",Toast.LENGTH_LONG).show();;
            }
        };
        final DialogInterface.OnClickListener click2 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(context,"订单已取消",Toast.LENGTH_LONG).show();
                // xiuCartPersenter.getNews();
                dataBean.setStatus(0);
                notifyDataSetChanged();
            }
        };
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"查看订单",Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(context).setTitle("提示").setMessage("确定取消订单吗？")
                        .setPositiveButton("否", click1)
                        .setNegativeButton("是", click2).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView price;
        private final TextView time;
        private final TextView wen;
        private final Button btn;
        public ViewHolder(View itemView) {
            super(itemView);
            title = v.findViewById(R.id.tv_title);
            price = v.findViewById(R.id.tv_price);
            time = v.findViewById(R.id.tv_time);
            wen = v.findViewById(R.id.tv_wen);
            btn= v.findViewById(R.id.btn);
        }
    }
}
