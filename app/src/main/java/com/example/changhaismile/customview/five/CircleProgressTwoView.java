package com.example.changhaismile.customview.five;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.changhaismile.customview.R;
import com.example.changhaismile.customview.three.DimenUtil;

/**圆形刻度进度条
 * Created by changhaismile on 2017/5/4.
 */

public class CircleProgressTwoView extends View {
    private static final String TAG = CircleProgressTwoView.class.getSimpleName();
    private Context mContext;
    private float mArcWidth;
    private RectF mRectF;
    /* 刻度个数 */
    private int mScaleCount;
    //起始，终止颜色
    private int mStartColor;
    private int mEndColor;
    private int[] mColorArray;

    //说明文字
    private Paint mLableTextPaint;
    private String mLableText;
    private float mLableTextSize;
    private int mTextColor;

    //百分比
    private Paint mProgressTextPaint;
    private float mProgressTextSize;

    //刻度线画笔
    private Paint mLinePaint;
    //背景弧线画笔
    private Paint mArcBackPaint;
    //百分比圆弧画笔
    private Paint mArcForePaint;
    //圆心坐标，半径
    private Point mCenterPoint;
    private float mRadius;
    /* 百分比 */
    private float mProgress;
    /* 百分比对应角度 */
    private float mSweepAngle;

    public CircleProgressTwoView(Context context) {
        this(context, null);
    }

    public CircleProgressTwoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressTwoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        init(attrs);
        setProgress(50);
    }

    private void init(AttributeSet attrs) {
        initAttrs(attrs);
        initPaint();
    }

    /**
     * 获取属性
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressTwoView);
        mArcWidth = typedArray.getDimension(R.styleable.CircleProgressTwoView_arcWidth, 8);
        mScaleCount = typedArray.getInt(R.styleable.CircleProgressTwoView_scaleCount, 24);
        mStartColor = typedArray.getColor(R.styleable.CircleProgressTwoView_startColor, Color.parseColor("#3FC199"));
        mEndColor = typedArray.getColor(R.styleable.CircleProgressTwoView_endColor, Color.parseColor("#3294C1"));
        mLableText = typedArray.getString(R.styleable.CircleProgressTwoView_labelText);
        mTextColor = typedArray.getColor(R.styleable.CircleProgressTwoView_textColor, Color.parseColor("#4F5F6F"));
        mLableTextSize = typedArray.getDimension(R.styleable.CircleProgressTwoView_labelTextSize, 64);
        mProgressTextSize = typedArray.getDimension(R.styleable.CircleProgressTwoView_progressTextSize, 160);
        mColorArray = new int[]{mStartColor, mEndColor};
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //说明文字
        mLableTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLableTextPaint.setStyle(Paint.Style.FILL);
        mLableTextPaint.setColor(mTextColor);
        mLableTextPaint.setTextSize(mLableTextSize);

        //百分比
        mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressTextPaint.setStyle(Paint.Style.FILL);
        mProgressTextPaint.setColor(mTextColor);
        mProgressTextPaint.setTextSize(mProgressTextSize);
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);

        //背景圆弧
        mArcBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcBackPaint.setStyle(Paint.Style.STROKE);
        mArcBackPaint.setStrokeWidth(mArcWidth);
        mArcBackPaint.setColor(Color.LTGRAY);

        //百分比圆弧
        mArcForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcForePaint.setStyle(Paint.Style.STROKE);
        mArcForePaint.setStrokeWidth(mArcWidth);
        mRectF = new RectF();
        mCenterPoint = new Point();

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(DimenUtil.dp2px(mContext, 2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureDimension(widthMeasureSpec),
                measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 500;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w = " + w + "; h = " + h + "; oldw = " + oldw + "; oldh = " + oldh);
        int minSize = Math.min(w - getPaddingLeft() - getPaddingRight() -  2 * (int)mArcWidth,
                h - getPaddingTop() - getPaddingBottom() - 2 * (int)mArcWidth);

        mRadius = minSize / 2;
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2;
        //绘制圆弧的边界
        mRectF.left = mCenterPoint.x - mRadius - mArcWidth / 2;
        mRectF.top = mCenterPoint.y - mRadius - mArcWidth / 2;
        mRectF.right = mCenterPoint.x + mRadius + mArcWidth / 2;
        mRectF.bottom = mCenterPoint.y + mRadius + mArcWidth / 2;
        Log.d(TAG, "onSizeChanged: 控件大小 = " + "(" + w + ", " + h + ")"
                + "圆心坐标 = " + mCenterPoint.toString()
                + ";圆半径 = " + mRadius
                + ";圆的外接矩形 = " + mRectF.toString());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas);
        drawText(canvas);
    }

    /**
     * 画背景圆弧
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        //画背景弧线
        canvas.drawArc(mRectF, -90, 360, false, mArcBackPaint);
        //设置渐变渲染
        SweepGradient mSweepGradient = new SweepGradient(mCenterPoint.x, mCenterPoint.y, mColorArray, null);
        mArcForePaint.setShader(mSweepGradient);
        //画百分比弧线
        canvas.drawArc(mRectF, -90, mSweepAngle, false, mArcForePaint);
    }

    /**
     * 画百分比
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        String progressText = mProgress + "%";
        Rect textRectF = new Rect();
        mProgressTextPaint.getTextBounds(progressText, 0, progressText.length(), textRectF);
        float progressTextWidth = textRectF.width();
        float progressTextHeight = textRectF.height();
        //画刻度线
        for (int i = 0; i < mScaleCount; i++) {
            canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, mArcWidth, mLinePaint);
            //旋转画布
            canvas.rotate(360 / mScaleCount, getWidth() / 2, getHeight() / 2);
        }
        canvas.drawText(progressText, mCenterPoint.x, mCenterPoint.y + progressTextHeight / 2, mProgressTextPaint);
        //画标签说明文本
        mLableTextPaint.getTextBounds(mLableText, 0, mLableText.length(), textRectF);
        canvas.drawText(mLableText, getWidth() / 2 - textRectF.width() / 2,
                getHeight() / 2 - progressTextHeight / 2 - textRectF.height(), mLableTextPaint);
    }

    public void setProgress(float progress) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mProgress, progress);
        valueAnimator.setDuration((long) (Math.abs(mProgress - progress) * 20));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                mSweepAngle = mProgress / 100 * 360 ;
                mProgress = (float) (Math.round(mProgress * 10)) / 10;//四舍五入保留到小数点后两位
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
