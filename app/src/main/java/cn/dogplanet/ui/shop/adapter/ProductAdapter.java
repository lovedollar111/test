package cn.dogplanet.ui.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.widget.RoundCornerImageView;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Product;
import cn.dogplanet.entity.ProductDetail;
import cn.dogplanet.net.volley.toolbox.ListImageListener;

public class ProductAdapter extends BaseAdapter {

    private List<Product> details;
    private Context context;

    public ProductAdapter(List<Product> details, Context context) {
        this.details = details;
        this.context = context;
    }

    @Override
    public int getCount() {
        return details.size();
    }

    public void clear(){
        details.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Product> details){
        this.details.addAll(details);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return details.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Product detail = details.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_product, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int padding = 15;
        if(position%2==0){
            viewHolder.layProduct.setPadding(AndroidUtil.dip2px(12), padding, AndroidUtil.dip2px(6), padding);
        }else{
            viewHolder.layProduct.setPadding(AndroidUtil.dip2px(6), padding, AndroidUtil.dip2px(12), padding);
        }
        String url=detail.getUrl();
        if (StringUtils.isNotBlank(url)) {
            viewHolder.imgProduct.setTag(url);
            ListImageListener imageListener = new ListImageListener(
                    viewHolder.imgProduct, WConstant.IMG_DEF_RES_ID,
                    WConstant.IMG_ERROR_RES_ID, url);
            GlobalContext.getInstance().getImageLoader()
                    .get(url, imageListener);
        } else {
            viewHolder.imgProduct.setImageResource(WConstant.IMG_DEF_RES_ID);
        }
        if(StringUtils.isNotBlank(detail.getName())){
            viewHolder.tvName.setText(detail.getName());
        }
        if(StringUtils.isNotBlank(detail.getPrice().toString())){
            viewHolder.tvPrice.setText(String.format("%d", detail.getPrice()));
        }
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.img_product)
        RoundCornerImageView imgProduct;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.lay_product)
        LinearLayout layProduct;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
