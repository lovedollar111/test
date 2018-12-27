package cn.dogplanet.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.PullToRefreshHelper;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.library.PullToRefreshBase;
import cn.dogplanet.app.widget.library.PullToRefreshListView;
import cn.dogplanet.base.BaseFragment;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.Order;
import cn.dogplanet.entity.OrderDetail;
import cn.dogplanet.entity.OrderResp;
import cn.dogplanet.entity.Resp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.OrderFragment;
import cn.dogplanet.ui.order.adapter.OrderAdapter;
import cn.dogplanet.ui.shop.ShopProductPayActivity;

public class OrderListFragment extends BaseFragment {


    private static final String ORDER_TYPE = "order_type";
    // 默认加载
    private final String LOAD_TYPE_DEF = "loading";
    // 重新刷新数据
    private final String LOAD_TYPE_RELOAD = "reloading";

    @BindView(R.id.list_order)
    PullToRefreshListView listOrder;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private Unbinder bind;
    private String type;
    private int category, order_type;
    private Expert expert;
    private int current_page;
    private OrderAdapter orderAdapter;

    public static OrderListFragment newFragment(String type) {
        OrderListFragment f = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_TYPE, type);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        bind = ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        type = getArguments().getString(ORDER_TYPE);
        expert = WCache.getCacheExpert();
        initView();
        getCategory();
        getOrder(LOAD_TYPE_DEF);
        return view;
    }

    private void initView() {
        listOrder.setMode(PullToRefreshBase.Mode.BOTH);
        PullToRefreshHelper.initIndicator(listOrder);
        PullToRefreshHelper.initIndicatorStart(listOrder);
        listOrder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                getOrder(LOAD_TYPE_DEF);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 刷新
                current_page++;
                getOrder(LOAD_TYPE_RELOAD);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void respEventBus(Resp resp) {
        if (resp.isSuccess()) {
            getOrder(LOAD_TYPE_DEF);
        }
    }

    public void onRefresh() {
        getOrder(LOAD_TYPE_DEF);
    }

    private void getOrder(String loadType) {
        String url = HttpUrl.GET_RECENT_ORDER;
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("page_id", String.valueOf(current_page));
            if (category != Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_ALL)) {
                params.put("status", String.valueOf(category));
            }
            if (category == Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_BACK_ALL_MONEY) ||
                    category == Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_BACK_MONEY)) {
                url = HttpUrl.GET_REFUND_ORDER;
            }
            PublicReq.request(url, response -> {
                listOrder.onRefreshComplete();
                OrderResp respData = GsonHelper.parseObject(response,
                        OrderResp.class);
                if (null != respData) {
                    if (respData.isSuccess()) {
                        notifyAdapter(respData.getOrder(), loadType);
                    } else {
                        ToastUtil.showError(respData.getMsg());
                    }

                } else {
                    ToastUtil.showError(R.string.network_data_error);
                }
            }, error -> {
                listOrder.onRefreshComplete();
                ToastUtil.showError(R.string.network_error);
            }, params);
        }
    }

    private void notifyAdapter(List<Order> order, String loadType) {
        if (order == null || order.isEmpty()) {
            listOrder.setVisibility(View.GONE);
            tvTip.setVisibility(View.VISIBLE);
        } else {
            listOrder.setVisibility(View.VISIBLE);
            tvTip.setVisibility(View.GONE);
            if (orderAdapter == null) {
                orderAdapter = new OrderAdapter(getActivity(), order,OrderAdapter.TYPE_LIST, id -> {
                    startActivity(ShopProductPayActivity.newIntent(id));
                });
                listOrder.setAdapter(orderAdapter);
            } else {
                if (loadType.equals(LOAD_TYPE_DEF)) {
                    orderAdapter.clear();
                    orderAdapter.addAll(order);
                    orderAdapter.notifyDataSetChanged();
                } else {
                    orderAdapter.addAll(order);
                    orderAdapter.notifyDataSetChanged();
                }
            }

        }

    }

    private void getCategory() {
        switch (type) {
            case OrderFragment.ORDER_TYPE_ALL:
                category = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_ALL);
                order_type = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_ALL);
                break;
            case OrderFragment.ORDER_TYPE_WAIT:
                category = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_WAIT);
                order_type = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_WAIT);
                break;
            case OrderFragment.ORDER_TYPE_SUCCESS:
                category = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_SUCCESS);
                order_type = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_SUCCESS);
                break;
            case OrderFragment.ORDER_TYPE_BACK_ALL_MONEY:
                category = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_BACK_ALL_MONEY);
                order_type = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_BACK_ALL_MONEY);
                break;
            case OrderFragment.ORDER_TYPE_BACK_MONEY:
                category = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_BACK_MONEY);
                order_type = Integer.parseInt(OrderDetail.ORDER_MAIN_TYPE_BACK_MONEY);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

}
