package cn.dogplanet.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.base.BaseActivity;

public class RefundRulerActivity extends BaseActivity {

    private static final String MSG = "msg";
    @BindView(R.id.tv_msg)
    TextView tvMsg;

    public static Intent newIntent(String msg) {
        Intent intent = new Intent(GlobalContext.getInstance(), RefundRulerActivity.class);
        intent.putExtra(MSG, msg);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        ButterKnife.bind(this);
        tvMsg.setText(getIntent().getStringExtra(MSG));
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
