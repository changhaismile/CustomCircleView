package com.example.changhaismile.customview.two;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by changhaismile on 2017/4/19.
 * 画布缩放
 */

public class ScaleView extends View {
    private Paint mPaint;

    public ScaleView(Context context) {
        super(context);
        initPaint();
    }

    /**
     * 画笔初始化
     */
    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);
        canvas.drawRect(new Rect(0, 0, 400, 400), mPaint);
        //保存画布状态
        canvas.save();
        canvas.scale(0.5f, 0.5f);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(new Rect(0, 0, 400, 400), mPaint);
        //画布状态回滚
        canvas.restore();

        canvas.scale(0.5f, 0.5f, 200, 200);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(new Rect(0, 0, 400, 400), mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
