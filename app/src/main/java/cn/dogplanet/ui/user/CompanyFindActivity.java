package cn.dogplanet.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.entity.Travel;
import cn.dogplanet.entity.TravelResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.ui.user.adapter.CompanyAdapter;

import static com.baidu.location.h.g.G;

public class CompanyFindActivity extends BaseActivity {

    public static final int COMPANY_FIND_CODE = 10002;


    public static final String COMPANY_ID = "find_company_id";
    public static final String COMPANY_NAME = "find_company_name";
    @BindView(R.id.et_search)
    EditTextWithDel etSearch;
    @BindView(R.id.lay_search)
    RelativeLayout laySearch;
    @BindView(R.id.lay_hint)
    RelativeLayout layHint;
    @BindView(R.id.list_company)
    ListView listCompany;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private CompanyAdapter companyAdapter;

    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), CompanyFindActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_find);
        ButterKnife.bind(this);
        getCompany();
        initView();
    }

    private void initView() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getCompany();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.btn_cancel, R.id.lay_hint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.lay_hint:
                layHint.setVisibility(View.GONE);
                laySearch.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getCompany() {
        Map<String, String> params = new HashMap<>();
        showProgress();
        params.put("desc",etSearch.getText().toString());
        PublicReq.request(HttpUrl.GET_TRAVEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgress();
                        if (StringUtils.isNotBlank(response)) {
                            TravelResp resp= GsonHelper.parseObject(response, TravelResp.class);
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
        List<Travel> travels = resp.getTravelAgency();
        if(travels!=null&&travels.size()>0){
            tvTip.setVisibility(View.GONE);
            listCompany.setVisibility(View.VISIBLE);
            companyAdapter=new CompanyAdapter(resp.getTravelAgency(),this,false,"");
            listCompany.setAdapter(companyAdapter);
            companyAdapter.notifyDataSetChanged();
            listCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra(COMPANY_ID, travels.get(position).getId().toString());
                    intent.putExtra(COMPANY_NAME, travels.get(position).getName());
                    setResult(COMPANY_FIND_CODE, intent);
                    finish();
                }
            });
        }else{
            tvTip.setVisibility(View.VISIBLE);
            listCompany.setVisibility(View.GONE);
        }

    }
}
