package cn.dogplanet.ui.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.MainActivity;
import cn.dogplanet.R;
import cn.dogplanet.app.util.Arithmetic;
import cn.dogplanet.app.util.FileUtils;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.HoldTabScrollView;
import cn.dogplanet.app.widget.bannerViewPager.BannerPagerAdapter;
import cn.dogplanet.app.widget.bannerViewPager.BannerTimerTask;
import cn.dogplanet.app.widget.bannerViewPager.IndicatorView;
import cn.dogplanet.app.widget.layout.SlideItem;
import cn.dogplanet.base.BaseFragmentActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.OrderPayResp;
import cn.dogplanet.entity.ProductDetail;
import cn.dogplanet.entity.ProductResp;
import cn.dogplanet.entity.Resp;
import cn.dogplanet.entity.ShareData;
import cn.dogplanet.entity.ShopBuyDetail;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.ui.popup.ShareHomePopupWindow;

public class ProductBuyActivity extends BaseFragmentActivity implements HoldTabScrollView.OnHoldTabScrollViewScrollChanged {

    public static final String PRODUCT_ID = "product_id";

    @BindView(R.id.img_price)
    ImageView imgPrice;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.img_rem)
    ImageView imgRem;
    @BindView(R.id.tv_rem)
    TextView tvRem;
    @BindView(R.id.btn_join_cart)
    Button btnJoinCart;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.lay_bottom)
    LinearLayout layBottom;
    @BindView(R.id.vp_banner)
    ViewPager vpBanner;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.lay_product)
    LinearLayout layProduct;
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.btn_share)
    ImageButton btnShare;
    @BindView(R.id.img_product_buy)
    ImageView imgProductBuy;
    @BindView(R.id.tv_product_buy)
    TextView tvProductBuy;
    @BindView(R.id.img_product_detail)
    ImageView imgProductDetail;
    @BindView(R.id.tv_product_detail)
    TextView tvProductDetail;
    @BindView(R.id.fragment_product)
    FrameLayout fragmentProduct;
    @BindView(R.id.lay_product_buy)
    LinearLayout layProductBuy;
    @BindView(R.id.lay_product_detail)
    LinearLayout layProductDetail;
    @BindView(R.id.btn_back_top)
    ImageButton btnBackTop;
    @BindView(R.id.btn_product_buy)
    TextView btnProductBuy;
    @BindView(R.id.btn_product_detail)
    TextView btnProductDetail;
    @BindView(R.id.btn_share_top)
    ImageButton btnShareTop;
    @BindView(R.id.lay_top)
    RelativeLayout layTop;
    @BindView(R.id.lay_choose)
    LinearLayout layChoose;
    @BindView(R.id.lay_center)
    LinearLayout layCenter;
    @BindView(R.id.ht_scrollView)
    HoldTabScrollView htScrollView;
    @BindView(R.id.lay_main)
    RelativeLayout layMain;
    @BindView(R.id.idv_banner)
    IndicatorView idvBanner;


    private int mHeight = 0;

    private String product_id;
    private Expert expert;

    private ProductBuyFragment productBuyFragment;
    private ProductDetailFragment productDetailFragment;

    private boolean mIsUserTouched = false;
    private Timer timer = new Timer();
    private BannerTimerTask mBannerTimerTask;

    private boolean is_rem = false;//是否是推荐产品

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
    private int price;
    private String category;
    private String order_date;
    private int order_num;
    private ProductDetail product;
    private ShareHomePopupWindow shareHomePopupWindow;

    public static Intent newIntent(String productId) {
        Intent intent = new Intent(GlobalContext.getInstance(), ProductBuyActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        FragmentManager fManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            productBuyFragment = (ProductBuyFragment) fManager.findFragmentByTag("productBuyFragment");
            productDetailFragment = (ProductDetailFragment) fManager.findFragmentByTag("productDetailFragment");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_buy);
        ButterKnife.bind(this);
        product_id = getIntent().getStringExtra(PRODUCT_ID);
        expert = WCache.getCacheExpert();
        getProduct();
        showBuy();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        if (productBuyFragment == null) {
            productBuyFragment = new ProductBuyFragment();
            transaction
                    .add(R.id.fragment_product, productBuyFragment, "productBuyFragment");
        } else {
            transaction.show(productBuyFragment);
        }
        transaction.commitAllowingStateLoss();
        initPopup();
    }

    private void initPopup() {
        String qrCodeUrl = HttpUrl.GET_QR_CODE
                + "?expert_id=" + expert.getId();
        String qrCacheUrl = Arithmetic.getMD5Str(qrCodeUrl)
                + ".png";
        FileUtils fileUtils = new FileUtils(this);
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
        shareHomePopupWindow = new ShareHomePopupWindow(this, imgUrl, isLocal);

    }

    private void getProduct() {
        if (expert != null) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("pro_id", product_id);
            PublicReq.request(HttpUrl.GET_PRI_PRODUCT, response -> {
                if (StringUtils.isNotBlank(response)) {
                    ProductResp resp = GsonHelper.parseObject(response, ProductResp.class);
                    if (resp != null) {
                        if (resp.isSuccess()) {
                            product = resp.getProduct();
                            updateView();
                        }
                    } else {
                        ToastUtil.showError(R.string.network_data_error);
                    }

                }
            }, error -> {
                ToastUtil.showError(R.string.network_error);
            }, params);

        }
    }

    private void updateView() {
        EventBus.getDefault().postSticky(product);
        htScrollView.setOnObservableScrollViewScrollChanged(this);
        category = String.valueOf(product.getCategory());
        tvProductPrice.setText(product.getPrice().toString());
        tvProductName.setText(product.getName());
        tvPrice.setText("0");
        productBuyFragment.setInfoListener(new ProductBuyFragment.SetInfoListener() {
            @Override
            public void setDate(String date) {
                order_date = date.trim();
            }

            @Override
            public void setNum(int num) {
                order_num = num;
                tvPrice.setText(String.valueOf(num * product.getPrice()));
                price = num * product.getPrice();
                if (num != 0) {
                    if (product.getCan_join_cart()) {
                        btnJoinCart.setVisibility(View.VISIBLE);
                        if (product.getCanBuyNum().getNumber() > 0 && product.getTravel_agency_status() && product.getAuthentication_status()) {
                            btnBuy.setEnabled(true);
                            btnJoinCart.setEnabled(true);
                            imgPrice.setImageResource(R.drawable.ic_price);
                            btnJoinCart.setBackgroundResource(R.drawable.gradient_btn_red_left);
                            btnBuy.setBackgroundResource(R.drawable.gradient_btn_red_right);
                            tvPrice.setTextColor(getResources().getColor(R.color.color_f5a623));
                        } else {
                            btnBuy.setEnabled(false);
                            btnJoinCart.setEnabled(false);
                            imgPrice.setImageResource(R.drawable.ic_price_normal);
                            tvPrice.setTextColor(getResources().getColor(R.color.color_8e));
                            btnJoinCart.setBackgroundResource(R.drawable.gradient_btn_gray_left);
                            btnBuy.setBackgroundResource(R.drawable.gradient_btn_gray_right);
                        }
                    } else {
                        btnJoinCart.setVisibility(View.GONE);
                        btnJoinCart.setBackgroundResource(R.drawable.gradient_btn_red_left);
                        if(product.isIs_package_ticket()){
                            if (product.getCanBuy().getStatus()&&product.getTravel_agency_status() && product.getAuthentication_status()) {
                                btnBuy.setEnabled(true);
                                btnJoinCart.setEnabled(true);
                                btnBuy.setBackgroundResource(R.drawable.gradient_f1_e0);
                                imgPrice.setImageResource(R.drawable.ic_price);
                                tvPrice.setTextColor(getResources().getColor(R.color.color_f5a623));
                            } else {
                                btnBuy.setEnabled(false);
                                btnJoinCart.setEnabled(false);
                                btnBuy.setBackgroundResource(R.drawable.gradient_c7_ab);
                                imgPrice.setImageResource(R.drawable.ic_price_normal);
                                tvPrice.setTextColor(getResources().getColor(R.color.color_8e));
                            }
                        }else{
                            if (product.getTravel_agency_status() && product.getAuthentication_status()) {
                                btnBuy.setEnabled(true);
                                btnJoinCart.setEnabled(true);
                                btnBuy.setBackgroundResource(R.drawable.gradient_f1_e0);
                                imgPrice.setImageResource(R.drawable.ic_price);
                                tvPrice.setTextColor(getResources().getColor(R.color.color_f5a623));
                            } else {
                                btnBuy.setEnabled(false);
                                btnJoinCart.setEnabled(false);
                                btnBuy.setBackgroundResource(R.drawable.gradient_c7_ab);
                                imgPrice.setImageResource(R.drawable.ic_price_normal);
                                tvPrice.setTextColor(getResources().getColor(R.color.color_8e));
                            }
                        }
                    }
                } else {
                    if (product.getCan_join_cart()) {
                        btnJoinCart.setVisibility(View.VISIBLE);
                        btnJoinCart.setBackgroundResource(R.drawable.gradient_btn_gray_left);
                        btnBuy.setBackgroundResource(R.drawable.gradient_btn_gray_right);
                    } else {
                        btnJoinCart.setVisibility(View.GONE);
                        btnJoinCart.setBackgroundResource(R.drawable.gradient_btn_gray_left);
                        btnBuy.setBackgroundResource(R.drawable.gradient_c7_ab);
                    }
                    btnBuy.setEnabled(false);
                    btnJoinCart.setEnabled(false);
                    imgPrice.setImageResource(R.drawable.ic_price_normal);
                    tvPrice.setTextColor(getResources().getColor(R.color.color_8e));
                }
            }
        });
        is_rem = product.getRecommend();
        if (is_rem) {
            imgRem.setImageResource(R.drawable.ic_rem_product_select);
            tvRem.setTextColor(getResources().getColor(R.color.color_f5a623));
        } else {
            imgRem.setImageResource(R.drawable.ic_btn_rem_product);
            tvRem.setTextColor(getResources().getColor(R.color.color_8e));
        }
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            SlideItem[] items = new SlideItem[product.getImages().size()];
            for (int i = 0; i < items.length; i++) {
                SlideItem item = new SlideItem();
                item.image = product.getImages().get(i);
                item.value = product.getImages().get(i);
                items[i] = item;
            }
            notifyHomeAdapter(items);
        }
        if (!product.getAuthentication_status()) {
            ToastUtil.showError(product.getAuthentication_message());
            return;
        }

        if (!product.getTravel_agency_status()) {
            ToastUtil.showError(product.getTravel_agency_message());
            return;
        }

        if (product.isIs_package_ticket()) {
            if (!product.getCanBuy().getStatus()) {
                int time = product.getCanBuy().getTime();
                int hour = time / 3600;
                int min = (time - hour * 3600) / 60;
                int second = time - hour * 3600 - min * 60;
                if (StringUtils.isNotBlank(product.getCanBuy().getMsg())) {
                    ToastUtil.showError(product.getCanBuy().getMsg());

                } else {
                    ToastUtil.showError(String.format("当前时段暂不可购买该产品，请%d小时%d分%d秒后再试", hour, min, second));
                }
                productBuyFragment.hideNumBtn(0, false);
            }else{
                productBuyFragment.hideNumBtn(product.getMost(),true);
            }
            btnJoinCart.setVisibility(View.GONE);
            btnJoinCart.setBackgroundResource(R.drawable.gradient_btn_gray_left);
            btnBuy.setBackgroundResource(R.drawable.gradient_c7_ab);
            btnJoinCart.setEnabled(false);
            btnBuy.setEnabled(false);
        } else {
            if (product.getCan_join_cart()) {
                if (productBuyFragment != null) {
                    productBuyFragment.hideNumBtn(product.getCanBuyNum().getNumber(), true);
                    btnJoinCart.setVisibility(View.VISIBLE);
                    btnJoinCart.setBackgroundResource(R.drawable.gradient_btn_gray_left);
                    btnBuy.setBackgroundResource(R.drawable.gradient_btn_gray_right);
                    btnJoinCart.setEnabled(false);
                    btnBuy.setEnabled(false);
                }
            }else {
                btnJoinCart.setVisibility(View.GONE);
                btnJoinCart.setBackgroundResource(R.drawable.gradient_btn_gray_left);
                btnBuy.setBackgroundResource(R.drawable.gradient_c7_ab);
                btnJoinCart.setEnabled(false);
                btnBuy.setEnabled(false);
            }
        }

    }


    @OnClick({R.id.lay_rem, R.id.btn_join_cart, R.id.btn_buy, R.id.btn_back, R.id.btn_share, R.id.lay_product_buy, R.id.lay_product_detail, R.id.btn_back_top, R.id.btn_product_buy, R.id.btn_product_detail, R.id.btn_share_top})
    public void onViewClicked(View view) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (view.getId()) {
            case R.id.btn_join_cart:
                joinCart();
                break;
            case R.id.btn_buy:
                buy();
                break;
            case R.id.btn_back:
            case R.id.btn_back_top:
                finish();
                break;
            case R.id.lay_product_buy:
            case R.id.btn_product_buy:
                showBuy();
                if (productBuyFragment == null) {
                    productBuyFragment = new ProductBuyFragment();
                    transaction
                            .add(R.id.fragment_product, productBuyFragment, "productBuyFragment");
                } else {
                    transaction.show(productBuyFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case R.id.lay_product_detail:
            case R.id.btn_product_detail:
                showDetail();
                if (productDetailFragment == null) {
                    productDetailFragment = new ProductDetailFragment();
                    transaction
                            .add(R.id.fragment_product, productDetailFragment, "productDetailFragment");
                } else {
                    transaction.show(productDetailFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case R.id.btn_share_top:
            case R.id.btn_share:
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
            case R.id.lay_rem:
                remProduct();
                break;
        }
    }

    private void showDetail() {
        imgProductDetail.setImageResource(R.drawable.ic_product_buy_select);
        imgProductBuy.setImageResource(R.drawable.ic_product_buy_normal);
        tvProductBuy.setTextColor(getResources().getColor(R.color.color_c7));
        tvProductDetail.setTextColor(getResources().getColor(R.color.color_33));
        btnProductBuy.setTextColor(getResources().getColor(R.color.color_c7));
        btnProductDetail.setTextColor(getResources().getColor(R.color.color_33));
    }

    private void showBuy() {
        imgProductDetail.setImageResource(R.drawable.ic_product_buy_normal);
        imgProductBuy.setImageResource(R.drawable.ic_product_buy_select);
        tvProductBuy.setTextColor(getResources().getColor(R.color.color_33));
        tvProductDetail.setTextColor(getResources().getColor(R.color.color_c7));
        btnProductBuy.setTextColor(getResources().getColor(R.color.color_33));
        btnProductDetail.setTextColor(getResources().getColor(R.color.color_c7));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (productBuyFragment != null) {
            transaction.hide(productBuyFragment);
        }
        if (productDetailFragment != null) {
            transaction.hide(productDetailFragment);
        }
    }


    private void buy() {
        ShopBuyDetail detail = new ShopBuyDetail();
        detail.setId(product.getProduct_id());
        detail.setCategory(String.valueOf(product.getCategory()));
        detail.setType(ShopBuyDetail.TYPE_DETAIL);
        detail.setChild_name(product.getName());
        detail.setImgUrl(product.getImages().get(0));
        detail.setNum(String.valueOf(order_num));
        detail.setTime(order_date);
        detail.setType(ShopBuyDetail.TYPE_DETAIL);
        detail.setPrice(String.valueOf(product.getPrice()));
        EventBus.getDefault().postSticky(detail);
        startActivity(PersonMsgActivity.newIntent());
    }

    private void joinCart() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            // 订单来源 ios ：10 ，android：20
            params.put("source", "20");
            params.put("pro_id", product_id);
            params.put("category", String.valueOf(category));
            final String url;
            url = HttpUrl.CREATE_ORDER;
            params.put("type", "2");
            params.put("num", String.valueOf(order_num));
            params.put("price", String.valueOf(price));
            params.put("begin_date", order_date);
            params.put("finish_date", order_date);
            PublicReq.request(url, response -> {
                OrderPayResp respData = GsonHelper.parseObject(response,
                        OrderPayResp.class);
                if (null != respData) {
                    if (respData.isSuccess()) {
                        ToastUtil.showMes("加入购物车成功");
                        startActivity(MainActivity.newIntent("3"));
                    } else {
                        ToastUtil.showError(respData.getMsg());
                    }
                } else {
                    ToastUtil.showError(R.string.network_data_error);
                }
            }, error -> ToastUtil.showError(R.string.network_error), params);
        }

    }

    private void remProduct() {
        if (expert != null) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("pro_id", product_id);
            params.put("category", "10");
            params.put("type", is_rem ? "20" : "10");
            PublicReq.request(HttpUrl.EDIT_RECOMMEND_PRODUCT, response -> {
                if (StringUtils.isNotBlank(response)) {
                    Resp resp = GsonHelper.parseObject(response, Resp.class);
                    if (resp != null) {
                        if (resp.isSuccess()) {
                            is_rem = !is_rem;
                            //todo
                            if (is_rem) {
                                imgRem.setImageResource(R.drawable.ic_rem_product_select);
                                tvRem.setTextColor(getResources().getColor(R.color.color_f5a623));
                            } else {
                                imgRem.setImageResource(R.drawable.ic_btn_rem_product);
                                tvRem.setTextColor(getResources().getColor(R.color.color_8e));
                            }
                        }
                        ToastUtil.showMes(resp.getMsg());
                    } else {
                        ToastUtil.showError(R.string.network_data_error);
                    }
                }
            }, error -> {
                ToastUtil.showError(R.string.network_error);
            }, params);

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void notifyHomeAdapter(final SlideItem[] items) {
        List<SlideItem> pictureList = new ArrayList<>();
        pictureList.clear();
        Collections.addAll(pictureList, items);
        BannerPagerAdapter mBannerPagerAdapter = new BannerPagerAdapter(this, pictureList);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //获取HeaderView的高度，当滑动大于等于这个高度的时候，需要把topView移除当前布局，放入到外层布局
            mHeight = layCenter.getTop();
        }
    }

    @Override
    public void onObservableScrollViewScrollChanged(int l, int t, int oldl, int oldt) {
        if (t >= mHeight) {
            if (layTop.getVisibility() == View.GONE) {
                layTop.setVisibility(View.VISIBLE);
                layChoose.setVisibility(View.GONE);
            }
        } else {
            if (layTop.getVisibility() == View.VISIBLE) {
                layTop.setVisibility(View.GONE);
                layChoose.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mBannerTimerTask) {
            mBannerTimerTask.cancel();
            mBannerTimerTask = null;
        }
    }


}
