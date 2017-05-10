package com.example.changhaismile.customview.four;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.changhaismile.customview.R;

/**
 * Created by changhaismile on 2017/4/25.
 * 属性动画练习
 * 属性动画运行机制是通过不断的对值进行操作来实现的，初始值和结束值之间的动画过度就是由ValueAnimator这个类实现
 *
 */

public class ValueAnimationActivity extends AppCompatActivity{
    private Button btn;
    private TextView txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animator);
        btn = (Button) findViewById(R.id.button);
        txt = (TextView) findViewById(R.id.text);

//        valueAnimator();
        objectAnimator();
    }

    /**
     * ValueAnimator
     */
    private void valueAnimator() {
        final ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) anim.getAnimatedValue();
                Log.i("TAG", "cuurent value is " + currentValue);
            }
        });
        anim.start();
    }

    /**
     * ObjectAnimator
     */
    private void objectAnimator() {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textview在5秒中从常规变换成全透明，再从全透明变换成常规
//                ObjectAnimator animator = ObjectAnimator.ofFloat(txt, "alpha", 1f, 0f, 1f);
//                animator.setDuration(5000);
//                animator.start();
                //textview进行一次360度旋转
//                ObjectAnimator animator = ObjectAnimator.ofFloat(txt, "rotation", 0f, 360f);
//                animator.setDuration(5000);
//                animator.start();
                //textview先向左移出屏幕，然后在移回来

                //textview的getTranslationX()方法获取当前的textview的translationX的位置
//                float curTranslationX = txt.getTranslationX();
//                ObjectAnimator animator = ObjectAnimator.ofFloat(txt, "translationX", curTranslationX, -500f, curTranslationX);
//                animator.setDuration(5000);
//                animator.start();

                //textview进行缩放操作，垂直方向上放大3倍再还原
//                ObjectAnimator animator = ObjectAnimator.ofFloat(txt, "scaleY", 1f, 3f, 1f);
//                animator.setDuration(5000);
//                animator.start();

                ObjectAnimator moveIn = ObjectAnimator.ofFloat(txt, "translationX", -500f, 0f);
                ObjectAnimator rotate = ObjectAnimator.ofFloat(txt, "rotation", 0f, 360f);
                ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(txt, "alpha", 1f, 0f, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(rotate).with(fadeInOut).after(moveIn);
                animSet.setDuration(5000);
                animSet.start();
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

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
            }
        });
    }
}























