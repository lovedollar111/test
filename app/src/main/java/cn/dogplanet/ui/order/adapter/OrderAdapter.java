package cn.dogplanet.ui.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.dogplanet.R;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.entity.Order;
import cn.dogplanet.entity.OrderMainEnum;
import cn.dogplanet.ui.order.OrderDetailActivity;
import cn.dogplanet.ui.shop.ShopProductPayActivity;

/**
 * 我的订单Adapter
 * editor:ztr
 * package_name:cn.dogplanet.ui.order.adapter
 * file_name:OrderAdapter.java
 * date:2016-12-6
 */
public class OrderAdapter extends BaseAdapter {

	private Context mContext;
	private List<Order> mOrders=new ArrayList<>();
	private SimpleDateFormat formatter_num = new SimpleDateFormat("yyyy-MM-dd");
	private int type;

	public OrderAdapter(Context context, List<Order> orders, int type) {
		super();
		this.mContext = context;
		this.mOrders.addAll(orders);
		this.type = type;
	}

	public List<Order> getOrders() {
		return mOrders;
	}
	
	public void clear(){
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
	
	public void setType(int type){
		this.type=type;
	}

	public void setIsRead(int pos){
		if(pos<=mOrders.size()){
			mOrders.get(pos).setIs_read("1");
		}
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.order_adapter_item, parent, false);
			holder.tv_status = convertView
					.findViewById(R.id.tv_status);
			holder.btn_pay = convertView.findViewById(R.id.btn_pay);
			holder.line = convertView.findViewById(R.id.view_line);
			holder.order_num = convertView
					.findViewById(R.id.order_num);
			holder.tv_price = convertView
					.findViewById(R.id.tv_price);
			holder.tv_peo = convertView.findViewById(R.id.tv_peo);
			holder.isRead = convertView.findViewById(R.id.isRead);
			holder.tv_time = convertView.findViewById(R.id.tv_time);
			holder.layout_main= convertView.findViewById(R.id.layout_main);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Order o = mOrders.get(position);
		if (StringUtils.isNotBlank(o.getOrder_num())) {
			holder.order_num.setText(String.format("订单编号：%s", o.getOrder_num()));
		}

		if (StringUtils.isNotBlank(o.getCreate_time())) {
			holder.tv_time.setText(o.getCreate_time());
		}

		if (StringUtils.isNotBlank(o.getContact_name())&&StringUtils.isNotBlank(o.getContact_tel())) {
			holder.tv_peo.setText(String.format("%s——%s", o.getContact_name(), o.getContact_tel()));
		}

		if (StringUtils.isNotBlank(o.getUser_id()) &&o.getPay()!=null&& o.getPay()
				&& o.getStatus().equals(OrderMainEnum.OS_1.getStaus())) {
			holder.btn_pay.setVisibility(View.VISIBLE);
			holder.line.setVisibility(View.VISIBLE);
			holder.btn_pay.setTag(o.getId());
			holder.btn_pay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String oid = v.getTag().toString();
					mContext.startActivity(ShopProductPayActivity
							.newIntent(oid));
				}
			});
		} else {
			holder.btn_pay.setVisibility(View.GONE);
			holder.line.setVisibility(View.GONE);
			holder.btn_pay.setOnClickListener(null);
		}

		holder.tv_price.setText(String.format("¥%s", o.getPrice()));
		if (o.getIs_read().equals("1")) {
			holder.isRead.setVisibility(View.GONE);
		} else {
			holder.isRead.setVisibility(View.VISIBLE);
		}

		holder.layout_main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.isRead.setVisibility(View.GONE);
				mContext.startActivity(OrderDetailActivity.newIntent());
			}
		});
		
		OrderMainEnum oe = OrderMainEnum.getOrderStausBySCode(o.getStatus());
		if (50 != type && OrderMainEnum.OS_6.getStaus().equals(o.getStatus())
				&& !StringUtils.isBlank(o.getFinishDate())) {
			Date date;
			try {
				date = formatter_num.parse(o.getFinishDate());
				
				boolean isCanSucs = formatter_num.parse(
						formatter_num.format(new Date())).after(date);
				if (!isCanSucs) {
					// 未过游玩时间 显示已付款
					holder.tv_status.setText(OrderMainEnum.OS_2.getText());
					holder.tv_status.setBackgroundColor(Color
							.parseColor(OrderMainEnum.OS_2.getColor()));
				} else {
					// 过了游玩时间 显示已成功
					holder.tv_status.setText(OrderMainEnum.OS_6.getText());
					holder.tv_status.setBackgroundColor(Color
							.parseColor(OrderMainEnum.OS_6.getColor()));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (60 == type){
			if(OrderMainEnum.OS_6.getStaus().equals(o.getStatus())
					&& !StringUtils.isBlank(o.getFinishDate())){
				holder.tv_status.setText("部分退款");
				holder.tv_status.setBackgroundColor(Color.rgb(179, 222, 126));
			}else{
				holder.tv_status.setText("全部退款");
				holder.tv_status.setBackgroundColor(Color.rgb(255, 184, 184));
			}

		} else {
			if (null == oe) {
				holder.tv_status.setVisibility(View.INVISIBLE);
			} else {
				holder.tv_status.setText(oe.getText());
				holder.tv_status.setBackgroundColor(Color.parseColor(oe
						.getColor()));
			}
		}
		if(type==20){
			holder.tv_status.setText(OrderMainEnum.OS_2.getText());
			holder.tv_status.setBackgroundColor(Color
					.parseColor(OrderMainEnum.OS_2.getColor()));
		}else if(type==40){
			holder.tv_status.setText(OrderMainEnum.OS_6.getText());
			holder.tv_status.setBackgroundColor(Color
					.parseColor(OrderMainEnum.OS_6.getColor()));
		}
		return convertView;
	}

	private class ViewHolder {
		private Button btn_pay;
		private View line;

		private TextView order_num;
		private TextView tv_status;
		private TextView tv_peo;
		private TextView tv_time;
		private TextView tv_price;
		private ImageView isRead;
		
		private RelativeLayout layout_main;
	}
}
