package cn.dogplanet.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.AppManager;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.CircleImageView;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.base.BaseFragment;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.constant.WConstant;
import cn.dogplanet.entity.AreaListResp;
import cn.dogplanet.entity.CartResp;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.ShareData;
import cn.dogplanet.entity.SharePersonResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.net.RespData;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.net.volley.toolbox.ListImageListener;
import cn.dogplanet.ui.login.LoginActivity;
import cn.dogplanet.ui.popup.SharePopupWindow;
import cn.dogplanet.ui.shop.ShopProductCartActivity;
import cn.dogplanet.ui.user.AboutActivity;
import cn.dogplanet.ui.user.AuthenticationInfoActivity;
import cn.dogplanet.ui.user.InfoActivity;
import cn.dogplanet.ui.user.SafeActivity;

public class UserFragment extends BaseFragment {

    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.lay_main)
    LinearLayout layMain;

    private Unbinder bind;
    private Expert expert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        bind = ButterKnife.bind(this, view);
        expert = WCache.getCacheExpert();
        loadExpertData();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadExpertData();
    }

    private void loadExpertData() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            PublicReq.request(HttpUrl.EXPERT_PERSON_DATA,
                    response -> {
                        try {
                            RespData respData = GsonHelper.parseObject(
                                    response, RespData.class);
                            if (null != respData) {
                                if (respData.isValida()) {
                                    ToastUtil.showError("请重新登陆");
                                    SPUtils.clear();
                                    AppManager.getAppManager().finishAllActivity();
                                    startActivity(LoginActivity.newIntent());
                                } else if (respData.isSuccess()) {
                                    Expert et = GsonHelper.parseObject(
                                            GsonHelper.toJson(respData
                                                    .getExpert()),
                                            Expert.class);
                                    if (null != et) {
                                        SPUtils.put(WConstant.EXPERT_DATA,
                                                GsonHelper.toJson(et));
                                        expert = et;
                                        updateView(et);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> System.out.println("异常"), params);
        }
    }

    private void updateView(Expert et) {
        if (StringUtils.isNotBlank(et.getExpert_icon())) {
            imgHead.setTag(et.getExpert_icon());
            ListImageListener imageListener = new ListImageListener(
                    imgHead, R.mipmap.userimage, R.mipmap.userimage,
                    et.getExpert_icon());
            GlobalContext.getInstance().getImageLoader()
                    .get(et.getExpert_icon(), imageListener);
        }else{
            imgHead.setImageResource(R.mipmap.userimage);
        }
        tvName.setText(et.getExpert_name());
    }


    @OnClick({R.id.btn_quit,R.id.lay_info, R.id.lay_authentication_info, R.id.lay_safe, R.id.lay_invite, R.id.lay_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_info:
                startActivity(InfoActivity.newIntent());
                break;
            case R.id.lay_authentication_info:
                startActivity(AuthenticationInfoActivity.newIntent());
                break;
            case R.id.lay_safe:
                startActivity(SafeActivity.newIntent());
                break;
            case R.id.lay_invite:
                getInviteCode();
                break;
            case R.id.lay_about:
                startActivity(AboutActivity.newIntent());
                break;
            case R.id.btn_quit:
                exit();
                break;
        }
    }

    private void getInviteCode() {
        if (null != expert) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            PublicReq.request(HttpUrl.RECOMMEND_DOWNLOAD, response -> {
                SharePersonResp respData = GsonHelper.parseObject(response,
                        SharePersonResp.class);
                if (null != respData) {
                    updateView(respData.getInviteArr());
                }
            }, error -> {
            }, params);
        }
    }

    private void updateView(SharePersonResp.InviteArr inviteArr) {
        if (Expert.AUTHENTICATION_20.equals(expert.getAuthentication_status())) {
            ShareData shareData = new ShareData();
            shareData.setContent("旅行，不单单是从一个熟悉的地方去到一个陌生的地方，而是应该去像个当地人一样体验别样的风土人情，跟着我们的达人一起。");
            shareData.setPic("http://img0.dogplanet.cn/share_logo.jpg");
            shareData.setTitle("汪汪星球");
            String url = "http://a.app.qq.com/o/simple.jsp?pkgname=cn.dogplanet";
            shareData.setUrl(url);
            SharePopupWindow sharePopupWindow = new SharePopupWindow(getContext(), inviteArr.getInvite_code());
            sharePopupWindow.initShareParams(shareData);
            sharePopupWindow.showAtLocation(layMain, Gravity.BOTTOM
                    | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            ToastUtil.showError("通过认证审核后才可邀请好友");
        }

    }


    private void exit() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_ok,
                null);
        NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(getContext());
        builder.setCustomView(view, getContext());
        builder.withEffect(Effectstype.Fadein);
        builder.show();
        Button btn_ok = view.findViewById(R.id.btn_ok);
        btn_ok.setText("确定");
        btn_ok.setOnClickListener(v1 -> {
            SPUtils.clear();
            AppManager.getAppManager().finishAllActivity();
            startActivity(LoginActivity.newIntent());
            builder.dismiss();
        });
        TextView tv_title = view.findViewById(R.id.title);
        tv_title.setText("提示");
        tv_title.setTextSize(18);
        tv_title.setTextColor(Color.rgb(40, 44, 54));
        TextView tv_msg = view.findViewById(R.id.msg);
        tv_msg.setText(String.format("退出当前帐号(%s)？",expert.getExpert_account()));
        tv_msg.setTextSize(12);
        tv_msg.setTextColor(Color.rgb(72, 72, 72));
        tv_msg.setPadding(AndroidUtil.dip2px(30), AndroidUtil.dip2px(10), AndroidUtil.dip2px(30), AndroidUtil.dip2px(2));


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
