package cn.dogplanet.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.HashMap;
import java.util.Map;

import anetwork.channel.Param;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.base.BaseFragmentActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.OrderDetailResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;


/**
 * 订单详情
 * editor:ztr
 * package_name:cn.dogplanet.ui.order
 * file_name:OrderDetailActivity.java
 * date:2016-12-6
 */
public class OrderDetailActivity extends BaseFragmentActivity implements
		OnClickListener {

	private Expert expert;

	public static Intent newIntent() {
		Intent intent = new Intent(GlobalContext.getInstance(),
				OrderDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		expert= WCache.getCacheExpert();
		initView();
		initPopup();
	}

	private void initPopup() {

	}

	private void initView() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		getOrderDetail();
//		getOrderRemind();
	}

	private void getOrderDetail() {
		if (null != expert) {
			Map<String, String> params = new HashMap<>();
			PublicReq.request(HttpUrl.GET_ORDER_DETAIL,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								OrderDetailResp respData = GsonHelper
										.parseObject(response,
												OrderDetailResp.class);
								if (null != respData) {


								} else {
									ToastUtil
											.showError(R.string.network_data_error);
								}
							} catch (Exception e) {
								e.printStackTrace();
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

	@Override
	public void onClick(View v) {

	}
}
