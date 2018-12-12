package cn.dogplanet.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.PullToRefreshHelper;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.app.widget.FloatingActionButton;
import cn.dogplanet.app.widget.MyScrollview;
import cn.dogplanet.app.widget.NoScrollGridView;
import cn.dogplanet.app.widget.RoundCornerImageView;
import cn.dogplanet.app.widget.bannerViewPager.BannerPagerAdapter;
import cn.dogplanet.app.widget.bannerViewPager.BannerTimerTask;
import cn.dogplanet.app.widget.bannerViewPager.IndicatorView;
import cn.dogplanet.app.widget.layout.SlideItem;
import cn.dogplanet.app.widget.library.PullToRefreshBase;
import cn.dogplanet.app.widget.library.PullToRefreshBase.Mode;
import cn.dogplanet.app.widget.library.PullToRefreshScrollView;
import cn.dogplanet.base.BaseFragment;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.HomeResp;
import cn.dogplanet.entity.Product;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.volley.toolbox.ListImageListener;
import cn.dogplanet.ui.shop.ProductBuyActivity;
import cn.dogplanet.ui.shop.ProductFindActivity;
import cn.dogplanet.ui.shop.ShopProductCartActivity;
import cn.dogplanet.ui.shop.adapter.ProductAdapter;

public class ShopFragment extends BaseFragment {


    public static final int AUTOBANNER_CODE = 0X1001;
    @BindView(R.id.vp_banner)
    ViewPager vpBanner;
    @BindView(R.id.idv_banner)
    IndicatorView idvBanner;
    @BindView(R.id.lay_ticket)
    LinearLayout layTicket;
    @BindView(R.id.lay_diving)
    LinearLayout layDiving;
    @BindView(R.id.lay_sea)
    LinearLayout laySea;
    @BindView(R.id.lay_land)
    LinearLayout layLand;
    @BindView(R.id.lay_other)
    LinearLayout layOther;
    @BindView(R.id.list_rem_product)
    NoScrollGridView listRemProduct;
    @BindView(R.id.img_new_product)
    RoundCornerImageView imgNewProduct;
    @BindView(R.id.tv_new_product_name)
    TextView tvNewProductName;
    @BindView(R.id.tv_new_product_price)
    TextView tvNewProductPrice;
    @BindView(R.id.tv_new_product_introduce)
    TextView tvNewProductIntroduce;
    @BindView(R.id.list_hot_product)
    NoScrollGridView listHotProduct;
    @BindView(R.id.img_2vm)
    ImageView img2vm;
    @BindView(R.id.et_search)
    EditTextWithDel etSearch;
    @BindView(R.id.img_input)
    ImageView imgInput;
    @BindView(R.id.lay_search)
    RelativeLayout laySearch;
    @BindView(R.id.scr_main)
    PullToRefreshScrollView scrMain;
    @BindView(R.id.btn_cart)
    FloatingActionButton btnCart;

    private Unbinder bind;
    private Expert expert;

    private ProductAdapter remProductAdapter, hotProductAdapter;
    private boolean mIsUserTouched = false;
    private Timer timer = new Timer();
    private BannerTimerTask mBannerTimerTask;

