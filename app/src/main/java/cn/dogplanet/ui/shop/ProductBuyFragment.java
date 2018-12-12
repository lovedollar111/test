package cn.dogplanet.ui.shop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.dogplanet.R;
import cn.dogplanet.app.util.DateUtils;
import cn.dogplanet.app.util.StringUtils;


public class ProductBuyFragment extends Fragment {

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.et_other)
    EditText etOther;
    private Unbinder bind;
    private SetInfoListener getInfo;

    public ProductBuyFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static ProductBuyFragment newIntent() {
        return new ProductBuyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_buy, container, false);
        bind = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        etOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isNotBlank(s.toString())) {
                    getInfo.setNum(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvDate.setText(DateUtils.getTimeShort());
    }

    public void setInfoListener(SetInfoListener info) {
        this.getInfo = info;
        getInfo.setDate(DateUtils.dateToStr(new Date()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                getInfo.setNum(1);
                break;
            case R.id.tv_2:
                getInfo.setNum(2);
                break;
            case R.id.tv_3:
                getInfo.setNum(3);
                break;
            case R.id.tv_4:
                getInfo.setNum(4);
                break;
            case R.id.tv_5:
                getInfo.setNum(5);
                break;
            case R.id.tv_date:
                getInfo.setDate(DateUtils.dateToStr(new Date()));
                break;
        }
    }

    public interface SetInfoListener {
        void setDate(String date);
        void setNum(int num);
    }
}
