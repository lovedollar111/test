package cn.dogplanet.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.NoScrollListView;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.Resp;
import cn.dogplanet.entity.Travel;
import cn.dogplanet.entity.TravelResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.user.adapter.CompanyAdapter;

public class CompanyListActivity extends BaseActivity {

    public static final int COMPANY_LIST_CODE = 10001;

    public static final String COMPANY_ID = "list_company_id";
    public static final String COMPANY_NAME = "list_company_name";
    public static final String COMPANY_TYPE = "list_company_type";

    public static final String TYPE_CHOOSE = "type_choose";
    public static final String TYPE_CHANGE = "type_change";

    @BindView(R.id.list_rem_company)
    NoScrollListView listRemCompany;
    @BindView(R.id.list_company)
    NoScrollListView listCompany;

    CompanyAdapter companyRemAdapter;
    CompanyAdapter companyListAdapter;

    private String company_id, company_name, type, old_travel_agency_id;
    private Expert expert;
    private List<Travel> remTravels;
    private List<Travel> travels;

    public static Intent newIntent(String company_id, String type) {
        Intent intent = new Intent(GlobalContext.getInstance(), CompanyListActivity.class);
        intent.putExtra(COMPANY_ID, company_id);
        intent.putExtra(COMPANY_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        ButterKnife.bind(this);
        company_id = getIntent().getStringExtra(COMPANY_ID);
        old_travel_agency_id = getIntent().getStringExtra(COMPANY_ID);
        type = getIntent().getStringExtra(COMPANY_TYPE);
        expert = WCache.getCacheExpert();
        getCompany();
    }

    private void getCompany() {
        Map<String, String> params = new HashMap<>();
        showProgress();
        PublicReq.request(HttpUrl.GET_TRAVEL,
                response -> {
                    hideProgress();
                    if (StringUtils.isNotBlank(response)) {
                        TravelResp resp = GsonHelper.parseObject(response, TravelResp.class);
                        if (resp != null) {
                            updateView(resp);
                        }
                    }

                }, error -> ToastUtil.showError(R.string.network_error), params);
    }

    private void updateView(TravelResp resp) {
        remTravels = resp.getRecommendTravelAgency();
        travels = resp.getTravelAgency();

        companyRemAdapter = new CompanyAdapter(remTravels, this, true, company_id);
        listRemCompany.setAdapter(companyRemAdapter);
        companyRemAdapter.notifyDataSetChanged();
        companyListAdapter = new CompanyAdapter(travels, this, false, company_id);
        listCompany.setAdapter(companyListAdapter);
        companyListAdapter.notifyDataSetChanged();
        listCompany.setOnItemClickListener((parent, v, position, id) -> {
            if (type.equals(TYPE_CHOOSE)) {
                Intent intent = new Intent();
                company_id = travels.get(position).getId().toString();
                company_name = travels.get(position).getName();
                intent.putExtra(COMPANY_ID, company_id);
                intent.putExtra(COMPANY_NAME, company_name);
                setResult(COMPANY_LIST_CODE, intent);
                finish();
            } else {
                company_id = travels.get(position).getId().toString();
                company_name = travels.get(position).getName();
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_ok,
                        null);
                NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
                builder.setCustomView(view, this);
                builder.withEffect(Effectstype.Fadein);
                builder.show();
                TextView tv_title = view.findViewById(R.id.title);
                tv_title.setText("提示");
                TextView tv_msg = view.findViewById(R.id.msg);
                tv_msg.setText("您的更换公司申请将提交给原公司，原公司同意后进入新公司审核流程，共计约7个工作日，请您耐心等待，在此期间内，您的账号无法进行下单操作。");
                Button button = view.findViewById(R.id.btn_ok);
                button.setText("确定");
                button.setOnClickListener(v1 -> {
                    builder.dismiss();
                    changeCompany(company_id, position, false);
                });
            }

        });
        listRemCompany.setOnItemClickListener((parent, v, position, id) -> {
            if (type.equals(TYPE_CHOOSE)) {
                Intent intent = new Intent();
                company_id = remTravels.get(position).getId().toString();
                company_name = remTravels.get(position).getName();
                intent.putExtra(COMPANY_ID, company_id);
                intent.putExtra(COMPANY_NAME, company_name);
                setResult(COMPANY_LIST_CODE, intent);
                finish();
            } else {
                company_id = travels.get(position).getId().toString();
                company_name = travels.get(position).getName();
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_ok,
                        null);
                NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
                builder.setCustomView(view, this);
                builder.withEffect(Effectstype.Fadein);
                builder.show();
                TextView tv_title = view.findViewById(R.id.title);
                tv_title.setText("提示");
                TextView tv_msg = view.findViewById(R.id.msg);
                tv_msg.setText("您的更换公司申请将提交给原公司，原公司同意后进入新公司审核流程，共计约7个工作日，请您耐心等待，在此期间内，您的账号无法进行下单操作。");
                Button button = view.findViewById(R.id.btn_ok);
                button.setText("确定");
                button.setOnClickListener(v1 -> {
                    builder.dismiss();
                    changeCompany(company_id, position, true);
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CompanyFindActivity.COMPANY_FIND_CODE) {
            Intent intent = new Intent();
            company_id = data.getStringExtra(CompanyFindActivity.COMPANY_ID);
            company_name = data.getStringExtra(CompanyFindActivity.COMPANY_NAME);
            intent.putExtra(COMPANY_ID, company_id);
            intent.putExtra(COMPANY_NAME, company_name);
            setResult(COMPANY_LIST_CODE, intent);
            finish();
        }
    }

    @OnClick({R.id.btn_back, R.id.img_find})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.img_find:
                startActivityForResult(CompanyFindActivity.newIntent(type, company_id), CompanyFindActivity.COMPANY_FIND_CODE);
                break;
        }
    }

    private void changeCompany(String companyId, int position, boolean isRem) {
        if (!company_id.equals(old_travel_agency_id)) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("old_travel_agency_id", old_travel_agency_id);
            params.put("new_travel_agency_id", companyId);
            PublicReq.request(HttpUrl.CHANGE_TRAVEL_AGENCY, response -> {
                Resp resp = GsonHelper.parseObject(response, Resp.class);
                if (resp != null && resp.isSuccess()) {
                    ToastUtil.showMes("保存成功");
                    if (isRem) {
                        Intent intent = new Intent();
                        company_id = remTravels.get(position).getId().toString();
                        company_name = remTravels.get(position).getName();
                        intent.putExtra(COMPANY_ID, company_id);
                        intent.putExtra(COMPANY_NAME, company_name);
                        setResult(COMPANY_LIST_CODE, intent);
                    } else {
                        Intent intent = new Intent();
                        company_id = travels.get(position).getId().toString();
                        company_name = travels.get(position).getName();
                        intent.putExtra(COMPANY_ID, company_id);
                        intent.putExtra(COMPANY_NAME, company_name);
                        setResult(COMPANY_LIST_CODE, intent);
                    }
                    finish();
                } else {
                    ToastUtil.showError(R.string.network_data_error);
                }

            }, error -> ToastUtil.showError(R.string.network_error), params);
        }
    }
}