    Handler bannerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // 当用户点击时,不进行轮播
            if (!mIsUserTouched) {
                // 获取当前的位置
                int mBannerPosition = vpBanner.getCurrentItem();
                // 更换轮播图
                mBannerPosition = (mBannerPosition + 1) % BannerPagerAdapter.FAKE_BANNER_SIZE;
                vpBanner.setCurrentItem(mBannerPosition);
            }
            return true;
        }
    });
    private String new_product_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        bind = ButterKnife.bind(this, view);
        expert = WCache.getCacheExpert();
        scrMain.setMode(Mode.PULL_FROM_START);
        PullToRefreshHelper.initIndicator(scrMain);
        PullToRefreshHelper.initIndicatorStart(scrMain);
        scrMain.setOnRefreshListener(refreshView -> getHome());
        scrMain.setOnScrollListener(new MyScrollview.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if(scrollY>0){
                    btnCart.hide(true);
                }else{
                    btnCart.hide(false);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getHome();
    }

    private void getHome() {
        if (expert != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            PublicReq.request(HttpUrl.GET_FIRST_PAGE, response -> {
                if (StringUtils.isNotBlank(response)) {
                    scrMain.onRefreshComplete();
                    HomeResp resp = GsonHelper.parseObject(response, HomeResp.class);
                    if (resp != null) {
                        updateView(resp);
                    } else {
                        ToastUtil.showError(R.string.network_data_error);
                    }
                }
            }, error -> ToastUtil.showError(R.string.network_data_error), params);
        }
    }

    @SuppressLint("DefaultLocale")
    private void updateView(HomeResp resp) {
        if (resp.getBanner() != null && !resp.getBanner().isEmpty()) {
            SlideItem[] items = new SlideItem[resp.getBanner().size()];
            for (int i = 0; i < items.length; i++) {
                SlideItem item = new SlideItem();
                item.image = resp.getBanner().get(i).getImage_url();
                item.value = resp.getBanner().get(i).getUrl();
                items[i] = item;
            }
            notifyHomeAdapter(items);
        }
        if (resp.getRecommendProduct() != null && !resp.getRecommendProduct().isEmpty()) {
            remProductAdapter = new ProductAdapter(resp.getRecommendProduct(), getActivity());
            listRemProduct.setAdapter(remProductAdapter);
            listRemProduct.setOnItemClickListener((parent, view, position, id) -> startActivity(ProductBuyActivity.newIntent(resp.getRecommendProduct().get(position).getPro_id())));
        } else {
            //todo
            if (remProductAdapter != null) {
                remProductAdapter.notifyDataSetChanged();
            }
        }

        if (resp.getHotProduct() != null && !resp.getHotProduct().isEmpty()) {
            hotProductAdapter = new ProductAdapter(resp.getHotProduct(), getActivity());
            listHotProduct.setAdapter(hotProductAdapter);
            listHotProduct.setOnItemClickListener((parent, view, position, id) -> startActivity(ProductBuyActivity.newIntent(resp.getHotProduct().get(position).getPro_id())));
        }
        if (resp.getLatestProduct() != null && !resp.getLatestProduct().isEmpty()) {
            Product detail = resp.getLatestProduct().get(0);
            String url = detail.getUrl();
            if (StringUtils.isNotBlank(url)) {
                imgNewProduct.setTag(url);
                ListImageListener imageListener = new ListImageListener(imgNewProduct, WConstant.IMG_DEF_RES_ID, WConstant.IMG_ERROR_RES_ID, url);
                GlobalContext.getInstance().getImageLoader().get(url, imageListener);
            } else {
                imgNewProduct.setImageResource(WConstant.IMG_DEF_RES_ID);
            }
            tvNewProductIntroduce.setText(detail.getShort_comment());
            tvNewProductName.setText(detail.getName());
            tvNewProductPrice.setText(String.format("%d", detail.getPrice()));
            new_product_id = detail.getPro_id();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void notifyHomeAdapter(final SlideItem[] items) {
        List<SlideItem> pictureList = new ArrayList<>();
        pictureList.clear();
        Collections.addAll(pictureList, items);
        BannerPagerAdapter mBannerPagerAdapter = new BannerPagerAdapter(getActivity(), pictureList);
        vpBanner.setAdapter(mBannerPagerAdapter);
        idvBanner.setViewPager(pictureList.size(), vpBanner);
        // 设置默认起始位置,使开始可以向左边滑动
        vpBanner.setCurrentItem(pictureList.size() * 100);
        vpBanner.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN
                    || action == MotionEvent.ACTION_MOVE) {
                mIsUserTouched = true;
            } else if (action == MotionEvent.ACTION_UP) {
                mIsUserTouched = false;
            }
            return false;
        });
        startBannerTimer();
    }

    private void startBannerTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        if (mBannerTimerTask != null) {
            mBannerTimerTask.cancel();
        }
        mBannerTimerTask = new BannerTimerTask(bannerHandler);
        if (timer != null) {
            // 循环5秒执行
            timer.schedule(mBannerTimerTask, 5000, 5000);
        }
    }


    /**
     * 销毁时,关闭任务,防止异常
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBannerTimerTask) {
            mBannerTimerTask.cancel();
            mBannerTimerTask = null;
        }
    }

    @OnClick({R.id.btn_cart, R.id.lay_ticket, R.id.lay_diving, R.id.lay_sea, R.id.lay_land, R.id.lay_other, R.id.lay_search, R.id.lay_new_product})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_ticket:
                break;
            case R.id.lay_diving:
                break;
            case R.id.lay_sea:
                break;
            case R.id.lay_land:
                break;
            case R.id.lay_other:
                break;
            case R.id.lay_search:
                startActivity(ProductFindActivity.newIntent());
                break;
            case R.id.lay_new_product:
                startActivity(ProductBuyActivity.newIntent(new_product_id));
                break;
            case R.id.btn_cart:
                startActivity(ShopProductCartActivity.newIntent());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

}
