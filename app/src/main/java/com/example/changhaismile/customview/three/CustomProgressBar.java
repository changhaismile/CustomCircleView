package com.example.changhaismile.customview.three;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.changhaismile.customview.R;

/**
 * Created by changhaismile on 2017/4/21.
 */

public class CustomProgressBar extends View {
    /**画笔*/
    private Paint mPaint;
    /**第一圈颜色*/
    private int mFirstColor;
    /**第二圈颜色*/
    private int mSecondColor;
    /**圈宽度*/
    private int mCircleWidth;
    /**当前进度*/
    private int mProgress;
    /**进度*/
    private int speed;
    private boolean isNext = false;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0;i < n; i ++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgressBar_firstColor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr,
                            (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_speed:
                    speed = a.getInt(attr, 20);
                    break;

            }
        }
        a.recycle();
        mPaint = new Paint();

        //绘制线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    mProgress ++ ;
                    if (mProgress == 360) {
                        mProgress = 0;
                        if (isNext) {
                            isNext = false;
                        } else {
                            isNext = true;
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(100 / speed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取圆心x坐标
        int center = getWidth() / 2;
        //半径
        int radius = center - mCircleWidth;
        //设置圆环宽度
        mPaint.setStrokeWidth(mCircleWidth);
        //消除锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        RectF oval = new RectF(center - radius, center - radius,
                center + radius, center + radius);
        if (!isNext) {
            //第一颜色圈完整，第二颜色转动
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mSecondColor);
            canvas.drawArc(oval, -90, mProgress, false, mPaint);
        } else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mFirstColor);
            canvas.drawArc(oval, -90, mProgress, false, mPaint);
        }
    }
}
