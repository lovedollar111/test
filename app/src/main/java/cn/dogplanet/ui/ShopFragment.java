package cn.dogplanet.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
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
import cn.dogplanet.app.util.Arithmetic;
import cn.dogplanet.app.util.FileUtils;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.PullToRefreshHelper;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.FloatingActionButton;
import cn.dogplanet.app.widget.NoScrollGridView;
import cn.dogplanet.app.widget.RoundCornerImageView;
import cn.dogplanet.app.widget.bannerViewPager.BannerPagerAdapter;
import cn.dogplanet.app.widget.bannerViewPager.BannerTimerTask;
import cn.dogplanet.app.widget.bannerViewPager.IndicatorView;
import cn.dogplanet.app.widget.layout.SlideItem;
import cn.dogplanet.app.widget.library.PullToRefreshBase.Mode;
import cn.dogplanet.app.widget.library.PullToRefreshScrollView;
import cn.dogplanet.base.BaseFragment;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.HomeResp;
import cn.dogplanet.entity.Product;
import cn.dogplanet.entity.ShareData;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.volley.toolbox.ListImageListener;
import cn.dogplanet.ui.popup.ShareHomePopupWindow;
import cn.dogplanet.ui.shop.ProductBuyActivity;
import cn.dogplanet.ui.shop.ProductFindActivity;
import cn.dogplanet.ui.shop.ProductListActivity;
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
    TextView etSearch;
    @BindView(R.id.img_input)
    ImageView imgInput;
    @BindView(R.id.lay_search)
    RelativeLayout laySearch;
    @BindView(R.id.scr_main)
    PullToRefreshScrollView scrMain;
    @BindView(R.id.btn_cart)
    FloatingActionButton btnCart;
    @BindView(R.id.lay_new_product)
    LinearLayout layNewProduct;
    @BindView(R.id.lay_main)
    RelativeLayout layMain;

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
    private ShareHomePopupWindow shareHomePopupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        bind = ButterKnife.bind(this, view);
        expert = WCache.getCacheExpert();
        scrMain.setMode(Mode.PULL_FROM_START);
        PullToRefreshHelper.initIndicatorStart(scrMain);
        scrMain.setOnRefreshListener(refreshView -> getHome());
        scrMain.setOnScrollListener(scrollY -> {
            if (scrollY < -3||scrollY>3) {
                btnCart.hide(true);
            } else {
                btnCart.hide(false);
            }
        });
        String qrCodeUrl = HttpUrl.GET_QR_CODE
                + "?expert_id=" + expert.getId();
        String qrCacheUrl = Arithmetic.getMD5Str(qrCodeUrl)
                + ".png";
        FileUtils fileUtils = new FileUtils(getActivity().getApplicationContext());
        String imgUrl;
        boolean isLocal;
        if (fileUtils.isFileExists(qrCacheUrl)) {
            imgUrl = fileUtils.getStorageDirectory()
                    + File.separator
                    + qrCacheUrl;
            isLocal = true;
        } else {
            imgUrl = qrCodeUrl;
            isLocal = false;
        }
        shareHomePopupWindow = new ShareHomePopupWindow(getContext(), imgUrl, isLocal);
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
            if (remProductAdapter == null) {
                remProductAdapter = new ProductAdapter(resp.getRecommendProduct(), getActivity().getApplicationContext());
                listRemProduct.setAdapter(remProductAdapter);
                listRemProduct.setOnItemClickListener((parent, view, position, id) -> startActivity(ProductBuyActivity.newIntent(resp.getRecommendProduct().get(position).getPro_id())));
            }else {
                remProductAdapter.clear();
                remProductAdapter.addAll(resp.getRecommendProduct());
            }
        } else {
            //todo
            if (remProductAdapter != null) {
                remProductAdapter.notifyDataSetChanged();
            }
        }
        listRemProduct.setFocusable(false);
        vpBanner.setFocusable(true);
        vpBanner.setFocusableInTouchMode(true);
        vpBanner.requestFocus();
        scrMain.smoothScrollTo(0,200);
        if (resp.getHotProduct() != null && !resp.getHotProduct().isEmpty()) {
            hotProductAdapter = new ProductAdapter(resp.getHotProduct(), getActivity().getApplicationContext());
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

    @OnClick({R.id.img_2vm, R.id.btn_cart, R.id.lay_ticket, R.id.lay_diving, R.id.lay_sea, R.id.lay_land, R.id.lay_other, R.id.lay_search, R.id.lay_new_product})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_ticket:
                startActivity(ProductListActivity.newIntent(Product.CATE_TICKET));
                break;
            case R.id.lay_diving:
                startActivity(ProductListActivity.newIntent(Product.CATE_DIVING));
                break;
            case R.id.lay_sea:
                startActivity(ProductListActivity.newIntent(Product.CATE_SEA));
                break;
            case R.id.lay_land:
                startActivity(ProductListActivity.newIntent(Product.CATE_LAND));
                break;
            case R.id.lay_other:
                startActivity(ProductListActivity.newIntent(Product.CATE_OTHER));
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
            case R.id.img_2vm:
                if (null != shareHomePopupWindow) {
                    String url = expert.getShop_url();
                    ShareData shareData = new ShareData();
                    shareData.setContent("我在汪汪星球，在这里为你提供最便宜的旅行产品，最优质的旅行服务，让你玩的更好，快来找我吧。"
                            + url);
                    shareData.setPic(expert.getExpert_icon());
                    shareData.setTitle("汪汪星球");
                    shareData.setUrl(url);
                    shareHomePopupWindow.initShareParams(shareData);
                    shareHomePopupWindow.showAtLocation(layMain,
                            Gravity.BOTTOM
                                    | Gravity.CENTER_HORIZONTAL, 0,
                            0);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

}
