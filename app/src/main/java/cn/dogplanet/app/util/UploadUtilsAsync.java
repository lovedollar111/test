package cn.dogplanet.app.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import cn.dogplanet.app.widget.CustomProgress;
import cn.dogplanet.constant.HttpUrl;


/**
 * 发布帖子
 * editor:ztr
 * package_name:cn.dogplanet.app.util
 * file_name:UploadUtilsAsync.java
 * date:2016-12-6
 */
public class UploadUtilsAsync extends AsyncTask<String, Integer, String> {
	/** 服务器路径 **/
	private String url = HttpUrl.UPLOAD_IMG;
	private CustomProgress mProgress;
	/** 上传的参数 **/
	private Map<String, String> paramMap;
	/** 要上传的文件 的路径 **/
	private ArrayList<String> paths;
	private Context context;
	// 图片上传成功之后的回调
	private OnSuccessListener listener;

	public UploadUtilsAsync(Context context, Map<String, String> paramMap,
                            ArrayList<String> paths) {
		this.context = context;
		this.paramMap = paramMap;
		this.paths = paths;
	}

	public void setListener(OnSuccessListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {// 执行前的初始化
//		if (null == mProgress) {
//			mProgress = CustomProgress.show(context,
//					context.getString(R.string.network_wait), true, null);
//		} else {
//			mProgress.show();
//		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		return uploadImage();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if (null != mProgress) {
			mProgress.hide();
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		if (null != mProgress) {
//			mProgress.hide();
		}
		if (listener != null) {
			listener.onSuccess(result);
		}
		super.onPostExecute(result);
	}

	public String uploadImage() {
		String res = "";
		HttpURLConnection conn = null;
		try {
			String BOUNDARY = "---------------------------"
					+ System.currentTimeMillis(); // boundary就是request头和上传文件内容的分隔符
			String requestUrl = url;
			URL url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			// 允许Input、Output，不使用Cache
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			// 设置以POST方式进行传送
			conn.setRequestMethod("POST");
			// 设置RequestProperty
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + BOUNDARY);
			// 构造DataOutputStream流
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (null != paramMap && !paramMap.isEmpty()) {
				StringBuffer sf = new StringBuffer();
				for (String inputName : paramMap.keySet()) {
					String inputValue = paramMap.get(inputName);
					if (inputValue == null) {
						continue;
					}
					sf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					sf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					sf.append(inputValue);
				}
				out.write(sf.toString().getBytes("utf-8"));
			}
			// file
			if (paths != null && !paths.isEmpty()) {

				String fileName = paramMap.get("fileName");
				if (StringUtils.isBlank(fileName)) {
					fileName = "file";
				}
				for (int i = 0; i < paths.size(); i++) {
					String path = paths.get(i);
					File file = new File(path);
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ fileName + "\"; filename=\"" + file.getName()
							+ "\"\r\n");
					// strBuf.append("Content-Type:image/jpeg \r\n\r\n");
					strBuf.append("Content-Type: application/octet-stream \r\n\r\n");
					out.write(strBuf.toString().getBytes("utf-8"));
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];

					int num=1;
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
						num++;
					}
					Log.i("info", num+"");
					in.close();
				}
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
			out.write(endData);
			out.flush();
			out.close();
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/**
	 * 图片上传成功之后的回调
	 * 
	 * @author sh
	 *
	 */
	public interface OnSuccessListener {
		void onSuccess(String result);
	}
}
