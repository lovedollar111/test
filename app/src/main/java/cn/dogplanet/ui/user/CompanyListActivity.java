package cn.dogplanet.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.entity.Travel;
import cn.dogplanet.entity.TravelResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.user.adapter.CompanyAdapter;

public class CompanyListActivity extends BaseActivity {

    public static final int COMPANY_LIST_CODE = 10001;

    public static final String COMPANY_ID="list_company_id";
    public static final String COMPANY_NAME="list_company_name";

    @BindView(R.id.list_rem_company)
    NoScrollListView listRemCompany;
    @BindView(R.id.list_company)
    NoScrollListView listCompany;

    CompanyAdapter companyRemAdapter;
    CompanyAdapter companyListAdapter;

    private String company_id,company_name;

    public static Intent newIntent(String company_id) {
        Intent intent= new Intent(GlobalContext.getInstance(), CompanyListActivity.class);
        intent.putExtra(COMPANY_ID,company_id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        ButterKnife.bind(this);
        company_id=getIntent().getStringExtra(COMPANY_ID);
        getCompany();
    }

    private void getCompany() {
        Map<String, String> params = new HashMap<>();
        showProgress();
        PublicReq.request(HttpUrl.GET_TRAVEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgress();
                        if (StringUtils.isNotBlank(response)) {
                            TravelResp resp=GsonHelper.parseObject(response, TravelResp.class);
                            if (resp != null) {
                                updateView(resp);
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showError(R.string.network_error);
                    }
                }, params);
    }

    private void updateView(TravelResp resp) {
        List<Travel> remTravels=resp.getRecommendTravelAgency();
        List<Travel> travels=resp.getTravelAgency();

        companyRemAdapter=new CompanyAdapter(remTravels,this,true,company_id);
        listRemCompany.setAdapter(companyRemAdapter);
        companyRemAdapter.notifyDataSetChanged();
        companyListAdapter=new CompanyAdapter(travels,this,false,company_id);
        listCompany.setAdapter(companyListAdapter);
        companyListAdapter.notifyDataSetChanged();
        listCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                company_id = travels.get(position).getId().toString();
                company_name = travels.get(position).getName();
                intent.putExtra(COMPANY_ID, company_id);
                intent.putExtra(COMPANY_NAME, company_name);
                setResult(COMPANY_LIST_CODE, intent);
                finish();
            }
        });
        listRemCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                company_id = remTravels.get(position).getId().toString();
                company_name = remTravels.get(position).getName();
                intent.putExtra(COMPANY_ID, company_id);
                intent.putExtra(COMPANY_NAME, company_name);
                setResult(COMPANY_LIST_CODE, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==CompanyFindActivity.COMPANY_FIND_CODE){
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
                startActivityForResult(CompanyFindActivity.newIntent(),CompanyFindActivity.COMPANY_FIND_CODE);
                break;
        }
    }
}
