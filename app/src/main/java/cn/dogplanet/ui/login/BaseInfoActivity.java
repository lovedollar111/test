package cn.dogplanet.ui.login;

import android.content.Intent;
import android.os.Bundle;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.base.BaseActivity;

public class BaseInfoActivity extends BaseActivity {


    EditTextWithDel et_name;

    EditTextWithDel et_id_card;

    EditTextWithDel et_company;

    EditTextWithDel et_driving_licence_time;

    EditTextWithDel et_vehicle_license_time;

    EditTextWithDel et_operational_qualification_time;

    public static Intent newIntent(){
        return new Intent(GlobalContext.getInstance(),BaseInfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);

    }

}
