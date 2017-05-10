package com.example.changhaismile.customview.three;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.changhaismile.customview.R;

/**
 * Created by changhaismile on 2017/4/24.
 */

public class SuperCircleView extends View {
    /**进度的颜色*/
    private int outsideColor;
    /**外圆半径大小*/
    private float outsideRadius;
    /**背景颜色*/
    private int insideColor;
    /**圆环内文字颜色*/
    private int progressTextColor;
    /**圆环内文字大小*/
    private float progressTextSize;
    /**圆环的宽度*/
    private float progressWidth;
    /**最大进度*/
    private int maxProgress;
    /**当前进度*/
    private float progress;
    /**进度从哪个方向开始(上走下右)*/
    private int direction;

    private Paint paint;
    private String progressText;
    private Rect rect;

    enum DirectionEnum {
        LEFT(0, 180.0f),
        TOP(1, 270f),
        RIGHT(2, 0.0f),
        BOTTOM(3, 90.0f);

        private final int direction;
        private final float degree;

        DirectionEnum(int direction, float degree) {
            this.direction = direction;
            this.degree = degree;
        }

        public int getDirection() {
            return direction;
        }

        public float getDegree() {
            return degree;
        }

        public boolean equalsDescription(int direction) {
            return this.direction == direction;
        }

        public static DirectionEnum getDirection(int direction) {
            for (DirectionEnum enumObject : values()) {
                if (enumObject.equalsDescription(direction)) {
                    return enumObject;
                }
            }
            return RIGHT;
        }

        public static float getDegree(int direction) {
            DirectionEnum enumObject = getDirection(direction);
            if (enumObject == null) {
                return 0;
            }
            return enumObject.getDegree();
        }
    }

    public SuperCircleView(Context context) {
        this(context, null);
    }

    public SuperCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SuperCircleView, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i ++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SuperCircleView_outside_color:
                    outsideColor = a.getColor(attr, ContextCompat.getColor(context, R.color.colorPrimary));
                    break;
                case R.styleable.SuperCircleView_outside_radius:
                    outsideRadius = a.getDimension(attr, DimenUtil.dp2px(context, 60.0f));
                    break;
                case R.styleable.SuperCircleView_inside_color:
                    insideColor = a.getColor(attr, ContextCompat.getColor(context, R.color.inside_color));
                    break;
                case R.styleable.SuperCircleView_progress_text_color:
                    progressTextColor = a.getColor(attr, ContextCompat.getColor(context, R.color.colorPrimary));
                    break;
                case R.styleable.SuperCircleView_progress_text_size:
                    progressTextSize = a.getDimension(attr, DimenUtil.dp2px(context, 14.0f));
                    break;
                case R.styleable.SuperCircleView_progress_width:
                    progressWidth = a.getDimension(attr, DimenUtil.dp2px(context, 10.0f));
                    break;
                case R.styleable.SuperCircleView_progress:
                    progress = a.getFloat(attr, 50.0f);
                    break;
                case R.styleable.SuperCircleView_max_progress:
                    maxProgress = a.getInt(attr, 100);
                    break;
                case R.styleable.SuperCircleView_direction:
                    direction = a.getInt(attr, 3);
                    break;
            }
        }
        a.recycle();
        paint = new Paint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height, width;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = (int)((2 * outsideRadius) + progressWidth);
        }

        size = MeasureSpec.getSize(heightMeasureSpec);
        mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = (int) ((2 * outsideRadius) + progressWidth);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int circlePoint = getWidth() / 2;
        //第一步：画背景（内层圆）
        paint.setColor(insideColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(progressWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(circlePoint, circlePoint, outsideRadius, paint);

        //第二步：画进度（圆弧）
        paint.setColor(outsideColor);
        RectF oval = new RectF(circlePoint - outsideRadius, circlePoint - outsideRadius,
                circlePoint + outsideRadius, circlePoint + outsideRadius);
        canvas.drawArc(oval, DirectionEnum.getDegree(direction), 360 * (progress / maxProgress), false, paint);

        //第三步：画圆圈内百分比文字
        rect = new Rect();
        paint.setColor(progressTextColor);
        paint.setTextSize(progressTextSize);
        paint.setStrokeWidth(0);
        progressText = getProgressText();
        paint.getTextBounds(progressText, 0, progressText.length(), rect);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int baseLine = (int) ((getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);  //获得文字的基准线;
        canvas.drawText(progressText, getMeasuredWidth() / 2 - rect.width() / 2, baseLine, paint);
    }

    /**
     * 显示百分比
     * @return
     */
    private String getProgressText() {
        return (int)((progress / maxProgress) * 100) + "%";
    }

    //设置属性动画，动态改变百分比
    private void startAnim(float startProgress) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, startProgress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                SuperCircleView.this.progress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setDuration(2000);
        animator.setStartDelay(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public synchronized int getMaxProgress() {
        return maxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress < 0) {
            //此为传递非法参数异常
            throw new IllegalArgumentException("maxProgress should not be less than 0");
        }
        this.maxProgress = maxProgress;
    }

    //加锁保证线程安全,能在线程中使用
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress should not be less than 0");
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        startAnim(progress);
    }
}
