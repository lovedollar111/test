package cn.dogplanet.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.iflytek.sunflower.FlowerCollector;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AppManager;
import cn.dogplanet.app.util.SystemStatusManager;
import cn.dogplanet.app.widget.CustomProgress;

/**
 * Activity 基类
 * editor:ztr
 * package_name:cn.dogplanet.base
 * file_name:BaseActivity.java
 * date:2016-12-6
 */
@SuppressLint("Registered")
public class BaseActivity extends Activity {

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);
        FlowerCollector.openPageMode(true);
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

    protected void showToast(int resId) {
        showToast(getResources().getString(resId));
    }

    @SuppressLint("ShowToast")
    protected void showToast(String text) {
        if (null == mToast) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public void showToast(String text, int duration) {
        if (null == mToast) {
            mToast = Toast.makeText(this, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    private CustomProgress mProgress;

    public void showProgress() {
        // if (null == mProgress) {
        mProgress = CustomProgress.show(this, getString(R.string.network_wait),
                true, null);
        // } else {
        // mProgress.show();
        // }
    }

    public void hideProgress() {
        if (null != mProgress && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    public void cancelToast() {
        if (null != mToast) {
            mToast.cancel();
        }
    }

    @Override
    protected void onResume() {
        FlowerCollector.onResume(getApplicationContext());
        FlowerCollector.onPageStart(this.getLocalClassName());
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        cancelToast();
        hideProgress();
        MobclickAgent.onPause(this);
        FlowerCollector.onPageEnd(this.getLocalClassName());
        FlowerCollector.onPause(this);
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalContext.getInstance().getRequestQueue().cancelAll(this);
        AppManager.getAppManager().finishActivity(this);
    }
}
