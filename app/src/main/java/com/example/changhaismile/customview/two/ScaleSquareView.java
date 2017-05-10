package com.example.changhaismile.customview.two;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by changhaismile on 2017/4/19.
 */

public class ScaleSquareView extends View {
    private static final int TOTAL_SQUARE_COUNT = 15;
    private Paint mPaint;
    private int mTotalWidth, mTotalHeight;
    private int mHalfWidth, mHalfHeight;
    private int mLineWidth, mHalfLineWidth;
    private Rect mSquareRect;

    public ScaleSquareView(Context context) {
        super(context);
        mLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getResources().getDisplayMetrics());
        mHalfLineWidth = mLineWidth;
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //画笔设置为空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(mLineWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSquare(canvas);
    }

    private void drawSquare(Canvas canvas) {
        for (int i = 0;i < TOTAL_SQUARE_COUNT; i ++) {
            canvas.save();
            float fraction = (float) i / TOTAL_SQUARE_COUNT;
            canvas.scale(fraction, fraction, mHalfWidth, mHalfHeight);
            canvas.drawRect(mSquareRect, mPaint);
            canvas.restore();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mHalfWidth = w / 2;
        mHalfHeight = h / 2;
        int top = mHalfHeight - mHalfWidth;
        mSquareRect = new Rect(mHalfLineWidth, top,
                mTotalWidth - mHalfLineWidth, top + mTotalWidth);
    }
}
