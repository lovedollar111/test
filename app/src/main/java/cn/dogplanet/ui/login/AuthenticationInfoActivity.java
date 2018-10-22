package cn.dogplanet.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.base.BaseActivity;

public class AuthenticationInfoActivity extends BaseActivity {

    public static Intent newIntent(){
        return new Intent(GlobalContext.getInstance(),AuthenticationInfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_info);
    }
}
