package cn.dogplanet.ui.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.dogplanet.R;
import cn.dogplanet.entity.ShareData;
import cn.dogplanet.ui.popup.adapter.ShareAdapter;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 自定义分享面板
 * editor:ztr
 * package_name:cn.dogplanet.ui.popup
 * file_name:SharePopupWindow.java
 * date:2016-12-6
 */
public class SharePopupWindow extends PopupWindow {

    private View view;
    private Context context;
    private PlatformActionListener platformActionListener;
    private ShareParams shareParams;
    private String inviteCode;

    public SharePopupWindow(Context cx,String inviteCode) {
        this.context = cx;
        this.inviteCode=inviteCode;
        view = LayoutInflater.from(context)
                .inflate(R.layout.share_layout, null);
        GridView gridView = view.findViewById(R.id.share_gridview);
        TextView tvInviteCode=view.findViewById(R.id.tv_invite_code);
        tvInviteCode.setText(String.format("邀请码:%s", inviteCode));
        ShareAdapter adapter = new ShareAdapter(context, 1);
        gridView.setAdapter(adapter);
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.argb(140, 0, 0, 0));
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        view.setOnTouchListener((v, event) -> {
            int height = view.findViewById(R.id.pop_layout).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss();
                }
            }
            return true;
        });
        gridView.setOnItemClickListener(new ShareItemClickListener(this));
    }

    public PlatformActionListener getPlatformActionListener() {
        return platformActionListener;
    }

    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }

    public void showShareWindow() {

    }

    private class ShareItemClickListener implements OnItemClickListener {
        private PopupWindow pop;

        public ShareItemClickListener(PopupWindow pop) {
            this.pop = pop;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            share(position);
            pop.dismiss();
        }
    }

    /**
     * 分享
     *
     * @param position
     */
    private void share(int position) {
        if (position == 2) {
            qq();
        } else if(position==4){
            qzone();
        }else {
            Platform plat = null;
            plat = ShareSDK.getPlatform(context, getPlatform(position));
            if (platformActionListener != null) {
                plat.setPlatformActionListener(platformActionListener);
            }
            plat.share(shareParams);
        }
    }

    /**
     * 初始化分享参数
     */
    public void initShareParams(ShareData shareData) {
        if (shareData != null) {
            ShareParams sp = new ShareParams();
            sp.setShareType(Platform.SHARE_TEXT);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setTitle(shareData.getTitle());
            sp.setText(shareData.getContent());
            sp.setUrl(shareData.getUrl());
            sp.setImageUrl(shareData.getPic());
            shareParams = sp;
        }
    }

    /**
     * 获取平台
     *
     * @param position
     * @return
     */
    private String getPlatform(int position) {
        String platform = "";
        switch (position) {
            case 0:
                platform = Wechat.NAME;
                break;
            case 1:
                platform = WechatMoments.NAME;
                break;
            case 2:
                platform = QQ.NAME;
                break;
            case 3:
                platform = SinaWeibo.NAME;
                break;
            case 4:
                platform = QZone.NAME;
                break;
        }
        return platform;
    }

    /**
     * 分享到QQ空间
     */
    private void qzone() {
        ShareParams sp = new ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());
        Platform qzone = ShareSDK.getPlatform(context, QZone.NAME);
        qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
        // 执行图文分享
        qzone.share(sp);
    }

    private void qq() {
        ShareParams sp = new ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());
        Platform qq = ShareSDK.getPlatform(context, QQ.NAME);
        qq.setPlatformActionListener(platformActionListener);
        qq.share(sp);
    }

    private void sinaWeibo() {
        ShareParams sp = new ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());
        Platform qq = ShareSDK.getPlatform(context, SinaWeibo.NAME);
        qq.setPlatformActionListener(platformActionListener);
        qq.share(sp);
    }

}
