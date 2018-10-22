package cn.dogplanet.app.util;

import java.util.Map;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.net.NormalPostRequest;
import cn.dogplanet.net.volley.Request;
import cn.dogplanet.net.volley.Response;


/**
 * 
 * editor:ztr
 * package_name:cn.dogplanet.ui.req
 * file_name:PublicReq.java
 * date:2016-12-6
 */
public class PublicReq {

	/**
	 *
	 */
	public static void request(String url,
                               final Response.Listener<String> listener,
                               Response.ErrorListener errorListener, Map<String, String> params) {
		// Request<String> request = new NormalPostRequest(url,
		// new Response.Listener<String>() {
		// @Override
		// public void onResponse(String response) {
		// try {
		// RespData respData = GsonHelper.parseObject(
		// response, RespData.class);
		// if (null != respData) {
		// if (respData.isValida()) {

		// Intent intent = new Intent(GlobalContext
		// .getInstance(), LoginActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// GlobalContext.getInstance().startActivity(
		// intent);
		// } else {
		// listener.onResponse(response);
		// }

		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }, errorListener, params);

		Request<String> request = new NormalPostRequest(url, listener,
				errorListener, params);
		GlobalContext.getInstance().addRequest(request);
	}
}
