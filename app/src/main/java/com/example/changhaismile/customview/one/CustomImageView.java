package com.example.changhaismile.customview.one;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.changhaismile.customview.R;

/**
 * Created by changhaismile on 2017/4/20.
 */

public class CustomImageView extends View{
    private static final int IAMGE_SCALE_FILXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;
    /**文本*/
    private String mTitleText;
    /**文本的颜色*/
    private int mTitleTextColor;
    /**文本大小*/
    private int mTitleTextSize;
    /**绘制时控制文本绘制的范围*/
    private Rect rect, mTextBound;
    private Paint mPaint;

    private Bitmap mImage;
    private int mImageScale;

    private int width, height;


    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomImageView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i ++) {
            int attr = a.getIndex(i);

            switch (attr) {
                case R.styleable.CustomImageView_image:
//                    mImage = ((BitmapDrawable)getResources().getDrawable(a.getResourceId(attr, 0))).getBitmap();
                    mImage = BitmapFactory.decodeResource(context.getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = a.getInt(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    mTitleTextSize = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    16,
                                    getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        initPaint();
    }

    private void initPaint() {
        rect = new Rect();
        mPaint = new Paint();
        mTextBound = new Rect();
        mPaint.setTextSize(mTitleTextSize);
        //计算描绘字体需要的范围
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            Log.i("log", "EXACTLY");
            width = specSize;
        } else {
            //由图片决定宽
            int desireByImg = getPaddingLeft() + getPaddingRight()
                    + mImage.getWidth();
            //由字体决定宽
            int desireByTitle = getPaddingLeft() + getPaddingRight()
                    + mTextBound.width();
            if (specMode == MeasureSpec.AT_MOST) {
                //wrap_content
                int desire = Math.max(desireByImg, desireByTitle);
                width = Math.min(desire, specSize);
                Log.i("log", "AT_MOST");
            }
        }


        /***
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {  // match_parent , accurate
            height = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mTextBound.height();
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                height = Math.min(desire, specSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //边框
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        rect.left = getPaddingLeft();
        rect.right = width - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = height - getPaddingBottom();

        mPaint.setColor(mTitleTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx
         */
        if (mTextBound.width() > width) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitleText, paint, width - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), height - getPaddingBottom(), mPaint);
        } else {
            //正常显示，字体居中
            canvas.drawText(mTitleText, width / 2 - mTextBound.width() * 1.0f / 2, height - getPaddingBottom(), mPaint);
        }

        rect.bottom -= mTextBound.height();
        if (mImageScale == IAMGE_SCALE_FILXY) {
            canvas.drawBitmap(mImage, null, rect, mPaint);
        } else {
            //计算居中的矩形范围
            rect.left = width / 2 - mImage.getWidth() / 2;
            rect.right = width / 2 + mImage.getWidth() / 2;
            rect.top = (height - mTextBound.height()) / 2 - mImage.getHeight() / 2;
            rect.bottom = (height - mTextBound.height()) / 2 + mImage.getHeight() / 2;

            canvas.drawBitmap(mImage, null, rect, mPaint);
        }
    }
}
