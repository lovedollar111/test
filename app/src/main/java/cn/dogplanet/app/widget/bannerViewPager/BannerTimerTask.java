package cn.dogplanet.app.widget.bannerViewPager;

import android.os.Handler;

import java.util.TimerTask;

import cn.dogplanet.ui.ShopFragment;

/**
 * Created by junweiliu on 16/6/15.
 */
public class BannerTimerTask extends TimerTask {
    /**
     * handler
     */
    Handler handler;

    public BannerTimerTask(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(ShopFragment.AUTOBANNER_CODE);
    }
}
