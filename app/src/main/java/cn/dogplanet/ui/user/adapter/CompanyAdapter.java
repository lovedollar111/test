package cn.dogplanet.ui.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.widget.CircleImageView;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.entity.Travel;
import cn.dogplanet.net.volley.toolbox.ListImageListener;

public class CompanyAdapter extends BaseAdapter {

    private List<Travel> travels;
    private Context mContext;
    private Boolean isRem;
    private String choose_id;

    public CompanyAdapter(List<Travel> travels, Context mContext, Boolean isRem, String choose_id) {
        this.travels = travels;
        this.mContext = mContext;
        this.isRem = isRem;
        this.choose_id = choose_id;
    }

    @Override
    public int getCount() {
        return travels.size();
    }

    @Override
    public Object getItem(int position) {
        return travels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_company, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!isRem) {
            viewHolder.viewLine.setVisibility(View.GONE);
        } else {
            viewHolder.viewLine.setVisibility(View.VISIBLE);
        }
        Travel travel = travels.get(position);
        if (StringUtils.isNotBlank(travel.getLogoUrl())) {
            viewHolder.imgCompany.setTag(travel.getLogoUrl());
            ListImageListener imageListener = new ListImageListener(
                    viewHolder.imgCompany, R.mipmap.company_logo, R.mipmap.company_logo,
                    travel.getLogoUrl());
            GlobalContext.getInstance().getImageLoader()
                    .get(travel.getLogoUrl(), imageListener);
        } else {
            viewHolder.imgCompany.setImageResource(R.mipmap.company_logo);
        }
        if (StringUtils.isNotBlank(travel.getHead())) {
            viewHolder.tvCompanyName.setText(travel.getName());
        }
        if (StringUtils.isNotBlank(choose_id)) {
            if (choose_id.equals(travel.getId().toString())) {
                viewHolder.tvCompanyName.setTextColor(Color.rgb(241, 48, 81));
            } else {
                viewHolder.tvCompanyName.setTextColor(Color.BLACK);
            }
        }
        if (StringUtils.isNotBlank(travel.getIntroduce())) {
            viewHolder.tvMsg.setText(travel.getIntroduce());
        } else {
            viewHolder.tvMsg.setText("无");
        }
        if (StringUtils.isNotBlank(travel.getExpertCount())) {
            viewHolder.tvPeopleNum.setText(travel.getExpertCount());
        }
        if (StringUtils.isNotBlank(travel.getHead_tel())) {
            viewHolder.imgCompanyPhone.setVisibility(View.VISIBLE);
            viewHolder.tvCompanyPhone.setVisibility(View.VISIBLE);
            viewHolder.layCompanyPhone.setVisibility(View.VISIBLE);
            viewHolder.tvCompanyPhone.setText(String.format("%s   %s", travel.getName(), travel.getHead_tel()));
            viewHolder.layCompanyPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCallDialog(travel.getHead_tel());

                }
            });
        } else {
            viewHolder.imgCompanyPhone.setVisibility(View.GONE);
            viewHolder.tvCompanyPhone.setVisibility(View.GONE);
            viewHolder.layCompanyPhone.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void showCallDialog(String phone) {
        NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(mContext);
        builder.withEffect(Effectstype.Fadein);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_phone, null);
        builder.setCustomView(view, mContext);
        ButterKnife.findById(view, R.id.btn_ok).setOnClickListener(v -> {
            callPhone(phone);
            builder.cancel();
        });
        TextView textView = ButterKnife.findById(view, R.id.tv_phone);
        textView.setText(phone);
        builder.show();
    }

    private void callPhone(String phone) {
        if (phone == null || phone.equals("")) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        mContext.startActivity(intent);
    }

    static class ViewHolder {
        @BindView(R.id.view_line)
        View viewLine;
        @BindView(R.id.img_company)
        CircleImageView imgCompany;
        @BindView(R.id.tv_company_name)
        TextView tvCompanyName;
        @BindView(R.id.tv_msg)
        TextView tvMsg;
        @BindView(R.id.img_company_phone)
        ImageView imgCompanyPhone;
        @BindView(R.id.tv_company_phone)
        TextView tvCompanyPhone;
        @BindView(R.id.tv_people_num)
        TextView tvPeopleNum;
        @BindView(R.id.lay_company_phone)
        RelativeLayout layCompanyPhone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
