package cn.dogplanet.ui.popup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;

/**
 * 分享adapter
 * editor:ztr
 * package_name:cn.dogplanet.ui.popup.adapter
 * file_name:ShareAdapter.java
 * date:2016-12-6
 */
public class ShareAdapter extends BaseAdapter {
    private int type;
    private static String[] shareNames = new String[]{"微信", "朋友圈", "QQ",
            "微博", "空间"};
    private int[] shareIcons = new int[]{R.drawable.ic_wx,
            R.drawable.ic_wx_friend, R.drawable.ic_qq, R.drawable.ic_weibo, R.drawable.ic_qzone};
    private int[] shareIcons2 = new int[]{R.drawable.ic_wx,
            R.drawable.ic_wx_friend, R.drawable.ic_qq, R.drawable.ic_weibo, R.drawable.ic_qzone};

    private LayoutInflater inflater;

    public ShareAdapter(Context context, int type) {
        inflater = LayoutInflater.from(context);
        this.type=type;
    }

    @Override
    public int getCount() {
        return shareNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.share_item, null);
        }
        ImageView shareIcon = convertView
                .findViewById(R.id.share_icon);
        TextView shareTitle = convertView
                .findViewById(R.id.share_title);
        if(type==1){
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(AndroidUtil.dip2px(44), AndroidUtil.dip2px(44));
            shareIcon.setImageResource(shareIcons2[position]);
            shareIcon.setLayoutParams(layoutParams);
            shareTitle.setText(shareNames[position]);
        }else{
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(AndroidUtil.dip2px(30), AndroidUtil.dip2px(30));
            shareIcon.setImageResource(shareIcons[position]);
            shareIcon.setLayoutParams(layoutParams);
            shareTitle.setText(shareNames[position]);
        }
        return convertView;
    }

}
