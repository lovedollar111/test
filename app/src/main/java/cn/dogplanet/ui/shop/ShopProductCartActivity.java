package cn.dogplanet.ui.shop;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.CartResp;
import cn.dogplanet.entity.CartResp.Cart;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.shop.adapter.CartAdapter;
import cn.dogplanet.ui.shop.adapter.CartAdapter.IOnItemRightClickListener;
import cn.dogplanet.ui.shop.adapter.CartAdapter.OnUpdateMoneyListener;

/**
 * 购物车
 * editor:ztr
 * package_name:cn.dogplanet.ui.shop
 * file_name:ShopProductCartActivity.java
 * date:2016-12-6
 */
public class ShopProductCartActivity extends BaseActivity implements
		OnClickListener {

	private Expert expert = null;
	private ListView listview;
	private CartAdapter cartAdapter;
	private TextView tv_money,tv_all;
	private Button btn_pay;
	private ArrayList<String> proId;
	private LinearLayout lay_bottom;

	private TextView tv_tip;

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(),
				ShopProductCartActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		expert = WCache.getCacheExpert();
		setContentView(R.layout.shop_product_cart);
		proId = new ArrayList<>();
		initView();
		// getCartData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getCartData();
	}

	private void initView() {
		this.findViewById(R.id.btn_back).setOnClickListener(this);
		tv_tip = (TextView) this.findViewById(R.id.tv_tip);
		listview = (ListView) this.findViewById(R.id.listview);
		lay_bottom = (LinearLayout) this.findViewById(R.id.lay_bottom);
		tv_money = (TextView) this.findViewById(R.id.tv_money);
		tv_all= (TextView) this.findViewById(R.id.tv_all);
		btn_pay = (Button) this.findViewById(R.id.btn_cart_pay);
		btn_pay.setOnClickListener(this);
	}

	private void updateMoney() {
		proId.clear();
		if (null == cartAdapter || null == cartAdapter.getCarts()
				|| cartAdapter.getCarts().isEmpty()) {
			lay_bottom.setVisibility(View.GONE);
			tv_tip.setVisibility(View.VISIBLE);
			tv_money.setText("0");
			return;
		}
		proId.addAll(cartAdapter.getProId());
		tv_money.setText("" + cartAdapter.sumCartMoney());
		if ("0".equals(cartAdapter.sumCartMoney())) {
			tv_all.setTextColor(Color.rgb(218, 218, 218));
			tv_money.setTextColor(Color.rgb(218, 218, 218));
			btn_pay.setBackgroundColor(Color.rgb(218, 218, 218));

		} else {
			btn_pay.setBackgroundColor(ContextCompat.getColor(this,
					R.color.txt_selected_color));
			tv_all.setTextColor(ContextCompat.getColor(this, R.color.txt_selected_color));
			tv_money.setTextColor(ContextCompat.getColor(this, R.color.txt_selected_color));
		}
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
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
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

	private void updateView(final List<Cart> carts) {
		if (null != carts && !carts.isEmpty()) {
			listview.setVisibility(View.VISIBLE);
			if (null == cartAdapter) {
				cartAdapter = new CartAdapter(this,
						carts,
						new IOnItemRightClickListener() {

							@Override
							public void onRightClick(View v, final int position) {
//								final AlertDialog dialog = new AlertDialog.Builder(ShopProductCartActivity.this).create();
//								View del_view = LayoutInflater.from(ShopProductCartActivity.this).inflate(R.layout.dialog_ok, null);
//								Button btn_ok = (Button) del_view.findViewById(R.id.btn_ok);
//								btn_ok.setText("确定");
//								btn_ok.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										Cart cart = cartAdapter.getCarts()
//												.get(position);
//										delCartData(cart);
//										dialog.cancel();
//									}
//								});
//								Button btn_cancel = (Button) del_view.findViewById(R.id.btn_cancel);
//								btn_cancel.setText("取消");
//								btn_cancel.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										dialog.cancel();
//									}
//								});
//								TextView tv_title = (TextView) del_view.findViewById(R.id.title);
//								tv_title.setText("提示");
//								tv_title.setTextSize(18);
//								tv_title.setTextColor(Color.rgb(40, 44, 54));
//								TextView tv_msg = (TextView) del_view.findViewById(R.id.msg);
//								tv_msg.setText("是否删除该产品？");
//								tv_msg.setTextSize(12);
//								tv_msg.setTextColor(Color.rgb(72, 72, 72));
//								tv_msg.setPadding(AndroidUtil.dip2px(30), AndroidUtil.dip2px(10), AndroidUtil.dip2px(30), AndroidUtil.dip2px(2));
//								dialog.show();
//								dialog.setContentView(del_view);
							}
						}, new OnUpdateMoneyListener() {

							@Override
							public void refershMoney() {
								updateMoney();
							}
						});
				listview.setAdapter(cartAdapter);
			}
			// 更新计算总金额
			updateMoney();
			lay_bottom.setVisibility(View.VISIBLE);
		} else {
			lay_bottom.setVisibility(View.GONE);
			listview.setVisibility(View.GONE);
			tv_tip.setVisibility(View.VISIBLE);
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
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							System.out.println(response);
							RespData respData = GsonHelper.parseObject(
									response, RespData.class);
							if (null != respData) {
								if (respData.isSuccess()) {
									cartAdapter.removeCart(cart);
									cartAdapter.notifyDataSetChanged();
									// 更新计算总金额
									updateMoney();
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
			finish();
			break;
		case R.id.btn_cart_pay:
			if (null == cartAdapter || null == cartAdapter.getCarts()
					|| cartAdapter.getCarts().isEmpty()) {
				ToastUtil.showMes("购物车中无任何商品");
				return;
			}
			if (null == cartAdapter.getChkIds()
					|| cartAdapter.getChkIds().isEmpty()) {
				ToastUtil.showMes("请选中购买商品");
				return;
			}
			startActivity(PersonMsgActivity.newIntent());
			break;
		default:
			break;
		}
	}
}
