package com.example.changhaismile.customview.two;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by changhaismile on 2017/4/18.
 */

public class TextCenterView extends View{
    private static final String TEXT = "ap爱哥ξτβбпшㄎㄊ";
    private Paint textPaint, linePaint;//文本的画笔和中心线的画笔
    private int baseX, baseY;//baseline绘制的xy坐标

    public TextCenterView(Context context) {
        super(context, null);
        initPaint();
    }

    public TextCenterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextCenterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint() {
        //实例化画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(70);
        textPaint.setColor(Color.BLACK);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算baseline绘制的起点x轴坐标
        baseX = (int)(canvas.getWidth() / 2 - textPaint.measureText(TEXT) / 2);
        //计算baseline绘制的y坐标
        baseY = (int)((canvas.getHeight() / 2) - (textPaint.descent() + textPaint.ascent()) / 2);
        canvas.drawText(TEXT, baseX, baseY, textPaint);
        // 为了便于理解我们在画布中心处绘制一条中线
        canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, linePaint);
    }
}
