package cn.dogplanet.ui.popup;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import cn.dogplanet.R;
import cn.dogplanet.album.imageloader.ImageChooseActivity;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.constant.ConstantSet;
import cn.dogplanet.constant.WConstant;

/**
 * 拍照Dialog
 * editor:ztr
 * package_name:cn.dogplanet.ui.popup
 * file_name:CameraDialog.java
 * date:2016-12-6
 */
public class CameraDialog extends Dialog {

	private Context mContext;
	private Button mBtnCamera;
	private Button mBtnAlbum;
	private Button mBtnCancel;
	// 最大选择图片
	private int maxSelected = 1;

	private String actionName = ConstantSet.ACTION_NAME;
	private int theme;

	public CameraDialog(Context context) {
		super(context, R.style.dialog_transparent);
		this.mContext = context;
	}

	public void setMaxSelected(int maxSelected) {
		this.maxSelected = maxSelected;
	}

	public CameraDialog(Context context, int theme) {
		super(context, R.style.dialog_transparent);
		this.mContext = context;
		this.theme=theme;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_camera);
		this.initView();
		this.setListener();
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	private void initView() {
		mBtnCamera = this.findViewById(R.id.btn_camera);
		mBtnAlbum = this.findViewById(R.id.btn_album);
		mBtnCancel = this.findViewById(R.id.btn_cancel);
	}

	private void setListener() {
		mBtnCamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Build.MODEL.equals("SM-N9006")){
					ToastUtil.showError("请从手机相册中选择");
				}else{
					if (Build.VERSION.SDK_INT >= 23) {
						int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
						int checkSelfPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
						if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
							ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, WConstant.PERMISSION_CAMERA);

							return;
						}else if(checkSelfPermission!=PackageManager.PERMISSION_GRANTED){
							ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, WConstant.PERMISSION_READ_EXTERNAL_STORAGE_IN_CAMERA);

							return;
						}else{
							takeCamera();//调用具体方法
						}
					} else {
						takeCamera();//调用具体方法
					}

					hide();
				}
			}
		});
		mBtnAlbum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= 23) {
					int checkSelfPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
					if(checkSelfPermission!=PackageManager.PERMISSION_GRANTED){
						ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, WConstant.PERMISSION_READ_EXTERNAL_STORAGE);
						return;
					}else{
						getContext().startActivity(
								ImageChooseActivity.newIntent(actionName, maxSelected));
					}
				} else {
					getContext().startActivity(
							ImageChooseActivity.newIntent(actionName, maxSelected));
				}
				hide();
			}
		});
		mBtnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hide();
			}
		});
	}

	@Override
	public void show() {
		super.show();
	}

	public void showDialog() {
		Window window = this.getWindow();
		window.setGravity(Gravity.BOTTOM);
		this.show();
	}

	private Uri photoUri;

	public Uri getPhotoUri() {
		return photoUri;
	}

	/**
	 * 拍照获取图片
	 */
	private void takeCamera() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = mContext.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			((Activity) mContext).startActivityForResult(intent,
					ConstantSet.SELECT_PIC_BY_TACK_PHOTO);
		} else {
			AndroidUtil.showToast("内存卡不存在");
		}
	}

	public void allowTakeCamera(){
		mBtnCamera.performClick();
	}

	public void allowChooseInCamera(){
		mBtnCamera.performClick();
	}

	public void allowChoose(){
		getContext().startActivity(
				ImageChooseActivity.newIntent(actionName, maxSelected));
	}
}