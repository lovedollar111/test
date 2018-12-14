package cn.dogplanet.ui.order;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.MainActivity;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.MyScrollview;
import cn.dogplanet.app.widget.NoScrollListView;
import cn.dogplanet.app.widget.library.PullToRefreshBase;
import cn.dogplanet.app.widget.library.PullToRefreshScrollView;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.MaxRefundTicketResp;
import cn.dogplanet.entity.OrderDetail;
import cn.dogplanet.entity.OrderDetailResp;
import cn.dogplanet.entity.ProductDetailResp;
import cn.dogplanet.entity.Resp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.ui.order.adapter.OrderDetailAdapter;


/**
 * 订单详情
 * editor:ztr
 * package_name:cn.dogplanet.ui.order
 * file_name:OrderDetailActivity.java
 * date:2016-12-6
 */
public class OrderDetailActivity extends BaseActivity {

    private static final String ORDER_ID = "order_id";
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_id_card)
    TextView tvIdCard;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.list_sub_order)
    NoScrollListView listSubOrder;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.scroll_main)
    PullToRefreshScrollView scrollMain;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.lay_pay)
    LinearLayout layPay;
    private Expert expert;
    private String oid;
    private NiftyDialogBuilder builderCancel;
    private String backType;
    private String pro_id, pro_category;

    public static Intent newIntent(String oid) {
        Intent intent = new Intent(GlobalContext.getInstance(),
                OrderDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ORDER_ID, oid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        expert = WCache.getCacheExpert();
        oid = getIntent().getStringExtra(ORDER_ID);
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void stringEventBus(String type) {
        backType = type;
    }

    private void initView() {
        scrollMain.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        scrollMain.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<MyScrollview>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<MyScrollview> refreshView) {
                // TODO Auto-generated method stub
                getOrderDetail();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<MyScrollview> refreshView) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderDetail();
    }

    private void getOrderDetail() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", String.valueOf(expert.getId()));
            params.put("access_token", expert.getAccess_token());
            params.put("id", oid);

            PublicReq.request(HttpUrl.GET_ORDER_DETAIL,
                    response -> {
                        try {
                            scrollMain.onRefreshComplete();
                            OrderDetailResp respData = GsonHelper
                                    .parseObject(response,
                                            OrderDetailResp.class);
                            if (null != respData) {
                                updateView(respData.getOrder());
                            } else {
                                ToastUtil
                                        .showError(R.string.network_data_error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }, error -> ToastUtil.showError(R.string.network_error), params);
        }
    }

    private void updateView(OrderDetail order) {
        tvName.setText(order.getContact_name());
        tvPhone.setText(order.getContact_tel());
        tvIdCard.setText(order.getContact_id_card());
        tvMoney.setText(order.getPrice());
        tvOrderNum.setText(order.getOrder_num());
        tvTime.setText(order.getCreate_time());
        if (order.getProducts() != null && !order.getProducts().isEmpty()) {
            OrderDetailAdapter adapter = new OrderDetailAdapter(order.getProducts(), this, (id, num, date, isPackage, proId, proCategory) -> {
                if (isPackage) {
                    getTicketNum(date, id, num);
                } else {
                    showNumDialog(id, num);
                }
                pro_id = proId;
                pro_category = proCategory;
            });
            listSubOrder.setAdapter(adapter);
        }
        String status = order.getStatus();
        if (status.equals(OrderDetail.ORDER_TYPE_WAIT)) {
            btnCancel.setVisibility(View.VISIBLE);
            layPay.setVisibility(View.VISIBLE);
            tvPrice.setText(order.getPrice());
        } else {
            btnCancel.setVisibility(View.GONE);
            layPay.setVisibility(View.GONE);
        }
    }

    private void getTicketNum(String date, String pro_id, int num) {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("date", date);
            PublicReq.request(HttpUrl.GET_REFUND_TICKET,
                    response -> {
                        MaxRefundTicketResp parseObject = GsonHelper
                                .parseObject(response,
                                        MaxRefundTicketResp.class);
                        if (parseObject != null && parseObject.isSuccess()) {
                            if (num > parseObject.getNumber().getNumber()) {
                                showNumDialog(pro_id, parseObject.getNumber()
                                        .getNumber());
                            } else {
                                showNumDialog(pro_id, num);
                            }
                        }
                    }, error -> ToastUtil.showError(R.string.network_error), params);
        }
    }

    private void showNumDialog(final String id, final int num) {
        View view = LayoutInflater.from(this).inflate(R.layout.cancel_hint,
                null);
        NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
        builder.setCustomView(view, this);
        builder.withEffect(Effectstype.Fadein);
        builder.show();
        view.findViewById(R.id.lay_rule).setOnClickListener(v -> getRule());
        EditText etNum = view.findViewById(R.id.et_num);
        etNum.setHint("请输入退票张数(可退" + num + "张)");
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Integer.parseInt(s.toString()) > num) {
                    ToastUtil.showError("您当前最多可退" + num + "张票");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            String string = etNum.getText().toString();
            if (StringUtils.isBlank(string)
                    || Integer.parseInt(string) == 0) {
                ToastUtil.showError("请输入需要退款的订单数量");
                return;
            }
            if (Integer.parseInt(string) > num) {
                ToastUtil.showError("您当前最多可退" + num + "张票");
                return;
            }
            getCancel(id, etNum.getText().toString());
            builder.cancel();
        });
    }

    private void getRule() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("pro_id", pro_id);
            params.put("category", pro_category);
            PublicReq.request(HttpUrl.GET_PRODUCT_RETURNS,
                    response -> {
                        ProductDetailResp respData = GsonHelper.parseObject(
                                response, ProductDetailResp.class);
                        if (respData != null) {
                            if (respData.isSuccess()) {
                                View view = LayoutInflater.from(this).inflate(R.layout.dialog_ok,
                                        null);
                                NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
                                builder.setCustomView(view, this);
                                builder.withEffect(Effectstype.Fadein);
                                builder.show();
                                TextView tv_title = view.findViewById(R.id.title);
                                tv_title.setText("该产品退改规则如下");
                                TextView tv_msg = view.findViewById(R.id.msg);
                                String fromHtml;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    fromHtml = String.valueOf(Html.fromHtml(respData.getProduct().getReturns(), Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    fromHtml = String.valueOf(Html.fromHtml(respData.getProduct().getReturns()));
                                }
                                tv_msg.setText(fromHtml);
                                tv_msg.setLineSpacing(0, 0.96f);
                                tv_msg.setGravity(Gravity.START);
                                view.findViewById(R.id.btn_ok).setOnClickListener(
                                        v -> builder.dismiss());
                            } else {
                                ToastUtil.showMes(respData.getMsg());
                            }
                        }
                    }, error -> ToastUtil.showError(R.string.network_error), params);
        }
    }

    protected void getCancel(String id, String num) {
        // TODO Auto-generated method stub
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("id", id);
            params.put("number", num);
            PublicReq.request(HttpUrl.CANCEL_CHILD_ORDER,
                    response -> {
                        Resp parseObject = GsonHelper.parseObject(response,
                                Resp.class);
                        if (parseObject != null && parseObject.isSuccess()) {
                            ToastUtil.showMes("申请成功，请耐心等待");
                            getOrderDetail();
                        } else {
                            if (parseObject != null) {
                                ToastUtil.showMes(parseObject.getMsg());
                            }
                        }
                    }, error -> ToastUtil.showError(R.string.network_error), params);
        }

    }

    @OnClick({R.id.btn_back, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (StringUtils.isBlank(backType) || backType.equals(WConstant.TYPE_BACK_MAIN)) {
                    startActivity(MainActivity.newIntent(MainActivity.TYPE_ORDER));
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.btn_cancel:
                if (StringUtils.isNotBlank(oid)) {
                    View viewCancel = LayoutInflater.from(this).inflate(R.layout.dialog_ok,
                            null);
                    builderCancel = NiftyDialogBuilder.getInstance(this);
                    builderCancel.setCustomView(viewCancel, this);
                    builderCancel.withEffect(Effectstype.Fadein);
                    builderCancel.show();
                    view.findViewById(R.id.btn_ok).setOnClickListener(v -> cancelMainOrder());
                }
        }
    }

    private void cancelMainOrder() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("id", oid);
            showProgress();
            PublicReq.request(HttpUrl.CANCEL_PRIMARY_ORDER,
                    response -> {
                        try {
                            builderCancel.dismiss();
                            RespData respData = GsonHelper.parseObject(
                                    response, RespData.class);
                            if (null != respData) {
                                if (respData.isSuccess()) {
                                    getOrderDetail();
                                    Resp resp = new Resp();
                                    resp.setCode(Resp.CODE_SUCCESS);
                                    EventBus.getDefault().postSticky(resp);
                                    ToastUtil.showMes("取消成功");
                                } else {
                                    ToastUtil.showError(respData.getMsg());
                                }
                            } else {
                                ToastUtil
                                        .showError(R.string.network_data_error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideProgress();
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
