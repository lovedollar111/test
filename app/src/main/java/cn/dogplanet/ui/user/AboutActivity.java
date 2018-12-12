package cn.dogplanet.ui.user;

import android.content.Intent;
import android.os.Bundle;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    public static Intent newIntent(){
        return new Intent(GlobalContext.getInstance(),AboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
