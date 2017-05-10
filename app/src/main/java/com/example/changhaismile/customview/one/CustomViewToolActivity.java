package com.example.changhaismile.customview.one;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewConfiguration;

import com.example.changhaismile.customview.R;
import com.orhanobut.logger.Logger;

/**
 * Created by changhaismile on 2017/4/11.
 */

public class CustomViewToolActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void lookNetDis() {
        //Configuration 用来描述设备的配置信息
        /**
         * 比如：用户配置信息，locale和scaling等
         *      设备的相关信息，输入模式，屏幕大小，屏幕方向等
         *
         * */
        Configuration configuration = getResources().getConfiguration();
        //获取国家码
        int countryCode = configuration.mcc;
        //获取网络码
        int networkCode = configuration.mnc;
        //判断横竖屏
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

        } else {

        }
        Logger.d("国家码" + countryCode + "-----" + "网络码" + networkCode);


        //ViewConfiguration 提供自定义控件的标准常量
        /**
         * 尺寸大小，滑动距离，敏感度
         */
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this);
        //获取touchSlop,表示系统所能识别的被认为是滑动的最小距离
        int touchSlop = viewConfiguration.getScaledTouchSlop();
        //获取Fling速度的最小值和最大值
        int minmumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        int maxmumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        //判断是否有物理按键
        boolean isHavePermanentMenuKey = viewConfiguration.hasPermanentMenuKey();
        Logger.d("系统认为滑动最小距离" + touchSlop + "fling速度的最小值和最大值" + minmumVelocity + "," + maxmumVelocity);

        //viewConfiguration 提供静态方法
        //双击间隔时间，在该时间内是双击，否则是单击
        int doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
        //按住状态转变为长按状态需要的时间
        int longPressTimeOut = ViewConfiguration.getLongPressTimeout();
        //重复按键的时间
        int keyRepeatTimeout = ViewConfiguration.getKeyRepeatTimeout();
    }
}
