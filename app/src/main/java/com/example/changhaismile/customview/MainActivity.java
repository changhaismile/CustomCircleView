package com.example.changhaismile.customview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.changhaismile.customview.five.CircleProgressTwoView;
import com.example.changhaismile.customview.four.CircleProgressView;
import com.example.changhaismile.customview.four.WaveBezier;
import com.example.changhaismile.customview.four.WaveProgressView;
import com.example.changhaismile.customview.three.SuperCircleView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private SuperCircleView progressBarOne;
    private SuperCircleView progressBarTwo;
    private SuperCircleView progressBarThree;
    private SuperCircleView progressBarFour;
    private WaveProgressView waveProgressView;
    private CircleProgressTwoView circleProgressTwoView;
    private final static int[] COLORS = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};

    private CircleProgressView mCircleProgress1, mCircleProgress2, mCircleProgress3;
    private Random mRandom;
    private Button mBtnResetAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_custom_image);
//        progressBarOne = (SuperCircleView) findViewById(R.id.am_progressbar_one);
//        progressBarTwo = (SuperCircleView) findViewById(R.id.am_progressbar_two);
//        progressBarThree = (SuperCircleView) findViewById(R.id.am_progressbar_three);
//        progressBarFour = (SuperCircleView) findViewById(R.id.am_progressbar_four);
//
//        progressBarOne.setProgress(20);
//        progressBarTwo.setProgress(40);
//        progressBarThree.setProgress(60);
//        progressBarFour.setProgress(80);

        setContentView(R.layout.activity_circle_progress);
        mBtnResetAll = (Button) findViewById(R.id.btn_reset_all);
        mCircleProgress1 = (CircleProgressView) findViewById(R.id.circle_progress_bar1);
        mCircleProgress2 = (CircleProgressView) findViewById(R.id.circle_progress_bar2);
        mCircleProgress3 = (CircleProgressView) findViewById(R.id.circle_progress_bar3);
        waveProgressView = (WaveProgressView) findViewById(R.id.wave_progress_bar);
        circleProgressTwoView = (CircleProgressTwoView) findViewById(R.id.circle_progress_view);
        mRandom = new Random();

        mCircleProgress1.setOnClickListener(this);
        mCircleProgress2.setOnClickListener(this);
        mCircleProgress3.setOnClickListener(this);
        mBtnResetAll.setOnClickListener(this);
        waveProgressView.setOnClickListener(this);
        circleProgressTwoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_all:
                mCircleProgress1.reset();
                mCircleProgress2.reset();
                mCircleProgress3.reset();
                break;
            case R.id.circle_progress_bar1:
                mCircleProgress1.setValue(mRandom.nextInt((int) mCircleProgress1.getMaxValue()));
                break;
            case R.id.circle_progress_bar2:
                mCircleProgress2.setValue(mRandom.nextFloat() * mCircleProgress2.getMaxValue());
                break;
            case R.id.circle_progress_bar3:
                //在代码中动态改变渐变色，可能会导致颜色跳跃
                mCircleProgress3.setGradientColors(COLORS);
                mCircleProgress3.setValue(mRandom.nextFloat() * mCircleProgress3.getMaxValue());
                break;
            case R.id.wave_progress_bar:
                waveProgressView.setValue(mRandom.nextFloat() * waveProgressView.getMaxValue());
                break;
            case R.id.circle_progress_view:
                float progress = (float) (Math.random() * 100);
                circleProgressTwoView.setProgress(progress);
                break;
        }
    }

    /**
     * 学习方法
     */
    private void learn() {

    }

    /**
     * master分支
     */
    private void master() {

    }
}
