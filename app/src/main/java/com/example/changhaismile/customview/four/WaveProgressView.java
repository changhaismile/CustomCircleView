package com.example.changhaismile.customview.four;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.changhaismile.customview.BuildConfig;
import com.example.changhaismile.customview.R;
import com.example.changhaismile.customview.three.DimenUtil;

/**
 * 水波进度条
 * Created by changhaismile on 2017/5/2.
 */

public class WaveProgressView extends View {

    private static final String TAG = WaveProgressView.class.getSimpleName();

    //浅色波浪方向
    private static final int L2R = 0;
    private static final int R2L = 1;

    private int mDefaultSize;
    //圆心
    private Point mCenterPoint;
    //半径
    private float mRadius;
    //圆的外接矩形
    private RectF mRectF;
    //深色波浪移动距离
    private float mDarkWaveOffset;
    //浅色波浪移动距离
    private float mLightWaveOffset;
    //浅色波浪方向
    private boolean isR2L;
    //是否锁定波浪不随进度移动
    private boolean lockWave;

    //是否开启抗锯齿
    private boolean antiAlias;
    //最大值
    private float mMaxValue;
    //当前值
    private float mValue;
    //当前进度
    private float mPercent;

    //绘制提示
    private TextPaint mHintPaint;
    private CharSequence mHint;
    private int mHintColor;
    private float mHintSize;

    private Paint mPercentPaint;
    private float mValueSize;
    private int mValueColor;

    //圆环宽度
    private float mCircleWidth;
    //圆环
    private Paint mCirclePaint;
    //圆环颜色
    private int mCircleColor;
    //背景圆环颜色
    private int mBgCircleColor;

    //水波路径
    private Path mWaveLimitPath;
    private Path mWavePath;
    //水波高度
    private float mWaveHeight;
    //水波数量
    private int mWaveNum;
    //深色水波
    private Paint mWavePaint;
    //深色水波颜色
    private int mDarkWaveColor;
    //浅色水波颜色
    private int mLightWaveColor;

    //深色水波贝塞尔曲线上的起始点、控制点
    private Point[] mDarkPoints;
    //浅色水波贝塞尔曲线上的起始点、控制点
    private Point[] mLightPoints;

    //贝塞尔曲线点的总个数
    private int mAllPointCount;
    private int mHalfPointCount;

    private ValueAnimator mProgressAnimator;
    private long mDarkWaveAnimTime;
    private ValueAnimator mDarkWaveAnimator;
    private long mLightWaveAnimTime;
    private ValueAnimator mLightWaveAnimator;
    public WaveProgressView(Context context) {
        this(context, null);
    }

