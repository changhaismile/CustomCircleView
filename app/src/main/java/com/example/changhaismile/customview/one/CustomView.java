package com.example.changhaismile.customview.one;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.changhaismile.customview.R;

/**
 * Created by changhaismile on 2017/4/13.
 */
public class CustomView extends View{
    /**文本*/
    private String mTitleText;
    /**文本的颜色*/
    private int mTitleTextColor;
    /**文本大小*/
    private int mTitleTextSize;
    /**绘制时控制文本绘制的范围*/
    private Rect mBound;
    private Paint mPaint;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /**
         * 获取我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i ++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    //默认字体颜色为黑色
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    //默认设置为16sp,TypeValue可以把sp转换为px
                    mTitleTextSize = a.getDimensionPixelSize(attr,
                            (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    16,
                                    context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        /**
         * 获得绘制文本宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.i("log", "widthMode = " + widthMode + ", widthSIze = " + widthSize
                + "\nheightMode = " + heightMode + ",heightSize = " + heightSize);
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            Log.i("log", "width-----exactly");
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
            Log.i("log", "width-----at_most");
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
            Log.i("log", "height-----exactly");
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int)(getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
            Log.i("log", "height-----at_most");
        }
        setMeasuredDimension(width, height);
        Log.i("log", "\nwidth = " + width + ", height = " + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth()/2 - mBound.width()/2, getHeight()/2 + mBound.height()/ 2, mPaint);
    }
}
