package cn.dogplanet.net;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.dogplanet.net.volley.AuthFailureError;
import cn.dogplanet.net.volley.NetworkResponse;
import cn.dogplanet.net.volley.ParseError;
import cn.dogplanet.net.volley.Request;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.Response.ErrorListener;
import cn.dogplanet.net.volley.Response.Listener;
import cn.dogplanet.net.volley.toolbox.HttpHeaderParser;


/**
 * POST请求
 * 
 * @author sh
 *
 */
public class NormalPostRequest extends Request<String> {
	private Map<String, String> mMap;
	private Listener<String> mListener;

	public NormalPostRequest(String url, Listener<String> listener,
                             ErrorListener errorListener, Map<String, String> map) {
		super(Request.Method.POST, url, errorListener);

		mListener = listener;
		mMap = map;
	}

	// mMap是已经按照前面的方式,设置了参数的实例
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mMap;
	}

	// 此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));

			return Response.success(jsonString,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}
}
