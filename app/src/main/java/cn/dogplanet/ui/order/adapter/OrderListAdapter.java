package cn.dogplanet.ui.order.adapter;

import android.content.Context;
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
import cn.dogplanet.entity.OrderDetail;
import cn.dogplanet.net.volley.toolbox.ListImageListener;

public class OrderListAdapter extends BaseAdapter {

    private List<OrderDetail.OrderProduct> products;
    private Context context;

    public OrderListAdapter(List<OrderDetail.OrderProduct> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear(){
        this.products.clear();
    }

    public void addAll(List<OrderDetail.OrderProduct> products){
        this.products=products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        OrderDetail.OrderProduct product=products.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list_adapter, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        if (StringUtils.isNotBlank(product.getThumb())) {
            String url = product.getThumb();
            holder.imgProduct.setTag(url);
            ListImageListener imageListener = new ListImageListener(holder.imgProduct,
                    WConstant.IMG_ERROR_RES_ID, WConstant.IMG_ERROR_RES_ID, url);
            GlobalContext.getInstance().getImageLoader().get(url,imageListener);
        }
        holder.tvProductDate.setText(product.getBegin_date());
        holder.tvProductName.setText(product.getPro_name());
        holder.tvProductNum.setText(String.format("%s人", product.getNum()));
        holder.tvProductPrice.setText(String.format("¥%s", product.getPrice()));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_product)
        RoundCornerImageView imgProduct;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_product_price)
        TextView tvProductPrice;
        @BindView(R.id.tv_product_num)
        TextView tvProductNum;
        @BindView(R.id.tv_product_date)
        TextView tvProductDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
