package cn.dogplanet.ui.shop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.widget.CircleImageView;
import cn.dogplanet.entity.CartResp.Cart;
import cn.dogplanet.net.volley.toolbox.ListImageListener;

/**
 * 购物车Adapter
 * editor:ztr
 * package_name:cn.dogplanet.ui.shop.adapter
 * file_name:CartAdapter.java
 * date:2016-12-6
 */
public class CartAdapter extends BaseAdapter {

	/**
	 * 上下文对象
	 */
	private Context mContext = null;
	private List<Cart> mCarts = null;

	private Set<String> chkCarts = null;
	private boolean isGone = false;

	private SimpleDateFormat formatter_parse = new SimpleDateFormat(
			"yyyy-MM-dd");
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");

	/**
	 * 单击事件监听器
	 */
	private IOnItemRightClickListener mListener = null;

	public interface IOnItemRightClickListener {
		void onRightClick(View v, int position);
	}

	private OnUpdateMoneyListener refershMoneyListener;

	/**
	 */
	public CartAdapter(Context ctx, List<Cart> carts,
                       IOnItemRightClickListener l, OnUpdateMoneyListener listener) {
		mContext = ctx;
		mListener = l;
		refershMoneyListener = listener;
		chkCarts = new HashSet<>();
		this.mCarts = carts;
		for (Cart c : mCarts) {
			chkCarts.add(c.getId());
		}
	}

	public void setIsGone(boolean isGone) {
		this.isGone = isGone;
	}

	/**
	 * 购物车总金额
	 *
	 */
	public String sumCartMoney() {
		String sum = "0";
		if (null == mCarts || mCarts.isEmpty()) {
			return sum;
		}

		int s = 0;
		for (Cart c : mCarts) {
			if (chkCarts.contains(c.getId())) {
				int p = Integer.parseInt(c.getPrice())
						* Integer.parseInt(c.getNum());
				s += p;
			}
		}
		return String.valueOf(s);

	}

	public ArrayList<String> getProId() {
		ArrayList<String> arrayList = new ArrayList<>();
		for (Cart c : mCarts) {
			if (chkCarts.contains(c.getId())) {
				arrayList.add(c.getId());
			}
		}
		return arrayList;
	}

	public List<Cart> getCarts() {
		return mCarts;
	}


	public void removeCart(Cart cart) {
		chkCarts.remove(cart.getId());
		this.mCarts.remove(cart);
	}

	public List<String> getChkIds() {
		if (null == chkCarts || chkCarts.isEmpty()) {
			return new ArrayList<>();
		}
		return new ArrayList<>(chkCarts);
	}

	@Override
	public int getCount() {
		return this.mCarts.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mCarts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder item;
		final int thisPosition = position;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.shop_cart_item, parent, false);
			item = new ViewHolder();
			item.product_img = convertView
					.findViewById(R.id.product_img);
			item.tv_name = convertView.findViewById(R.id.tv_name);
			item.tv_date = convertView.findViewById(R.id.tv_date);
			item.tv_num = convertView.findViewById(R.id.tv_num);
			item.tv_money = convertView.findViewById(R.id.tv_money);
			item.ck_checkbox = convertView
					.findViewById(R.id.ck_checkbox);
			item.layout = convertView.findViewById(R.id.lay_chk);
			item.lay_msg = convertView
					.findViewById(R.id.lay_msg);
			item.pro_status= convertView.findViewById(R.id.pro_status);
			if (isGone) {
				item.layout.setVisibility(View.INVISIBLE);
			}
			convertView.setTag(item);
		} else {
			item = (ViewHolder) convertView.getTag();
		}
		item.lay_msg.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (mListener != null) {
					mListener.onRightClick(v, thisPosition);
				}
				return false;
			}
		});
		Cart cartData = mCarts.get(position);
		if (StringUtils.isNotBlank(cartData.getThumb())) {
			item.product_img.setTag(cartData.getThumb());
			ListImageListener imageListener = new ListImageListener(
					item.product_img, R.mipmap.loading, R.mipmap.loading,
					cartData.getThumb());
			GlobalContext.getInstance().getImageLoader()
					.get(cartData.getThumb(), imageListener);
		}
		item.tv_name.setText(cartData.getName());

		item.tv_num.setText(String.format(
				mContext.getString(R.string.shop_cart_num), cartData.getNum()));
		int price = Integer.parseInt(cartData.getNum())
				* Integer.parseInt(cartData.getPrice());
		item.tv_money.setText(String.format("¥%d", price));
		// if (Cart.STATUS_NOR.equals(cartData.getStatus())) {
		// item.pro_status.setVisibility(View.INVISIBLE);
		// } else {
		// item.pro_status.setVisibility(View.VISIBLE);
		// }
		try {
			item.tv_date.setVisibility(View.GONE);
			item.tv_date.setText(formatter.format(formatter_parse
					.parse(cartData.getFinish_date())));  
			item.tv_date.setVisibility(View.VISIBLE);
			Date date = formatter_parse.parse(cartData.getFinish_date());
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String nDate = sDateFormat.format(new Date());
			Date date1 = formatter_parse.parse(nDate);
			if (date1.after(date)) {
				item.layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						View inflate = LayoutInflater.from(mContext).inflate(
								R.layout.dialog_item, null);
						final AlertDialog dialog = new AlertDialog.Builder(mContext).show();
						dialog.setContentView(inflate);
						TextView title = inflate.findViewById(R.id.title);
						title.setText("提示");
						TextView msg = inflate.findViewById(R.id.msg);
						msg.setText("产品已失效");
						inflate.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});

					}
				});
				item.ck_checkbox.setVisibility(View.GONE);
				item.pro_status.setVisibility(View.VISIBLE);
				chkCarts.remove(cartData.getId());
				refershMoneyListener.refershMoney();
				notifyDataSetChanged();
			} else {
				item.ck_checkbox.setVisibility(View.VISIBLE);
				item.pro_status.setVisibility(View.GONE);
				item.layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(item.ck_checkbox.isChecked()){
							item.ck_checkbox.setChecked(false);
						}else{
							item.ck_checkbox.setChecked(true);
						}
					}
				});
				notifyDataSetChanged();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		item.ck_checkbox.setTag(position);
		item.ck_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int p = (Integer) buttonView.getTag();
						Cart c = mCarts.get(p);
						if (isChecked) {
							chkCarts.add(c.getId());
						} else {
							chkCarts.remove(c.getId());
						}
						refershMoneyListener.refershMoney();
					}
				});
//		item.lay_msg.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mContext.startActivity(ProductDetailActivity
//						.newIntent(fCartData.getPrimary_product_id(),false));
//			}
//		});
		return convertView;
	}

	private class ViewHolder {
		private RelativeLayout lay_msg;

		private CircleImageView product_img;
		private CheckBox ck_checkbox;

		private TextView tv_name;
		private TextView tv_date;
		private TextView tv_num;
		private TextView tv_money;
		private LinearLayout layout;
		private TextView pro_status;
	}

	public interface OnUpdateMoneyListener {
		void refershMoney();
	}
}
