package com.example.changhaismile.customview.two;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.util.AttributeSet;
import android.view.View;

import com.example.changhaismile.customview.R;

/**
 * Created by changhaismile on 2017/4/17.
 */

public class CanvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new RotateClockView(this));
    }

    class CustomView extends View {
        Paint paint;
        public CustomView(Context context) {
            super(context);

            /**
             * 设置一个笔刷大小是3的黄色画笔
             */
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(3);
        }

        public CustomView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            /**绘制圆形*/
//            canvas.drawCircle(100, 100, 90, paint);
            /**绘制弧形区域*/
//            RectF rectF = new RectF(0, 0, 100, 100);
//            canvas.drawArc(rectF,
//                    0, //开始角度
//                    90, //扫过的角度
//                    false, //是否使用中心
//                    paint);
            /**
             * 椭圆
             */
//            RectF rectF = new RectF(0, 0, 150, 100);
//            canvas.drawOval(rectF, paint);

            /**
             * 画线
             */
//            canvas.drawLine(0, 0, 100, 100, paint);
//            canvas.translate(0, 110);
//            //画一组线
//            float[] pts={0,0,100,0,
//                    100,0,100,100,
//                    100,100,0,100,
//                    1,100,1,0};
//            //线宽
//            paint.setStrokeWidth((float) 2.0);
//            canvas.drawLines(pts, 4, 12, paint);

            /**
             * 按照定点，绘制文本内容
             */
//            canvas.drawPosText("Android777", new float[]{
//                    10,10, //第一个字母在坐标10,10
//                    20,20, //第二个字母在坐标20,20
//                    30,30, //....
//                    40,40,
//                    50,50,
//                    60,60,
//                    70,70,
//                    80,80,
//                    90,90,
//                    100,100
//            }, paint);

            /**
             * 画点
             */
//            paint.setStrokeWidth((float)5.0);
//            canvas.drawPoint(50, 50, paint);
//            canvas.translate(0, 100);
//
//            float[] pst = {20, 20, 40, 40, 60, 60};
//            paint.setColor(Color.RED);
//            canvas.drawPoints(pst, paint);
//            canvas.translate(0, 100);
//            //跳过前两个元素
//            canvas.drawPoints(pst, 2, 4, paint);

            /**
             * 矩形
             */

//            canvas.drawRect(0, 0, 100, 100, paint);
//            canvas.translate(0, 110);
//
//            canvas.drawRect(new RectF(0, 0, 100, 100), paint);

            /**
             * 圆角矩形
             */
//            paint.setColor(Color.BLUE);
//            canvas.drawRoundRect(new RectF(0, 0, 100, 100), 10, 10, paint);

            /**
             * 画路径
             */
            //path对象
//            Path path = new Path();
//            //起始点
//            path.moveTo(10, 50);
//            //连接到下一点
//            path.lineTo(50, 50);
//            path.lineTo(10, 150);
//            path.lineTo(50, 100);
////            path.lineTo(50, 100);
//            paint.setColor(Color.RED);
//            canvas.drawPath(path, paint);

            /**
             * 画位图
             */
//            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
//            Rect rect = new Rect(0, 0, 100, 100);
//            RectF rectF = new RectF(0, 0, 20, 20);
//            canvas.drawBitmap(bitmap, rect, rectF, paint);

            /**
             * 绘制文字　
             */
//            paint.setTextSize(30);
//            Paint.FontMetrics fm = paint.getFontMetrics();
//            canvas.drawText("changhaismile", 0,
//                    (fm.bottom - fm.top)/2 , paint);

//            canvas.drawRect(new Rect(0, 0, 200, 200), paint);
//            canvas.scale(0.5f, 0.5f);//缩放
//            canvas.drawRect(new Rect(400, 400, 600, 600), paint);
//
//            canvas.translate(600, 600);//平移
//            canvas.rotate(45);
//            canvas.drawRect(new Rect(0, 0, 200, 200), paint);
//
//            canvas.translate(200, 200);
//            canvas.skew(0.5f, 0.5f);
//            canvas.drawRect(new Rect(0, 0, 200, 200), paint);

//            canvas.drawColor(Color.BLUE);
//            canvas.translate(100, 100);
//            canvas.drawRect(new Rect(0, 0, 400, 400), paint);
        }
    }
}
