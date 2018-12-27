package cn.dogplanet.ui.shop;

import android.content.Context;
import android.graphics.Color;
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
import cn.dogplanet.app.util.ToastUtil;


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
    EditText etNum;
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
        etNum.addTextChangedListener(new TextWatcher() {
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
        tvDate.setText(DateUtils.getStringDateShort());
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

    @OnClick({R.id.et_other,R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                getInfo.setNum(1);
                updateButton(1);
                break;
            case R.id.tv_2:
                getInfo.setNum(2);
                updateButton(2);
                break;
            case R.id.tv_3:
                getInfo.setNum(3);
                updateButton(3);
                break;
            case R.id.tv_4:
                getInfo.setNum(4);
                updateButton(4);
                break;
            case R.id.tv_5:
                getInfo.setNum(5);
                updateButton(5);
                break;
            case R.id.tv_date:
                getInfo.setDate(DateUtils.dateToStr(new Date()));
                break;
            case R.id.et_other:
                updateButton(0);
                break;
        }
    }

    public void updateButton(int num) {
        tv1.setTextColor(getResources().getColor(R.color.color_33));
        tv2.setTextColor(getResources().getColor(R.color.color_33));
        tv3.setTextColor(getResources().getColor(R.color.color_33));
        tv4.setTextColor(getResources().getColor(R.color.color_33));
        tv5.setTextColor(getResources().getColor(R.color.color_33));
        etNum.setTextColor(getResources().getColor(R.color.color_33));
        tv1.setBackgroundResource(R.drawable.gradient_num_normal);
        tv2.setBackgroundResource(R.drawable.gradient_num_normal);
        tv3.setBackgroundResource(R.drawable.gradient_num_normal);
        tv4.setBackgroundResource(R.drawable.gradient_num_normal);
        tv5.setBackgroundResource(R.drawable.gradient_num_normal);
        etNum.setBackgroundResource(R.drawable.gradient_num_normal);
        switch (num) {
            case 1:
                tv1.setTextColor(getResources().getColor(R.color.white));
                tv1.setBackgroundResource(R.drawable.gradient_num);
                break;
            case 2:
                tv2.setTextColor(getResources().getColor(R.color.white));
                tv2.setBackgroundResource(R.drawable.gradient_num);
                break;
            case 3:
                tv3.setTextColor(getResources().getColor(R.color.white));
                tv3.setBackgroundResource(R.drawable.gradient_num);
                break;
            case 4:
                tv4.setTextColor(getResources().getColor(R.color.white));
                tv4.setBackgroundResource(R.drawable.gradient_num);
                break;
            case 5:
                tv5.setBackgroundResource(R.drawable.gradient_num);
                tv5.setTextColor(getResources().getColor(R.color.white));
                break;
            case 0:
                etNum.setTextColor(getResources().getColor(R.color.white));
                etNum.setBackgroundResource(R.drawable.gradient_num);
                break;

        }
    }

    public void hideNumBtn(final int num,boolean canBuy) {
        if(canBuy){
            switch (num) {
                case 0:
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.VISIBLE);
                    etNum.setVisibility(View.VISIBLE);
                    ToastUtil.showError("根据景区规定，必须先购买门票产品");
                    tv1.setEnabled(false);
                    tv2.setEnabled(false);
                    tv3.setEnabled(false);
                    tv4.setEnabled(false);
                    tv5.setEnabled(false);
                    etNum.setEnabled(false);
                    break;
                case 1:
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.GONE);
                    tv3.setVisibility(View.GONE);
                    tv4.setVisibility(View.GONE);
                    tv5.setVisibility(View.GONE);
                    etNum.setVisibility(View.GONE);
                    tv1.setEnabled(true);
                    tv2.setEnabled(false);
                    tv3.setEnabled(false);
                    tv4.setEnabled(false);
                    tv5.setEnabled(false);
                    etNum.setEnabled(false);
                    break;
                case 2:
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.GONE);
                    tv4.setVisibility(View.GONE);
                    tv5.setVisibility(View.GONE);
                    etNum.setVisibility(View.GONE);
                    tv1.setEnabled(true);
                    tv2.setEnabled(true);
                    tv3.setEnabled(false);
                    tv4.setEnabled(false);
                    tv5.setEnabled(false);
                    etNum.setEnabled(false);
                    break;
                case 3:
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.GONE);
                    tv5.setVisibility(View.GONE);
                    etNum.setVisibility(View.GONE);
                    tv1.setEnabled(true);
                    tv2.setEnabled(true);
                    tv3.setEnabled(true);
                    tv4.setEnabled(false);
                    tv5.setEnabled(false);
                    etNum.setEnabled(false);
                    break;
                case 4:
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.GONE);
                    etNum.setVisibility(View.GONE);
                    tv1.setEnabled(true);
                    tv2.setEnabled(true);
                    tv3.setEnabled(true);
                    tv4.setEnabled(true);
                    tv5.setEnabled(false);
                    etNum.setEnabled(false);
                    break;
                case 5:
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.VISIBLE);
                    etNum.setVisibility(View.GONE);
                    tv1.setEnabled(true);
                    tv2.setEnabled(true);
                    tv3.setEnabled(true);
                    tv4.setEnabled(true);
                    tv5.setEnabled(true);
                    etNum.setEnabled(false);
                    break;
                default:
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    tv5.setVisibility(View.VISIBLE);
                    etNum.setVisibility(View.VISIBLE);
                    tv1.setEnabled(true);
                    tv2.setEnabled(true);
                    tv3.setEnabled(true);
                    tv4.setEnabled(true);
                    tv5.setEnabled(true);
                    etNum.setEnabled(true);
                    etNum.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            // TODO Auto-generated method stub
                            if (StringUtils.isNotBlank(s.toString())
                                    && Integer.parseInt(s.toString()) > num) {
                                ToastUtil.showError("根据景区规定，您现在最多可购买" + num + "张票");
                                etNum.setText(String.format("%d", num));
                            }
                            if (StringUtils.isNotBlank(s.toString())) {
                                getInfo.setNum(Integer.parseInt(s.toString()));
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // TODO Auto-generated method stub

                        }
                    });
                    break;
            }
        }else{
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);
            tv4.setVisibility(View.VISIBLE);
            tv5.setVisibility(View.VISIBLE);
            etNum.setVisibility(View.VISIBLE);
            tv1.setEnabled(false);
            tv2.setEnabled(false);
            tv3.setEnabled(false);
            tv4.setEnabled(false);
            tv5.setEnabled(false);
            etNum.setEnabled(false);
        }
    }

    public interface SetInfoListener {
        void setDate(String date);

        void setNum(int num);
    }
}
