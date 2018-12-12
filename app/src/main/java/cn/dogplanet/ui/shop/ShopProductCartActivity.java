package cn.dogplanet.ui.shop;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.CartResp;
import cn.dogplanet.entity.CartResp.Cart;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.ShopBuyDetail;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.ui.shop.adapter.CartAdapter;

/**
 * 购物车
 * editor:ztr
 * package_name:cn.dogplanet.ui.shop
 * file_name:ShopProductCartActivity.java
 * date:2016-12-6
 */
public class ShopProductCartActivity extends BaseActivity {


    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.price)
    TextView tvPrice;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.layout)
    LinearLayout layout;
    private Expert expert;
    private ArrayList<String> proId;
    private CartAdapter cartAdapter;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(),
                ShopProductCartActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expert = WCache.getCacheExpert();
        setContentView(R.layout.shop_product_cart);
        ButterKnife.bind(this);
        proId = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartData();
    }


    /**
     * 获取购物车数据
     */
    private void getCartData() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            showProgress();
            PublicReq.request(HttpUrl.GET_CART_PRODUCT,
                    response -> {
                        hideProgress();
                        CartResp respData = GsonHelper.parseObject(
                                response, CartResp.class);
                        if (null != respData) {
                            if (respData.isSuccess()) {
                                updateView(respData.getProduct());
                            } else {
                                ToastUtil.showError(respData.getMsg());
                            }
                        } else {
                            ToastUtil
                                    .showError(R.string.network_data_error);
                        }
                    }, error -> {
                        hideProgress();
                        ToastUtil.showError(R.string.network_error);
                    }, params);
        }
    }

    private void updateView(final List<Cart> carts) {
        if (carts != null && !carts.isEmpty()) {
            tvTip.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            if (cartAdapter == null) {
                cartAdapter = new CartAdapter(this, carts, this::updateMoney, (v, position) -> {
                    View view = LayoutInflater.from(ShopProductCartActivity.this).inflate(R.layout.dialog_ok,
                            null);
                    NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(ShopProductCartActivity.this);
                    builder.setCustomView(view, ShopProductCartActivity.this);
                    builder.withEffect(Effectstype.Fadein);
                    builder.show();
                    Button btn_ok = view.findViewById(R.id.btn_ok);
                    btn_ok.setText("确定");
                    btn_ok.setOnClickListener(v1 -> {
                        Cart cart = cartAdapter.getCarts()
                                .get(position);
                        delCartData(cart);
                        builder.dismiss();
                    });
                    TextView tv_title = view.findViewById(R.id.title);
                    tv_title.setText("提示");
                    tv_title.setTextSize(18);
                    tv_title.setTextColor(Color.rgb(40, 44, 54));
                    TextView tv_msg = view.findViewById(R.id.msg);
                    tv_msg.setText("是否删除该产品？");
                    tv_msg.setTextSize(12);
                    tv_msg.setTextColor(Color.rgb(72, 72, 72));
                    tv_msg.setPadding(AndroidUtil.dip2px(30), AndroidUtil.dip2px(10), AndroidUtil.dip2px(30), AndroidUtil.dip2px(2));
                });
                listview.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();
            } else {
                cartAdapter.clear();
                cartAdapter.addAll(carts);
            }
        } else {
            tvTip.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private void updateMoney() {
        proId.clear();
        proId.addAll(cartAdapter.getProId());
        tvPrice.setText(String.format("%s", cartAdapter.sumCartMoney()));
        if ("0".equals(cartAdapter.sumCartMoney())) {
            btnPay.setEnabled(false);
            btnPay.setBackgroundResource(R.drawable.gradient_c7_ab);
        } else {
            btnPay.setEnabled(true);
            btnPay.setBackgroundResource(R.drawable.gradient_f1_e0);
        }
    }

    private void delCartData(final Cart cart) {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("id", cart.getId());
            showProgress();
            PublicReq.request(HttpUrl.DEL_CART_PRODUCT,
                    response -> {
                        System.out.println(response);
                        RespData respData = GsonHelper.parseObject(
                                response, RespData.class);
                        if (null != respData) {
                            if (respData.isSuccess()) {
                                getCartData();
                            } else {
                                ToastUtil.showError(respData.getMsg());
                            }
                        } else {
                            ToastUtil
                                    .showError(R.string.network_data_error);
                        }
                        hideProgress();
                    }, error -> {
                        hideProgress();
                        ToastUtil.showError(R.string.network_error);
                    }, params);
        }
    }


    @OnClick({R.id.btn_back, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_pay:
                ShopBuyDetail detail = new ShopBuyDetail();
                detail.setType(ShopBuyDetail.TYPE_CART);
                detail.setIds(proId);
                EventBus.getDefault().postSticky(detail);
                break;
        }
    }
}