    public WaveProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultSize = DimenUtil.dp2px(context, 150);
        mRectF = new RectF();
        mCenterPoint = new Point();
        initAttrs(context, attrs);
        initPaint();
        initPath();
        //开始动画
        setValue(mValue);
        startWaveAnimator();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveProgress);
        antiAlias = typedArray.getBoolean(R.styleable.WaveProgress_antiAlias, true);
        mDarkWaveAnimTime = typedArray.getInt(R.styleable.WaveProgress_darkWaveAnimTime, 1000);
        mLightWaveAnimTime = typedArray.getInt(R.styleable.WaveProgress_lightWaveAnimTime, 1000);
        mMaxValue = typedArray.getFloat(R.styleable.WaveProgress_maxValue, 100);
        mValue = typedArray.getFloat(R.styleable.WaveProgress_value, 50);
        mValueSize = typedArray.getDimension(R.styleable.WaveProgress_valueSize, 15);
        mValueColor = typedArray.getColor(R.styleable.WaveProgress_valueColor, Color.BLACK);

        mHint = typedArray.getString(R.styleable.WaveProgress_hint);
        mHintColor = typedArray.getColor(R.styleable.WaveProgress_hintColor, Color.BLACK);
        mHintSize = typedArray.getDimension(R.styleable.WaveProgress_hintSize, 15);

        mCircleWidth = typedArray.getDimension(R.styleable.WaveProgress_circleWidth, 15);
        mCircleColor = typedArray.getColor(R.styleable.WaveProgress_circleColor, Color.GREEN);
        mBgCircleColor = typedArray.getColor(R.styleable.WaveProgress_bgCircleColor, Color.WHITE);

        mWaveHeight = typedArray.getDimension(R.styleable.WaveProgress_waveHeight, 40);
        mWaveNum = typedArray.getInt(R.styleable.WaveProgress_waveNum, 1);
        mDarkWaveColor = typedArray.getColor(R.styleable.WaveProgress_darkWaveColor,
                getResources().getColor(android.R.color.holo_blue_dark));
        mLightWaveColor = typedArray.getColor(R.styleable.WaveProgress_lightWaveColor,
                getResources().getColor(android.R.color.holo_green_light));

        isR2L = typedArray.getInt(R.styleable.WaveProgress_lightWaveDirect, R2L) == R2L;
        lockWave = typedArray.getBoolean(R.styleable.WaveProgress_lockWave, false);
        typedArray.recycle();
    }

    private void initPaint() {
        mHintPaint = new TextPaint();
        mHintPaint.setAntiAlias(antiAlias);
        mHintPaint.setTextSize(mHintSize);
        mHintPaint.setColor(mHintColor);
        mHintPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(antiAlias);
        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(antiAlias);
        mWavePaint.setStyle(Paint.Style.FILL);

        mPercentPaint = new Paint();
        mPercentPaint.setTextAlign(Paint.Align.CENTER);
        mPercentPaint.setAntiAlias(antiAlias);
        mPercentPaint.setColor(mValueColor);
        mPercentPaint.setTextSize(mValueSize);
    }

    private void initPath() {
        mWaveLimitPath = new Path();
        mWavePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidthHeight(widthMeasureSpec, mDefaultSize), measureWidthHeight(heightMeasureSpec, mDefaultSize));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas);
        drawLightWave(canvas);
        drawDarkWave(canvas);
        drawProgress(canvas);
    }

    /**
     * 绘制圆环
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.save();
        int currentAngle = (int) (mPercent * 360);
        canvas.rotate(270, mCenterPoint.x, mCenterPoint.y);
        //画背景圆环
        mCirclePaint.setColor(mBgCircleColor);
        canvas.drawArc(mRectF, currentAngle, 360 - currentAngle, false, mCirclePaint);

        //画圆环
        mCirclePaint.setColor(mCircleColor);
        canvas.drawArc(mRectF, 0, currentAngle, false, mCirclePaint);
        canvas.restore();
    }

    /**
     * 绘制深色波浪(贝塞尔曲线)
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void drawDarkWave(Canvas canvas) {
        mWavePaint.setColor(mDarkWaveColor);
        drawWave(canvas, mWavePaint, mDarkPoints, mDarkWaveOffset);
    }

    /**
     * 绘制浅色波浪
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void drawLightWave(Canvas canvas) {
        mWavePaint.setColor(mLightWaveColor);
        //从右到左的水波位移应该被减去
        drawWave(canvas, mWavePaint, mLightPoints, isR2L ? -mLightWaveOffset : mLightWaveOffset);
    }

    /**
     * 绘制水波
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void drawWave(Canvas canvas, Paint paint, Point[] points, float waveOffset) {
        mWaveLimitPath.reset();
        mWavePath.reset();
        float height = lockWave ? 0 : mRadius - 2 * mRadius * mPercent;
        //moveTo和lineTo绘制出水波区域矩形
        mWavePath.moveTo(points[0].x + waveOffset, points[0].y + height);
        for (int i = 1; i < mAllPointCount; i += 2) {
            mWavePath.quadTo(points[i].x + waveOffset, points[i].y + height,
                    points[i + 1].x + waveOffset, points[i + 1].y + height);
        }
        mWavePath.lineTo(points[mAllPointCount - 1].x, points[mAllPointCount - 1].y + height);
        //不管如何移动，波浪与圆路径的交集底部永远固定，否则会造成上移的时候底部为空的情况
        mWavePath.lineTo(points[mAllPointCount - 1].x, mCenterPoint.y + mRadius);
        mWavePath.lineTo(points[0].x, mCenterPoint.y + mRadius);
        mWavePath.close();
        mWaveLimitPath.addCircle(mCenterPoint.x, mCenterPoint.y, mRadius, Path.Direction.CW);
        //取圆与波浪路径的交集，形成波浪在园内的效果
        mWaveLimitPath.op(mWavePath, Path.Op.INTERSECT);
        canvas.drawPath(mWaveLimitPath, paint);
    }

    /**
     * 绘制文字
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        float y = mCenterPoint.y - (mPercentPaint.descent() + mPercentPaint.ascent()) / 2;
        canvas.drawText(String.format("%.0f%%", mPercent * 100), mCenterPoint.x, y, mPercentPaint);
        if (mHint != null) {
            float hy = mCenterPoint.y * 2 / 3 - (mHintPaint.descent() + mHintPaint.ascent()) / 2;
            canvas.drawText(mHint.toString(), mCenterPoint.x, hy, mHintPaint);
        }
    }

    /**
     * 测量 View
     *
     * @param measureSpec
     * @param defaultSize View 的默认大小
     * @return
     */
    public int measureWidthHeight(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w = " + w + "; h = " + h + "; oldw = " + oldw + "; oldh = " + oldh);

        int minSize = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 2 * (int)mCircleWidth,
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - 2 * (int)mCircleWidth);
        mRadius = minSize / 2;
        mCenterPoint.x = getMeasuredWidth() / 2;
        mCenterPoint.y = getMeasuredHeight() / 2;
        //绘制圆弧的边界
        mRectF.left = mCenterPoint.x - mRadius - mCircleWidth / 2;
        mRectF.top = mCenterPoint.y - mRadius - mCircleWidth / 2;
        mRectF.right = mCenterPoint.x + mRadius + mCircleWidth / 2;
        mRectF.bottom = mCenterPoint.y + mRadius + mCircleWidth / 2;
        Log.d(TAG, "onSizeChanged: 控件大小 = " + "(" + getMeasuredWidth() + ", " + getMeasuredHeight() + ")"
                + ";圆心坐标 = " + mCenterPoint.toString()
                + ";圆半径 = " + mRadius
                + ";圆的外接矩形 = " + mRectF.toString());
        initWavePoints();
    }

    private void initWavePoints() {
        //当前波浪宽度
        float waveWidth = (mRadius * 2) / mWaveNum;
        mAllPointCount = 8 * mWaveNum + 1;
        mHalfPointCount = mAllPointCount / 2;
        mDarkPoints = getPoint(false, waveWidth);
        mLightPoints = getPoint(isR2L, waveWidth);
    }

    /**
     * 从左往右或者从右往左获取贝塞尔点
     * @param isR2L
     * @param waveWidth
     * @return
     */
    private Point[] getPoint(boolean isR2L, float waveWidth) {
        Point[] points = new Point[mAllPointCount];
        //第一个点特殊处理，即数组中点
        points[mHalfPointCount] = new Point((int)(mCenterPoint.x + (isR2L ? mRadius : -mRadius)), mCenterPoint.y);
        for(int i = mHalfPointCount + 1; i < mAllPointCount; i += 4) {
            float width = points[mHalfPointCount].x + waveWidth * (i / 4 - mWaveNum);
            points[i] = new Point((int)(waveWidth / 4 + width), (int)(mCenterPoint.y - mWaveHeight));
            points[i + 1] = new Point((int)((waveWidth / 2) + width), mCenterPoint.y);
            points[i + 2] = new Point((int)(waveWidth * 3 / 4 + width), (int)(mCenterPoint.y + mWaveHeight));
            points[i + 3] = new Point((int)(waveWidth + width), mCenterPoint.y);
        }
        //屏幕外的贝塞尔曲线点
        for (int i = 0; i < mHalfPointCount; i ++) {
            int reverse = mAllPointCount - i - 1;
            points[i] = new Point((isR2L ? 2 : 1) * points[mHalfPointCount].x - points[reverse].x,
                    points[mHalfPointCount].y * 2 - points[reverse].y);
        }
        //对从右到左的贝塞尔数组反序，方便后续处理
        return isR2L ? reverse(points) : points;
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    /**
     * 设置当前值
     *
     * @param value
     */
    public void setValue(float value) {
        if (value > mMaxValue) {
            value = mMaxValue;
        }
        float start = mPercent;
        float end = value / mMaxValue;
        startAnimator(start, end, mDarkWaveAnimTime);
    }

    /**
     * 开始动画
     * @param start
     * @param end
     * @param animTime
     */
    private void startAnimator(float start, float end, long animTime) {
        mProgressAnimator = ValueAnimator.ofFloat(start, end);
        mProgressAnimator.setDuration(animTime);
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();
                if (mPercent == 0.0f || mPercent == 1.0f) {
                    stopWaveAnimator();
                } else {
                    startWaveAnimator();
                }
                mValue = mPercent * mMaxValue;
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onAnimationUpdate: percent = " + mPercent
                            + ";value = " + mValue);
                }
                invalidate();
            }
        });
        mProgressAnimator.start();
    }

    private void startWaveAnimator() {
        startLightWaveAnimator();
        startDarkWaveAnimator();
    }

    private void stopWaveAnimator() {
        if (mDarkWaveAnimator != null && mDarkWaveAnimator.isRunning()) {
            mDarkWaveAnimator.cancel();
            mDarkWaveAnimator = null;
        }
        if (mLightWaveAnimator != null && mLightWaveAnimator.isRunning()) {
            mLightWaveAnimator.cancel();
            mLightWaveAnimator = null;
        }
    }

    private void startLightWaveAnimator() {
        if (mLightWaveAnimator != null && mLightWaveAnimator.isRunning()) {
            return;
        }
        mLightWaveAnimator = ValueAnimator.ofFloat(0, 2 * mRadius);
        mLightWaveAnimator.setDuration(mLightWaveAnimTime);
        mLightWaveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLightWaveAnimator.setInterpolator(new LinearInterpolator());
        mLightWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLightWaveOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mLightWaveAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLightWaveOffset = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mLightWaveAnimator.start();
    }

    private void startDarkWaveAnimator() {
        if (mDarkWaveAnimator != null && mDarkWaveAnimator.isRunning()) {
            return;
        }
        mDarkWaveAnimator = ValueAnimator.ofFloat(0, 2 * mRadius);
        mDarkWaveAnimator.setDuration(mDarkWaveAnimTime);
        mDarkWaveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mDarkWaveAnimator.setInterpolator(new LinearInterpolator());
        mDarkWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDarkWaveOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mDarkWaveAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mDarkWaveOffset = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mDarkWaveAnimator.start();
    }

    /**
     * 反转数组
     *
     * @param arrays
     * @param <T>
     * @return
     */
    public static <T> T[] reverse(T[] arrays) {
        if (arrays == null) {
            return null;
        }
        int length = arrays.length;
        for (int i = 0; i < length / 2; i++) {
            T t = arrays[i];
            arrays[i] = arrays[length - i - 1];
            arrays[length - i - 1] = t;
        }
        return arrays;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopWaveAnimator();
        if (mProgressAnimator != null && mProgressAnimator.isRunning()) {
            mProgressAnimator.cancel();
        }
    }
}


































