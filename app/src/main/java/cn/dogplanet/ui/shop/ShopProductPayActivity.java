package cn.dogplanet.ui.shop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.Order;
import cn.dogplanet.entity.OrderDetailResp;
import cn.dogplanet.entity.OrderMainEnum;
import cn.dogplanet.entity.OrderPayResp;
import cn.dogplanet.entity.PayResult;
import cn.dogplanet.entity.WXPayEntity;
import cn.dogplanet.entity.WXPayEntity.WXPay;
import cn.dogplanet.entity.ZFBPayEntity;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.order.OrderDetailActivity;
import cn.dogplanet.wxapi.Constants;

/**
 * 立即支付 editor:ztr
 * package_name:cn.dogplanet.ui.shop
 * file_name:ShopProductPayActivity.java
 * date:2016-12-6
 */
public class ShopProductPayActivity extends BaseActivity implements
        OnClickListener {

    private static final int PAY_TYPE_WX = 0;
    private static final int PAY_TYPE_ZFB = 1;

    private final static String ORDER_ARG = "ORDER_ARG";
    private Expert expert = null;
    private String orderId;
    private TextView order_num, price;

    private IWXAPI api;

    private Order order;


    private static final int SDK_PAY_FLAG = 1;

    private WXPayEntity wxPayEntity;
    private ZFBPayEntity zfbPayEntity;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult(
                            (Map<String, String>) msg.obj);
                    /*
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    final AlertDialog dialog = new AlertDialog.Builder(ShopProductPayActivity.this).create();
                    View view = LayoutInflater.from(getApplicationContext()).inflate(
                            R.layout.dialog_item, null);
                    TextView title = view.findViewById(R.id.title);
                    title.setText("支付结果通知");
                    TextView tv_msg = view.findViewById(R.id.msg);
                    view.findViewById(R.id.btn_ok).setOnClickListener(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    dialog.cancel();
                                }
                            });

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        getOrderDetail();
                    }
                    if (TextUtils.equals(resultStatus, "6001")) {
                        tv_msg.setText("取消支付");
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        tv_msg.setText("网络连接出错");
                    } else {
                        tv_msg.setText("支付失败");
                    }
                    if (!TextUtils.equals(resultStatus, "9000")) {
                        if (!isFinishing()) {
                            dialog.show();
                            dialog.setContentView(view);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    public static Intent newIntent(String oId) {
        Intent intent = new Intent(GlobalContext.getInstance(),
                ShopProductPayActivity.class);
        intent.putExtra(ORDER_ARG, oId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_product_pay);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);
        orderId = Objects.requireNonNull(getIntent().getExtras()).getString(ORDER_ARG);
        SharedPreferences sharedPreferences = getSharedPreferences(
                "shopPayMsg", Activity.MODE_PRIVATE);
        Editor edit = sharedPreferences.edit();
        edit.putString("orderId", orderId);
        edit.apply();
        expert = WCache.getCacheExpert();
        getOrder();
        initView();
    }


    private void initView() {
        this.findViewById(R.id.btn_back).setOnClickListener(this);
        RelativeLayout pay_wx = this.findViewById(R.id.pay_wx);
        RelativeLayout pay_zfb = this.findViewById(R.id.pay_zfb);
        order_num = this.findViewById(R.id.order_num);
        price = this.findViewById(R.id.tv_price);

        pay_wx.setOnClickListener(this);
        pay_zfb.setOnClickListener(this);
    }

    private void updateView(Order order) {
        if (null != order) {
            String fr = getString(R.string.shop_order_price);
            price.setText(String.format(fr, order.getPrice()));
            order_num.setText(order.getOrder_num());
        }
    }

    private void getOrder() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("id", orderId);
            showProgress();
            PublicReq.request(HttpUrl.GET_ORDER_DATA,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            OrderPayResp respData = GsonHelper.parseObject(
                                    response, OrderPayResp.class);
                            if (null != respData) {
                                if (respData.isSuccess()) {
                                    order = respData.getOrder();
                                    updateView(respData.getOrder());
                                } else {
                                    ToastUtil.showError(respData.getMsg());
                                }
                            } else {
                                ToastUtil
                                        .showError(R.string.network_data_error);
                            }
                            hideProgress();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgress();
                            ToastUtil.showError(R.string.network_error);
                        }
                    }, params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                startActivity(OrderDetailActivity.newIntent());
                finish();
                break;
            case R.id.pay_wx:
                getPayMsg(PAY_TYPE_WX);
                break;
            case R.id.pay_zfb:
                getPayMsg(PAY_TYPE_ZFB);
                break;
            default:
                break;
        }
    }

    private void getPayMsg(final int payType) {
        // TODO Auto-generated method stub
        String url;
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        params.put("order_id", orderId);
        if (payType == PAY_TYPE_WX) {
            url = HttpUrl.PAY_ORDER_BY_WX;
        } else {
            url = HttpUrl.PAY_ORDER_BY_ZFB;
        }
        PublicReq.request(url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        if (payType == PAY_TYPE_WX) {
                            wxPayEntity = GsonHelper.parseObject(response,
                                    WXPayEntity.class);
                            if (wxPayEntity != null && wxPayEntity.isSuccess()) {
                                goToPay(payType);
                            }
                        } else if (payType == PAY_TYPE_ZFB) {
                            zfbPayEntity = GsonHelper.parseObject(response, ZFBPayEntity.class);
                            if (zfbPayEntity != null && zfbPayEntity.isSuccess()) {
                                goToPay(payType);
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        ToastUtil.showError(R.string.network_error);
                    }
                }, params);
    }


    private void goToPay(final int payType) {
        if (payType == PAY_TYPE_WX) {
            PayReq req = new PayReq();
            WXPay result = wxPayEntity.getResult();
            req.appId = result.getAppid();
            req.partnerId = result.getPartnerid();
            req.prepayId = result.getPrepayid();
            req.nonceStr = result.getNoncestr();
            req.timeStamp = result.getTimestamp();
            req.packageValue = "Sign=WXPay";
            req.sign = result.getSign();
            api.sendReq(req);
        } else if (payType == PAY_TYPE_ZFB) {
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(
                            ShopProductPayActivity.this);
                    Map<String, String> result = alipay.payV2(zfbPayEntity.getResult(), true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    }

    private void getOrderDetail() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("id", orderId);
            PublicReq.request(HttpUrl.GET_ORDER_DETAIL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            final AlertDialog dialog = new AlertDialog.Builder(
                                    ShopProductPayActivity.this).create();
                            View view = LayoutInflater.from(
                                    getApplicationContext()).inflate(
                                    R.layout.dialog_item, null);
                            OrderDetailResp respData = GsonHelper.parseObject(
                                    response, OrderDetailResp.class);
                            if (respData != null && respData.isSuccess()) {
                                if (OrderMainEnum.OS_6.getStaus().equals(
                                        respData.getOrder().getStatus())) {
                                    Toast.makeText(ShopProductPayActivity.this,
                                            "支付成功", Toast.LENGTH_SHORT).show();
                                    startActivity(OrderDetailActivity
                                            .newIntent());
                                    finish();
                                } else {
                                    TextView title = view
                                            .findViewById(R.id.title);
                                    title.setText("支付结果通知");
                                    TextView msg = view
                                            .findViewById(R.id.msg);
                                    msg.setText("支付失败");
                                    view.findViewById(R.id.btn_ok)
                                            .setOnClickListener(
                                                    new OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                View v) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                    dialog.show();
                                    dialog.setContentView(view);
                                }
                            } else {
                                ToastUtil
                                        .showError(R.string.network_data_error);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtil.showError(R.string.network_error);
                        }
                    }, params);
        }
    }

}
