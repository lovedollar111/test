package cn.dogplanet.ui.user;

import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dogplanet.R;
import cn.dogplanet.base.BaseActivity;

public class CompanyFindActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_find);
        ButterKnife.bind(this);
    }
}
