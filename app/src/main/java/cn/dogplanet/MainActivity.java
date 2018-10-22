package cn.dogplanet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.dogplanet.Ndk.NdkUtil;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.base.BaseFragmentActivity;

public class MainActivity extends BaseFragmentActivity {

    private static final String SELECT_TYPE = "type";

    public static final String TYPE_HOME = "home";
    public static final String TYPE_ORDER = "order";
    public static final String TYPE_USER = "user";

    private String select_type = TYPE_HOME;

    public static Intent newIntent(String type) {
        Intent intent = new Intent(GlobalContext.getInstance(), MainActivity.class);
        intent.putExtra(SELECT_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (StringUtils.isNotBlank(getIntent().getStringExtra(SELECT_TYPE))){
            select_type=getIntent().getStringExtra(SELECT_TYPE);
        }
    }

}
