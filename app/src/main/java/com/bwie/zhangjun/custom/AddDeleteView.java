package com.bwie.zhangjun.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bwie.zhangjun.R;

/**
 * Created by lenovo on 2018/4/29.
 */

public class AddDeleteView extends LinearLayout {
    private OnAddDelClickListener listener;
    private TextView et_number;

    public void setOnAddDelClickListener(OnAddDelClickListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }


    public interface OnAddDelClickListener {
        void onAddClick(View v);

        void onDelClick(View v);
    }

    public AddDeleteView(Context context) {
        this(context, null);
    }

    public AddDeleteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public AddDeleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View.inflate(context, R.layout.layout_add_delete, this);
        Button but_add = findViewById(R.id.but_add);
        Button but_delete = findViewById(R.id.but_delete);
        et_number = findViewById(R.id.et_number);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddDeleteViewStyle);
        String left_text = typedArray.getString(R.styleable.AddDeleteViewStyle_left_text);
        String middle_text = typedArray.getString(R.styleable.AddDeleteViewStyle_middle_text);
        String right_text = typedArray.getString(R.styleable.AddDeleteViewStyle_right_text);

        et_number.setText(middle_text);
        //释放资源
        typedArray.recycle();

        but_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddClick(view);
            }
        });
        but_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelClick(view);
            }
        });
    }

    /**
     * 对外提供设置EditText值的方法
     */
    public void setNumber(int number) {
        if (number > 0) {
            et_number.setText(number + "");
        }
    }

    /**
     * 得到控件原来的值
     */
    public int getNumber() {
        int number = 0;
        try {
            String numberStr = et_number.getText().toString().trim();
            number = Integer.valueOf(numberStr);
        } catch (Exception e) {
            number = 0;
        }
        return number;
    }
}
