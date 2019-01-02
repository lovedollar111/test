package cn.dogplanet.ui.order.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dogplanet.R;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.widget.NoScrollListView;
import cn.dogplanet.entity.Order;
import cn.dogplanet.entity.OrderDetail;
import cn.dogplanet.ui.order.OrderDetailActivity;

/**
 * 我的订单Adapter
 * editor:ztr
 * package_name:cn.dogplanet.ui.order.adapter
 * file_name:OrderAdapter.java
 * date:2016-12-6
 */
public class OrderAdapter extends BaseAdapter {

    public static final String TYPE_LIST = "list";
    public static final String TYPE_FIND = "find";


    private Context mContext;
    private List<Order> mOrders = new ArrayList<>();
    private OnPayListener onPayListener;
    private String type;

    public OrderAdapter(Context context, List<Order> orders, String type, OnPayListener onPayListener) {
        super();
        this.mContext = context;
        this.mOrders.addAll(orders);
        this.onPayListener = onPayListener;
        this.type = type;
    }

    public List<Order> getOrders() {
        return mOrders;
    }

    public void clear() {
        mOrders.clear();
        notifyDataSetChanged();
    }

    public void setOrders(List<Order> orders) {
        this.mOrders = orders;
    }

    public void addAll(List<Order> orders) {
        this.mOrders.addAll(orders);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setIsRead(int pos) {
        if (pos <= mOrders.size()) {
            mOrders.get(pos).setIs_read("1");
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.order_adapter_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Order order = mOrders.get(position);
        holder.tvName.setText(String.format("%s————%s", order.getContact_name(), order.getContact_tel()));
        String status = order.getStatus();
        Log.i("info",status);
        switch (status) {
            case OrderDetail.ORDER_MAIN_TYPE_WAIT:
                holder.tvStatus.setText("未支付");
                break;
            case OrderDetail.ORDER_MAIN_TYPE_SUCCESS:
                holder.tvStatus.setText("已支付");
                break;
            case OrderDetail.ORDER_MAIN_TYPE_CLOSE:
                holder.tvStatus.setText("交易关闭");
                break;
            case OrderDetail.ORDER_MAIN_TYPE_BACK_MONEY:
                holder.tvStatus.setText("退款中");
                break;
            case OrderDetail.ORDER_MAIN_TYPE_BACK_ALL_MONEY:
                holder.tvStatus.setText("已退款");
                break;
        }
        if (type.equals(TYPE_FIND)) {
            holder.btnPay.setVisibility(View.GONE);
            holder.viewLine.setVisibility(View.GONE);
        } else {
            switch (status) {
                case OrderDetail.ORDER_MAIN_TYPE_WAIT:
                    if(order.getPay()){
                        holder.btnPay.setVisibility(View.VISIBLE);
                        holder.viewLine.setVisibility(View.VISIBLE);
                    }else {
                        holder.btnPay.setVisibility(View.GONE);
                        holder.viewLine.setVisibility(View.GONE);
                    }
                    break;
                case OrderDetail.ORDER_MAIN_TYPE_SUCCESS:
                    holder.btnPay.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                    break;
                case OrderDetail.ORDER_MAIN_TYPE_CLOSE:
                    holder.btnPay.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                    break;
                case OrderDetail.ORDER_MAIN_TYPE_BACK_MONEY:
                    holder.btnPay.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                case OrderDetail.ORDER_MAIN_TYPE_BACK_ALL_MONEY:
                    holder.btnPay.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                    break;
            }
        }
        OrderListAdapter adapter;
        adapter = new OrderListAdapter(order.getOrderProducts(), mContext);
        holder.scrollList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        holder.btnPay.setOnClickListener(v -> onPayListener.OnPay(order.getId()));
        holder.layMain.setOnClickListener(v ->
                mContext.startActivity(OrderDetailActivity.newIntent(order.getId())));
        holder.scrollList.setOnItemClickListener((parent1, view, position1, id) -> mContext.startActivity(OrderDetailActivity.newIntent(order.getId())));
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.scroll_list)
        NoScrollListView scrollList;
        @BindView(R.id.btn_pay)
        Button btnPay;
        @BindView(R.id.view_line2)
        View viewLine;
        @BindView(R.id.lay_main)
        RelativeLayout layMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnPayListener {
        void OnPay(String id);
    }
}
