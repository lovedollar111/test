package cn.dogplanet.ui.shop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.widget.CircleImageView;
import cn.dogplanet.app.widget.RoundCornerImageView;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.Product;
import cn.dogplanet.net.volley.toolbox.ListImageListener;

public class ProductListAdapter extends BaseAdapter {

    private List<Product> products;
    private Context context;

    public ProductListAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    public void clear() {
        products.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Product> products) {
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Product product=products.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_product_list, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        String url=product.getUrl();
        if (StringUtils.isNotBlank(url)) {
            holder.imgProduct.setTag(url);
            ListImageListener imageListener = new ListImageListener(
                    holder.imgProduct, WConstant.IMG_DEF_RES_ID,
                    WConstant.IMG_ERROR_RES_ID, url);
            GlobalContext.getInstance().getImageLoader()
                    .get(url, imageListener);
        } else {
            holder.imgProduct.setImageResource(WConstant.IMG_DEF_RES_ID);
        }
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice().toString());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_product)
        RoundCornerImageView imgProduct;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
