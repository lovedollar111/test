package cn.dogplanet.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import cn.dogplanet.app.util.PullToRefreshHelper;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.library.PullToRefreshBase;
import cn.dogplanet.app.widget.library.PullToRefreshListView;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.Product;
import cn.dogplanet.entity.ProductListResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.ui.shop.adapter.ProductListAdapter;

public class ProductListActivity extends BaseActivity {

    public static final String PRODUCT_TYPE = "product_type";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.lay_title)
    RelativeLayout layTitle;
    @BindView(R.id.list_product)
    PullToRefreshListView listProduct;
    @BindView(R.id.tv_tip)
    ImageView tvTip;

    private String type;
    private Expert expert;
    private int pageId = 1;
    private ProductListAdapter adapter;

    public static Intent newIntent(String type) {
        Intent intent = new Intent(GlobalContext.getInstance(), ProductListActivity.class);
        intent.putExtra(PRODUCT_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra(PRODUCT_TYPE);
        expert = WCache.getCacheExpert();
        initView();
        getProduct();
    }

    private void initView() {
        listProduct.setMode(PullToRefreshBase.Mode.BOTH);
        listProduct.setOnItemClickListener((parent, view, position, id) -> {
            if(adapter!=null){
                Product product= (Product) adapter.getItem(position-1);
                startActivity(ProductBuyActivity.newIntent(product.getPro_id()));
            }
        });
        PullToRefreshHelper.initIndicator(listProduct);
        PullToRefreshHelper.initIndicatorStart(listProduct);
        listProduct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                pageId = 1;
                getProduct();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 刷新
                pageId++;
                getProduct();
            }
        });

        switch (type) {
            case Product.CATE_DIVING:
                title.setText("潜水类");
                break;
            case Product.CATE_SEA:
                title.setText("海上娱乐");
                break;
            case Product.CATE_TICKET:
                title.setText("门船票");
                break;
            case Product.CATE_LAND:
                title.setText("陆地玩乐");
                break;
            case Product.CATE_OTHER:
                title.setText("其他套票");
                break;
        }
    }

    private void getProduct() {
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        params.put("category", type);
        params.put("page_id", String.valueOf(pageId));
        showProgress();
        PublicReq.request(HttpUrl.GET_EXPERT_PRODUCT,
                response -> {
                    hideProgress();
                    listProduct.onRefreshComplete();
                    if (StringUtils.isNotBlank(response)) {
                        ProductListResp resp = GsonHelper.parseObject(response, ProductListResp.class);
                        if (resp != null) {
                            if (pageId == 1) {
                                if (adapter != null) {
                                    adapter.clear();
                                }
                            }
                            updateView(resp.getProduct());
                        }
                    }

                }, error -> ToastUtil.showError(R.string.network_error), params);
    }

    private void updateView(List<Product> products) {
        if (pageId == 1) {
            if (products == null || products.size() == 0) {
                listProduct.setVisibility(View.GONE);
                tvTip.setVisibility(View.VISIBLE);
            } else {
                listProduct.setVisibility(View.VISIBLE);
                tvTip.setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new ProductListAdapter(products, this);
                    listProduct.setAdapter(adapter);
                } else {
                    adapter.clear();
                    adapter.addAll(products);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            if (adapter == null) {
                adapter = new ProductListAdapter(products, this);
                listProduct.setAdapter(adapter);
            } else {
                adapter.addAll(products);
                adapter.notifyDataSetChanged();
            }
        }
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
