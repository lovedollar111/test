package cn.dogplanet.ui.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.KeyBoardUtils;
import cn.dogplanet.app.util.SoftKeyBoardListener;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.lay_update)
    LinearLayout layUpdate;
    @BindView(R.id.lay_logo)
    LinearLayout layLogo;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), AboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                tvUpdate.setText("最新版本");
            }

            @Override
            public void onUpdateAvailable(String s) {
                tvUpdate.setText("更新新版本");
            }
        });
        etMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isBlank(s.toString())) {
                    tvNum.setText("0/100");
                } else {
                    tvNum.setText(String.format("%d/100", s.length()));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SoftKeyBoardListener .setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                layLogo.setVisibility(View.GONE);
                layUpdate.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
                layLogo.setVisibility(View.VISIBLE);
                layUpdate.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.lay_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.lay_update:
                PgyUpdateManager.register(this, new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        View view = LayoutInflater.from(AboutActivity.this).inflate(R.layout.umeng_update_dialog,
                                null);
                        NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(AboutActivity.this);
                        builder.setCustomView(view, AboutActivity.this);
                        builder.withEffect(Effectstype.Fadein);
                        builder.show();
                        TextView textView = view
                                .findViewById(R.id.umeng_update_content);
                        textView.setText(String.format("版本号：%s\n本次更新内容：\n%s", appBean.getVersionName(), appBean.getReleaseNote()));
                        view.findViewById(R.id.umeng_update_id_cancel)
                                .setOnClickListener(v -> {
                                    // TODO Auto-generated method stub
                                    builder.dismiss();
                                });
                        view.findViewById(R.id.umeng_update_id_ok)
                                .setOnClickListener(v -> {
                                    // TODO Auto-generated method stub
                                    startDownloadTask(AboutActivity.this,
                                            appBean.getDownloadURL());
                                    builder.dismiss();
                                });
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        ToastUtil.showMes("已经是最新版本了~~");
                    }
                });
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (StringUtils.isNotBlank(etMsg.getText().toString())) {
                etMsg.setText(null);
                etMsg.setGravity(Gravity.START);
                etMsg.setSelection(0);
                ToastUtil.showMes("感谢您的反馈");
                layLogo.setVisibility(View.VISIBLE);
                layUpdate.setVisibility(View.VISIBLE);
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
