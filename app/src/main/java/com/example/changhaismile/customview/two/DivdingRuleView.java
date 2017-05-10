package com.example.changhaismile.customview.two;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by changhaismile on 2017/4/19.
 * 绘制刻度尺
 */

public class DivdingRuleView extends View {
    // 刻度尺高度
    private static final int DIVIDING_RULE_HEIGHT = 70;
    // 距离左右间
    private static final int DIVIDING_RULE_MARGIN_LEFT_RIGHT = 10;

    // 第一条线距离边框距离
    private static final int FIRST_LINE_MARGIN = 5;
    // 打算绘制的厘米数
    private static final int DEFAULT_COUNT = 9;

    private Resources mResuource;
    private Paint mOuterPaint, mLinePaint;
    private int mTotalWidth, mTotalHeight;
    private int mHalfWidth, mHalfHeight;

    //刻度尺高度
    private int mDividRuleHeight, mHalfRuleHeight;
    // 刻度尺距离左边距离
    private int mDividRuleLeftMargin;
    // 第一条线距离边框距离
    private int mFirstLineMargin;
    // 最高刻度线上端位置
    private int mMaxLineTop;
    // 中等刻度线上端位置
    private int mMiddleLineTop;
    // 最低刻度线上端位置
    private int mMinLineTop;
    // 刻度线之间的距离
    private int mLineInterval;
    // 刻度尺底端位置
    private int mRuleBottom;
    // 刻度线起始点
    private int mLineStartX;
    //外部区域
    private Rect mOutRect;

    public DivdingRuleView(Context context) {
        super(context);
        mResuource = context.getResources();
        initData();
        initPaint();
    }

    private void initData() {
        mDividRuleHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
            , DIVIDING_RULE_HEIGHT, mResuource.getDisplayMetrics());
        mHalfRuleHeight = mDividRuleHeight / 2;
        mDividRuleLeftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DIVIDING_RULE_MARGIN_LEFT_RIGHT, mResuource.getDisplayMetrics());
        mFirstLineMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                FIRST_LINE_MARGIN, mResuource.getDisplayMetrics());
    }

    private void initPaint() {
        mOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterPaint.setColor(Color.BLACK);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeWidth(1);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalHeight = h;
        mTotalWidth = w;
        mHalfHeight = h / 2;
        mHalfWidth = w / 2;

        int top = mHalfHeight - mHalfRuleHeight;
        mRuleBottom = top + mDividRuleHeight;
        mOutRect = new Rect(mDividRuleLeftMargin, top,
                mTotalWidth - mDividRuleLeftMargin, mRuleBottom);
        mLineInterval = (mTotalWidth - 2 * mDividRuleLeftMargin - 2 * mFirstLineMargin)
                / (DEFAULT_COUNT * 10 - 1);
        mLineStartX = mDividRuleLeftMargin + mFirstLineMargin;
        mMaxLineTop = mRuleBottom - mDividRuleHeight / 2;
        mMiddleLineTop = mRuleBottom - mDividRuleHeight/ 3;
        mMinLineTop = mRuleBottom - mDividRuleHeight / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制外框
        drawOuter(canvas);
        //绘制刻度线
        drawLines(canvas);
    }

    /**
     * 绘制外框
     */
    private void drawOuter(Canvas canvas) {
        canvas.drawRect(mOutRect, mOuterPaint);
    }

    /**
     * 绘制刻度线
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        canvas.save();
        canvas.translate(mLineStartX, 0);
        int top = mMaxLineTop;
        for(int i = 0; i <= DEFAULT_COUNT * 10; i ++) {
            if (i % 10 == 0) {
                top = mMaxLineTop;
            } else if (i % 5 == 0) {
                top = mMiddleLineTop;
            } else {
                top = mMinLineTop;
            }
            canvas.drawLine(0, mRuleBottom, 0, top, mLinePaint);
            canvas.translate(mLineInterval, 0);
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
