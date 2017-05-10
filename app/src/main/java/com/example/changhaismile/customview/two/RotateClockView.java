package com.example.changhaismile.customview.two;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by changhaismile on 2017/4/19.
 */

public class RotateClockView extends View {
    private static final int LONG_LINE_HEIGHT = 35;
    private static final int SHORT_LINE_HEIGHT = 25;
    private Paint mCirclePaint, mLinePaint;
    //圆环线宽度
    private int mCircleLineWidth, mHalfCircleLineWidth;
    private int mHalfWidth, mHalfHeight;
    //直线刻度线宽度
    private int mLineWidth, mHalfLineWidth;

    //长度宽度
    private int mLongLineHeight;
    //短线宽度
    private int mShortLineHeight;
    // 刻度线的左、上位置
    private int mLineLeft, mLineTop;
    //用于控制刻度线位置
    private int mFixLineHeight;
    // 刻度线的下边位置
    private int mLineBottom;

    public RotateClockView(Context context) {
        super(context);

        mCircleLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());
        mHalfCircleLineWidth = mCircleLineWidth;
        mLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());
        mHalfLineWidth = mLineWidth / 2;

        mFixLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());

        mLongLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                LONG_LINE_HEIGHT,
                getResources().getDisplayMetrics());
        mShortLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                SHORT_LINE_HEIGHT,
                getResources().getDisplayMetrics());
        initPaint();
    }

    private void initPaint() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.RED);
        //设置画笔空心
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleLineWidth);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(mLineWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawLines(canvas);
    }

    private void drawLines(Canvas canvas) {
        for (int i = 0; i <= 360; i ++) {
            if (i % 30 == 0) {
                mLineBottom = mLineTop + mLongLineHeight;
                mLinePaint.setStrokeWidth(mLineWidth);
            } else {
                mLineBottom = mLineTop + mShortLineHeight;
                mLinePaint.setStrokeWidth(mHalfLineWidth);
            }

            if (i % 6 == 0) {
                canvas.save();
                canvas.rotate(i, mHalfWidth, mHalfHeight);
                canvas.drawLine(mLineLeft, mLineTop, mLineLeft, mLineBottom, mLinePaint);
                canvas.restore();
            }
        }
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mHalfWidth, mHalfHeight, mHalfWidth - mHalfCircleLineWidth, mCirclePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("log", "w = " + w + ",h = " + h + ",oldw = " + oldw + ",oldh = " + oldh);
        mHalfWidth = w / 2;
        mHalfHeight = h / 2;
        mLineLeft = mHalfWidth - mHalfLineWidth;
        mLineTop = mHalfHeight - mHalfWidth + mFixLineHeight;
    }
}
