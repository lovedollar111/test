package cn.dogplanet.ui.shop;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.ComUtils;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.IdcardUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.CartResp;
import cn.dogplanet.entity.CartResp.Cart;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.OrderPayResp;
import cn.dogplanet.entity.ShopBuyDetail;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.ui.shop.adapter.ShopBuyAdapter;

/**
 * 联系人信息
 * editor:ztr
 * package_name:cn.dogplanet.ui.shop
 * file_name:PersonMsgActivity.java
 * date:2016-12-6
 */
public class PersonMsgActivity extends BaseActivity implements OnClickListener {


    @BindView(R.id.shop_list)
    ListView shopList;

    EditTextWithDel etName;
    EditTextWithDel etPhone;
    EditTextWithDel etIdcard;

    private Expert expert;
    private ShopBuyAdapter adapter;

    protected boolean canBuy = true;
    private String version_number;
    private String pro_id;
    private String category;
    private String num;
    private String date;
    private String type;
    private ArrayList<String> ids;
    private List<Cart> cart;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(),
                PersonMsgActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_msg);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        expert = WCache.getCacheExpert();
        PackageManager pm = getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            version_number = pi.versionName == null ? "null" : pi.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cart = new ArrayList<>();
        initTitle();
    }

    private void initTitle() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.listview_head, null);
        shopList.addHeaderView(view);
        etIdcard = view.findViewById(R.id.et_id_card);
        etPhone = view.findViewById(R.id.et_phone);
        etName = view.findViewById(R.id.et_name);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void productEventBus(ShopBuyDetail detail) {
        type = detail.getType();
        if (type.contains(ShopBuyDetail.TYPE_DETAIL)) {
            pro_id = detail.getId();
            category = detail.getCategory();
            num = detail.getNum();
            String price = detail.getPrice();
            date = detail.getTime();
            if (adapter != null) {
                adapter.clear();
                adapter.add(detail);
                adapter.notifyDataSetChanged();
            } else {
                adapter = new ShopBuyAdapter(this);
                adapter.add(detail);
                shopList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        } else {
            ids = detail.getIds();
            getCartData();
        }

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btn_pay) {
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();
            String idCard = etIdcard.getText().toString();
            if (StringUtils.isBlank(name)) {
                ToastUtil.showError(R.string.user_input_name_tip);
                return;
            }
            if (ComUtils.compileExChar(name)) {
                ToastUtil.showError("请输入正确的姓名");
                return;
            }
            if (StringUtils.isBlank(phone)) {
                ToastUtil.showError("请输入手机号");
                return;
            }
            if (!ComUtils.isMobileNo(phone)) {
                ToastUtil.showError(R.string.tip_phone);
                return;
            }
            if (!IdcardUtils.validateCard(idCard)) {
                ToastUtil.showError(R.string.user_input_card_tip);
                return;
            }
            if (type.contains(ShopBuyDetail.TYPE_DETAIL)) {
                createOrder();
            } else {
                createOrderByCart();
            }

        } else if (v.getId() == R.id.btn_back) {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            finish();
        }
    }

    private void createOrder() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            // 订单来源 ios ：10 ，android：20
            params.put("source", "20");
            params.put("pro_id", pro_id);
            params.put("category", category);
            final String url;
            url = HttpUrl.CREATE_ORDER;
            // 下单方式 直接下单：1 ，购物车下单：2
            params.put("type", ShopBuyDetail.TYPE_DETAIL);
            params.put("num", num);
            params.put("begin_date", date);
            params.put("finish_date", date);
            params.put("name", etName.getText().toString());
            params.put("phone", etPhone.getText().toString());
            params.put("id_card", etIdcard.getText().toString());
            params.put("version_number", version_number);
            showProgress();
            PublicReq.request(url, response -> {
                hideProgress();
                OrderPayResp respData = GsonHelper.parseObject(response,
                        OrderPayResp.class);
                if (null != respData) {
                    if (respData.isSuccess()) {
                        startActivity(ShopProductPayActivity
                                .newIntent(respData.getOrder().getId()));
                    } else {
                        ToastUtil.showError(respData.getMsg());
                    }
                } else {
                    ToastUtil.showError(R.string.network_data_error);
                }
            }, error -> {
                hideProgress();
                ToastUtil.showError(R.string.network_error);
            }, params);
        }
    }

    private void createOrderByCart() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            for (int i = 0; i < cart.size(); i++) {
                params.put("id[" + i + "]", cart.get(i).getId());
            }
            params.put("source", "20");
            // 下单方式 直接下单：1 ，购物车下单：2
            params.put("type", ShopBuyDetail.TYPE_CART);
            params.put("name", etName.getText().toString());
            params.put("phone", etPhone.getText().toString());
            params.put("id_card", etIdcard.getText().toString());
            params.put("version_number", version_number);
            showProgress();
            PublicReq.request(HttpUrl.CREATE_CART_ORDER,
                    response -> {
                        hideProgress();
                        OrderPayResp respData = GsonHelper.parseObject(
                                response, OrderPayResp.class);
                        if (null != respData) {
                            if (respData.isSuccess()) {
                                startActivity(ShopProductPayActivity
                                        .newIntent(respData.getOrder()
                                                .getId()));
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
                                ArrayList<ShopBuyDetail> details = new ArrayList<>();
                                for (int i = 0; i < ids.size(); i++) {
                                    for (int j = 0; j < respData
                                            .getProduct().size(); j++) {
                                        if (respData.getProduct().get(j)
                                                .getId()
                                                .equals(ids.get(i))) {
                                            cart.add(respData
                                                    .getProduct().get(j));
                                            Cart cart = respData
                                                    .getProduct().get(j);
                                            ShopBuyDetail buyDetail = new ShopBuyDetail();
                                            buyDetail.setChild_name(cart
                                                    .getName());
                                            buyDetail.setMain_name(cart
                                                    .getName());
                                            buyDetail.setPrice(cart
                                                    .getPrice());
                                            buyDetail.setNum(cart.getNum());
                                            buyDetail.setTime(cart
                                                    .getFinish_date());
                                            buyDetail.setImgUrl(cart
                                                    .getThumb());
                                            details.add(buyDetail);
                                        }
                                    }
                                }
                                if (adapter != null) {
                                    adapter.clear();
                                    adapter.addAll(details);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    adapter = new ShopBuyAdapter(this);
                                    adapter.addAll(details);
                                    shopList.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
