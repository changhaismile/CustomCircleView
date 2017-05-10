package com.example.changhaismile.customview.two;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by changhaismile on 2017/4/18.
 */

public class FontView extends View{
    private static final String TEXT = "ap爱哥ξτβбпшㄎㄊěǔぬも┰┠№＠↓";
    private Paint paint;//画笔
    private Paint.FontMetrics mFontMetrics;//文本测量对象

    public FontView(Context context) {
        super(context);
        initPaint();
    }

    public FontView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FontView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);

        mFontMetrics = paint.getFontMetrics();

        Log.i("log", "ascent：" + mFontMetrics.ascent);
        Log.i("log", "top：" + mFontMetrics.top);
        Log.i("log", "leading：" + mFontMetrics.leading);
        Log.i("log", "descent：" + mFontMetrics.descent);
        Log.i("log", "bottom：" + mFontMetrics.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(TEXT, 0, Math.abs(mFontMetrics.top), paint);
    }
}
