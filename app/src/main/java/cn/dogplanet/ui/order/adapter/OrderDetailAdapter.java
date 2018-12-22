package cn.dogplanet.ui.order.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

/**
 * 订单详情Adapter editor:ztr package_name:cn.dogplanet.ui.order.adapter
 * file_name:OrderDetailAdapter.java date:2016-12-6
 */
public class OrderDetailAdapter extends BaseAdapter {

    private List<OrderDetail.OrderProduct> products;
    private Context context;
    private OnReturnClickListener onReturnClickListener;

    public OrderDetailAdapter(List<OrderDetail.OrderProduct> products, Context context, OnReturnClickListener onReturnClickListener) {
        this.products = products;
        this.context = context;
        this.onReturnClickListener = onReturnClickListener;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_detail_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderDetail.OrderProduct product = products.get(position);
        if (StringUtils.isNotBlank(product.getThumb())) {
            String url = product.getThumb();
            holder.imgProduct.setTag(url);
            ListImageListener imageListener = new ListImageListener(holder.imgProduct,
                    WConstant.IMG_ERROR_RES_ID, WConstant.IMG_ERROR_RES_ID, url);
            GlobalContext.getInstance().getImageLoader().get(url,imageListener);
        }
        String status=product.getStatus();

        switch (status){
            case OrderDetail.ORDER_TYPE_WAIT:
                holder.btnReturn.setVisibility(View.INVISIBLE);
                holder.tvCode.setVisibility(View.INVISIBLE);
                break;
            case OrderDetail.ORDER_TYPE_SUCCESS:
                holder.btnReturn.setVisibility(View.VISIBLE);
                holder.tvCode.setVisibility(View.VISIBLE);
                holder.btnReturn.setBackgroundResource(R.drawable.gradient_c7_ab);
                holder.btnReturn.setText("申请退款");
                holder.btnReturn.setEnabled(true);
                if(StringUtils.isNotBlank(product.getCheck_number())){
                    holder.tvCode.setText(String.format("取票辅助码: %s", product.getCheck_number()));
                }else {
                    holder.tvCode.setText("取票辅助码: ");
                } break;
            case OrderDetail.ORDER_TYPE_CLOSE:
                holder.btnReturn.setVisibility(View.INVISIBLE);
                holder.tvCode.setVisibility(View.INVISIBLE);
                break;
            case OrderDetail.ORDER_TYPE_BACK_MONEY:
                holder.btnReturn.setVisibility(View.VISIBLE);
                holder.tvCode.setVisibility(View.VISIBLE);
                holder.btnReturn.setBackgroundResource(R.drawable.gradient_fa_f1);
                holder.btnReturn.setText("退款中");
                holder.btnReturn.setEnabled(false);
                if(StringUtils.isNotBlank(product.getCheck_number())){
                    holder.tvCode.setText(String.format("取票辅助码: %s", product.getCheck_number()));
                }else {
                    holder.tvCode.setText("取票辅助码: ");
                }break;
            case OrderDetail.ORDER_TYPE_BACK_ALL_MONEY:
                holder.btnReturn.setVisibility(View.VISIBLE);
                holder.tvCode.setVisibility(View.VISIBLE);
                holder.btnReturn.setBackgroundResource(R.drawable.gradient_f6);
                holder.btnReturn.setText("退款成功");
                holder.btnReturn.setEnabled(false);
                if(StringUtils.isNotBlank(product.getCheck_number())){
                    holder.tvCode.setText(String.format("取票辅助码: %s", product.getCheck_number()));
                }else {
                    holder.tvCode.setText("取票辅助码: ");
                }
                break;
        }
        holder.tvProductDate.setText(product.getBegin_date());
        holder.tvProductName.setText(product.getPro_name());
        holder.tvProductNum.setText(String.format("%s人", product.getNum()));
        holder.tvProductPrice.setText(String.format("¥%s", product.getPrice()));
        holder.btnReturn.setOnClickListener(v -> onReturnClickListener.onReturnClick(product.getId(), Integer.parseInt(product.getCheck_number()),product.getBegin_date(),product.isIs_package_ticket(),product.getPro_id(),product.getCategory()));

        return convertView;
    }

    public interface OnReturnClickListener {
        void onReturnClick(String id,int num,String date,boolean isPackage,String proId,String proCategory);
    }

    static class ViewHolder {
        @BindView(R.id.img_product)
        RoundCornerImageView imgProduct;
        @BindView(R.id.btn_return)
        Button btnReturn;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_product_num)
        TextView tvProductNum;
        @BindView(R.id.tv_product_date)
        TextView tvProductDate;
        @BindView(R.id.tv_product_price)
        TextView tvProductPrice;
        @BindView(R.id.tv_code)
        TextView tvCode;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
