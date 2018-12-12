package cn.dogplanet.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.iflytek.sunflower.FlowerCollector;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AppManager;
import cn.dogplanet.app.util.SystemStatusManager;
import cn.dogplanet.app.widget.CustomProgress;

/**
 * FragmentActivity基类
 * editor:ztr
 * package_name:cn.dogplanet.base
 * file_name:BaseFragmentActivity.java
 * date:2016-12-6
 */

@SuppressLint("Registered")
public class BaseFragmentActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);
        FlowerCollector.openPageMode(true);
        FlymeSetStatusBarLightMode(getWindow(),false);
        setTranslucentStatus();
    }

    private void setTranslucentStatus()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);//状态栏无背景
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalContext.getInstance().getRequestQueue().cancelAll(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    private CustomProgress mProgress;

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        FlowerCollector.onResume(getApplicationContext());
        FlowerCollector.onPageStart(this.getLocalClassName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        FlowerCollector.onPageEnd(this.getLocalClassName());
        FlowerCollector.onPause(this);
        super.onPause();
    }

    public void showProgress() {
        mProgress = CustomProgress.show(this, getString(R.string.network_wait),
                true, null);
    }

    public void hideProgress() {
        if (null != mProgress && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
