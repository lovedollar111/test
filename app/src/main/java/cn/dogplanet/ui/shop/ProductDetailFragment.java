package cn.dogplanet.ui.shop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.dogplanet.R;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.widget.richeditor.RichEditor;
import cn.dogplanet.entity.ProductDetail;

public class ProductDetailFragment extends Fragment {


    @BindView(R.id.tv_service_detail)
    RichEditor tvServiceDetail;
    @BindView(R.id.tv_money_explain)
    TextView tvMoneyExplain;
    @BindView(R.id.tv_instructions)
    TextView tvInstructions;
    @BindView(R.id.tv_directions)
    TextView tvDirections;
    @BindView(R.id.tv_returns)
    TextView tvReturns;
    private Unbinder bind;

    public ProductDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        bind = ButterKnife.bind(this,view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void productEventBus(ProductDetail detail) {
        if (StringUtils.isNotBlank(detail.getPro_describe())) {
            String s = detail.getPro_describe();
            if (!s.contains("<script>")) {
                s = String.format("<script>" +
                        "function ResizeImages()" +
                        "{var myimg,oldwidth;var width = screen.width;var maxwidth=width;" +
                        "for(i=0;i <document.images.length;i++){myimg = document.images[i]; " +
                        "if(myimg.width != maxwidth){oldwidth = myimg.width;" +
                        "oldheight = myimg.height;" +
                        "myimg.width = maxwidth;" +
                        "myimg.height = (maxwidth*oldheight)/oldwidth;}" +
                        "myimg.setAttribute('style', 'margin-left: -8px !important;');" +
                        "}}" +
                        "</script><style>p{word-wrap: break-word !important; color: #757779; font-size: 13px}</style>" +
                        "<body onload=\"ResizeImages()\" >%s</body>", s);
            }
            tvServiceDetail.loadData(s, "text/html; charset=UTF-8", null);
            tvServiceDetail.setEnabled(false);
            tvServiceDetail.setClickable(false);

        }
        if (StringUtils.isNotBlank(detail.getMoney_explain())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvMoneyExplain.setText(Html.fromHtml(detail
                        .getMoney_explain(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvMoneyExplain.setText(Html.fromHtml(detail
                        .getMoney_explain()));
            }
        }

        if (StringUtils.isNotBlank(detail.getInstructions())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvInstructions.setText(Html.fromHtml(detail
                        .getInstructions(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvInstructions.setText(Html.fromHtml(detail
                        .getInstructions()));
            }
            tvInstructions.setLineSpacing(0, 0.96f);
            tvInstructions.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isNotBlank(detail.getDirections())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvDirections.setText(Html.fromHtml(detail.getDirections(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvDirections.setText(Html.fromHtml(detail.getDirections()));
            }

            tvDirections.setLineSpacing(0, 0.96f);
            tvDirections.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isNotBlank(detail.getReturns())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvReturns.setText(Html.fromHtml(detail.getReturns(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvReturns.setText(Html.fromHtml(detail.getReturns()));
            }
            tvReturns.setLineSpacing(0, 0.96f);
            tvReturns.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
