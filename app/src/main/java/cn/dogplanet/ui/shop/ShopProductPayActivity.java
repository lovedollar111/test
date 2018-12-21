package cn.dogplanet.ui.shop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.OrderDetailResp;
import cn.dogplanet.entity.OrderMainEnum;
import cn.dogplanet.entity.OrderPayResp;
import cn.dogplanet.entity.PayResult;
import cn.dogplanet.entity.WXPayEntity;
import cn.dogplanet.entity.ZFBPayEntity;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.wxapi.Constants;


public class ShopProductPayActivity extends BaseActivity {


    private static final String PAY_BY_WX = "1";
    private static final String PAY_BY_ZFB = "2";

    private final static String ORDER_ID = "ORDER_ID";

    @BindView(R.id.btn_cancel)
    ImageView btnCancel;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.img_wx)
    ImageView imgWx;
    @BindView(R.id.img_zfb)
    ImageView imgZfb;
    @BindView(R.id.btn_pay)
    Button btnPay;

    private WXPayEntity wxPayEntity;
    private ZFBPayEntity zfbPayEntity;
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 1;

    private String orderId;

    private String payType = null;
    private Expert expert;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult(
                            (Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    final AlertDialog dialog = new AlertDialog.Builder(getApplicationContext()).create();
                    View view = LayoutInflater.from(getApplicationContext()).inflate(
                            R.layout.dialog_item, null);
                    TextView title = view.findViewById(R.id.title);
                    title.setText("支付结果通知");
                    TextView tv_msg = view.findViewById(R.id.msg);
                    view.findViewById(R.id.btn_ok).setOnClickListener(
                            new View.OnClickListener() {

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
        intent.putExtra(ORDER_ID, oId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_product_pay);
        ButterKnife.bind(this);
        expert = WCache.getCacheExpert();
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);
        orderId = getIntent().getStringExtra(ORDER_ID);
        getOrder();
    }

    private void getOrder() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("id", orderId);
            showProgress();
            PublicReq.request(HttpUrl.GET_ORDER_DATA,
                    response -> {
                        OrderPayResp respData = GsonHelper.parseObject(
                                response, OrderPayResp.class);
                        if (null != respData) {
                            if (respData.isSuccess()) {
                                updateView(respData);
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

    private void updateView(OrderPayResp respData) {
        if (StringUtils.isNotBlank(respData.getOrder().getPrice())) {
            tvPrice.setText(String.format("¥%s", respData.getOrder().getPrice()));
        }
    }


    @OnClick({R.id.lay_wx, R.id.lay_zfb, R.id.btn_pay, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_wx:
                payType = PAY_BY_WX;
                imgWx.setImageResource(R.drawable.ic_pay_select);
                imgZfb.setImageResource(R.drawable.ic_pay_normal);
                updateButton();
                break;
            case R.id.lay_zfb:
                payType = PAY_BY_ZFB;
                imgWx.setImageResource(R.drawable.ic_pay_normal);
                imgZfb.setImageResource(R.drawable.ic_pay_select);
                updateButton();
                break;
            case R.id.btn_pay:
                startPay();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void updateButton() {
        if (StringUtils.isNotBlank(payType)) {
            btnPay.setBackgroundResource(R.drawable.gradient_f1_e0);
            btnPay.setEnabled(false);
        } else {
            btnPay.setBackgroundResource(R.drawable.gradient_c7_ab);
            btnPay.setEnabled(true);
        }
    }

    private void startPay() {
        // TODO Auto-generated method stub
        String url;
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        params.put("order_id", orderId);
        if (payType.contains(PAY_BY_WX)) {
            url = HttpUrl.PAY_ORDER_BY_WX;
        } else {
            url = HttpUrl.PAY_ORDER_BY_ZFB;
        }
        PublicReq.request(url,
                response -> {
                    // TODO Auto-generated method stub
                    if (payType.contains(PAY_BY_WX)) {
                        wxPayEntity = GsonHelper.parseObject(response,
                                WXPayEntity.class);
                        if (wxPayEntity != null && wxPayEntity.isSuccess()) {
                            goToPay();
                        }
                    } else {
                        zfbPayEntity = GsonHelper.parseObject(response, ZFBPayEntity.class);
                        if (zfbPayEntity != null && zfbPayEntity.isSuccess()) {
                            goToPay();
                        }
                    }
                }, error -> {
                    // TODO Auto-generated method stub
                    ToastUtil.showError(R.string.network_error);
                }, params);
    }

    private void goToPay() {
        if (payType.contains(PAY_BY_WX)) {
            PayReq req = new PayReq();
            WXPayEntity.WXPay result = wxPayEntity.getResult();
            req.appId = result.getAppid();
            req.partnerId = result.getPartnerid();
            req.prepayId = result.getPrepayid();
            req.nonceStr = result.getNoncestr();
            req.timeStamp = result.getTimestamp();
            req.packageValue = "Sign=WXPay";
            req.sign = result.getSign();
            api.sendReq(req);
        } else {
            Runnable payRunnable = () -> {
                PayTask alipay = new PayTask(ShopProductPayActivity.this);
                Map<String, String> result = alipay.payV2(zfbPayEntity.getResult(), true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
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
                    response -> {
                        View view = LayoutInflater.from(this).inflate(R.layout.dialog_ok,
                                null);
                        NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
                        builder.setCustomView(view, this);
                        builder.withEffect(Effectstype.Fadein);
                        OrderDetailResp respData = GsonHelper.parseObject(
                                response, OrderDetailResp.class);
                        if (respData != null && respData.isSuccess()) {
                            if (OrderMainEnum.OS_6.getStaus().equals(
                                    respData.getOrder().getStatus())) {
                                Toast.makeText(ShopProductPayActivity.this,
                                        "支付成功", Toast.LENGTH_SHORT).show();
//                                startActivity(OrderDetailActivity
//                                        .newIntent(orderId));
//                                String type=WConstant.TYPE_BACK_MAIN;
//                                EventBus.getDefault().postSticky(type);
                                finish();
                            } else {
                                builder.show();
                                TextView title = view
                                        .findViewById(R.id.title);
                                title.setText("支付结果通知");
                                TextView msg = view
                                        .findViewById(R.id.msg);
                                msg.setText("支付失败");
                                view.findViewById(R.id.btn_ok)
                                        .setOnClickListener(
                                                v -> builder.dismiss());
                            }
                        } else {
                            ToastUtil
                                    .showError(R.string.network_data_error);
                        }
                    }, error -> ToastUtil.showError(R.string.network_error), params);
        }
    }
}

