package cn.dogplanet.app.widget.bannerViewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.widget.layout.SlideItem;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.net.volley.toolbox.ListImageListener;
import cn.dogplanet.ui.shop.ProductBuyActivity;

/**
 * Created by junweiliu on 16/6/14.
 * VP适配器
 */
public class BannerPagerAdapter extends PagerAdapter {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图像列表
     */
    private List<SlideItem> pictureList;
    /**
     * 默认轮播个数
     */
    public static final int FAKE_BANNER_SIZE = 10000;

    public BannerPagerAdapter(Context context, List<SlideItem> pictureList) {
        this.mContext = context;
        this.pictureList = pictureList;
    }

    public void clear() {
        pictureList.clear();
        notifyDataSetChanged();
    }

    public void setPic(List<SlideItem> pictureList) {
        this.pictureList = pictureList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return FAKE_BANNER_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_banner_item);
        // 获取当前显示位置
        position %= pictureList.size();
        imageView.setTag(pictureList.get(position).image);
        ListImageListener imageListener = new ListImageListener(
                imageView, WConstant.IMG_DEF_RES_ID,
                WConstant.IMG_ERROR_RES_ID, pictureList.get(position).image);
        GlobalContext.getInstance().getImageLoader()
                .get(pictureList.get(position).image, imageListener);
        if(pictureList.get(position).value!=null){
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else{
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        int finalPosition = position;
        imageView.setOnClickListener(v -> mContext.startActivity(ProductBuyActivity.newIntent(pictureList.get(finalPosition).value)));
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
