package cn.dogplanet.ui.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.DateUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.widget.RoundCornerImageView;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.CartResp.Cart;
import cn.dogplanet.net.volley.toolbox.ListImageListener;

/**
 * 购物车Adapter
 * editor:ztr
 * package_name:cn.dogplanet.ui.shop.adapter
 * file_name:CartAdapter.java
 * date:2016-12-6
 */
public class CartAdapter extends BaseAdapter {

    /**
     * 上下文对象
     */
    private Context mContext;
    private List<Cart> mCarts;

    private Set<String> chkCarts;


    private OnUpdateMoneyListener refershMoneyListener;
    private OnItemLongClickListener onItemLongClickListener;

    /**
     */
    public CartAdapter(Context ctx, List<Cart> carts, OnUpdateMoneyListener listener, OnItemLongClickListener onItemLongClickListener) {
        mContext = ctx;
        refershMoneyListener = listener;
        this.onItemLongClickListener = onItemLongClickListener;
        chkCarts = new HashSet<>();
        this.mCarts = carts;
        for (Cart c : mCarts) {
            chkCarts.add(c.getId());
        }
    }

    public void clear() {
        mCarts.clear();
    }

    /**
     * 购物车总金额
     */
    public String sumCartMoney() {
        String sum = "0";
        if (null == mCarts || mCarts.isEmpty()) {
            return sum;
        }

        int s = 0;
        for (Cart c : mCarts) {
            if (chkCarts.contains(c.getId())) {
                int p = Integer.parseInt(c.getPrice())
                        * Integer.parseInt(c.getNum());
                s += p;
            }
        }
        return String.valueOf(s);

    }

    public void addAll(List<Cart> carts) {
        this.mCarts.addAll(carts);
    }

    public ArrayList<String> getProId() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Cart c : mCarts) {
            if (chkCarts.contains(c.getId())) {
                arrayList.add(c.getId());
            }
        }
        return arrayList;
    }

    public List<Cart> getCarts() {
        return mCarts;
    }


    public void removeCart(Cart cart) {
        chkCarts.remove(cart.getId());
        this.mCarts.remove(cart);
    }

    public List<String> getChkIds() {
        if (null == chkCarts || chkCarts.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(chkCarts);
    }

    @Override
    public int getCount() {
        return this.mCarts.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mCarts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Cart cart = mCarts.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.shop_cart_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgChoose.setTag(true);
        holder.imgChoose.setOnClickListener(v -> {
            if ((boolean) holder.imgChoose.getTag()) {
                holder.imgChoose.setTag(false);
                holder.imgChoose.setImageResource(R.drawable.ic_pay_select);
                chkCarts.add(cart.getId());
            } else {
                holder.imgChoose.setTag(true);
                holder.imgChoose.setImageResource(R.drawable.ic_pay_normal);
                chkCarts.remove(cart.getId());
            }
            refershMoneyListener.refreshMoney();
        });
        if (StringUtils.isNotBlank(cart.getThumb())) {
            holder.imgProduct.setTag(cart.getThumb());
            ListImageListener imageListener = new ListImageListener(
                    holder.imgProduct, WConstant.IMG_DEF_RES_ID,
                    WConstant.IMG_ERROR_RES_ID, cart.getThumb());
            GlobalContext.getInstance().getImageLoader()
                    .get(cart.getThumb(), imageListener);
        }
        holder.tvDate.setText(cart.getBegin_date());
        holder.tvName.setText(cart.getName());
        holder.tvPrice.setText(cart.getPrice());
        holder.tvNum.setText(cart.getNum());
        View finalConvertView = convertView;
        holder.layMain.setOnLongClickListener(v -> {
            onItemLongClickListener.onItemLongClick(finalConvertView, position);
            return false;
        });
        return convertView;
    }


    public interface OnUpdateMoneyListener {
        void refreshMoney();
    }

    static class ViewHolder {
        @BindView(R.id.img_choose)
        ImageView imgChoose;
        @BindView(R.id.img_product)
        RoundCornerImageView imgProduct;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.lay_main)
        RelativeLayout layMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }
}
