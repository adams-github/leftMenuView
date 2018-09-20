package com.example.administrator.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/4/11.
 *
 * 半透明黑色挡板progressview,用于显示进度
 */


public class TransparentProgressView extends RelativeLayout{

    private int percent = 0;
    private View view;
    private View progressView;

    public TransparentProgressView(Context context) {
        super(context);
        initView();
    }

    public TransparentProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        view = LayoutInflater.from(getContext()).inflate(R.layout.transparentprogress_layout, this);
        progressView = view.findViewById(R.id.progressView);
    }

    public void setPercent(int percent){
        this.percent = percent;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        progressView.layout(0, (percent * b) / 100, r, b);
    }


}
