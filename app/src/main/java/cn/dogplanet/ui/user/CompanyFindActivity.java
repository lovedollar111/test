package cn.dogplanet.ui.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.aip.asrwakeup3.core.recog.MyRecognizer;
import com.baidu.aip.asrwakeup3.core.recog.listener.IRecogListener;
import com.baidu.aip.asrwakeup3.core.recog.listener.MessageStatusRecogListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.KeyBoardUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.Resp;
import cn.dogplanet.entity.Travel;
import cn.dogplanet.entity.TravelResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.ui.user.adapter.CompanyAdapter;

public class CompanyFindActivity extends BaseActivity {

    public static final int COMPANY_FIND_CODE = 10002;


    public static final String COMPANY_ID = "find_company_id";
    public static final String COMPANY_NAME = "find_company_name";
    public static final String COMPANY_TYPE = "find_company_type";

    @BindView(R.id.et_search)
    EditTextWithDel etSearch;
    @BindView(R.id.lay_search)
    RelativeLayout laySearch;
    @BindView(R.id.lay_hint)
    RelativeLayout layHint;
    @BindView(R.id.list_company)
    ListView listCompany;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private MyRecognizer myRecognizer;
    private boolean flag=false;//判断是不是语音输入
    private String type,old_travel_agency_id;
    private Expert expert;
    private List<Travel> travels;

    public static Intent newIntent(String companyId,String type) {
       Intent intent=new Intent(GlobalContext.getInstance(), CompanyFindActivity.class);
       intent.putExtra(COMPANY_TYPE,type);
       intent.putExtra(COMPANY_ID,companyId);
       return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_find);
        ButterKnife.bind(this);
        type=getIntent().getStringExtra(COMPANY_TYPE);
        old_travel_agency_id=getIntent().getStringExtra(COMPANY_ID);
        expert= WCache.getCacheExpert();
        initView();
        IRecogListener listener = new MessageStatusRecogListener(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }

        });
        // DEMO集成步骤 1.2 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例
        myRecognizer = new MyRecognizer(this, listener);
    }

    protected void handleMsg(Message msg) {
        if (etSearch != null && msg.obj != null && msg.arg2==1) {
            if(!"错误码".contains((CharSequence) msg.obj)){
                etSearch.setText(msg.obj.toString());
                getCompany(msg.obj.toString());
                flag=false;
            }
            stop();
        }
    }

    private void initView() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(flag){
                    return;
                }
                if(StringUtils.isNotBlank(s.toString())){
                    getCompany(s.toString());
                }
            }
        });
    }


    @OnClick({R.id.btn_cancel, R.id.lay_hint,R.id.img_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.lay_hint:
                layHint.setVisibility(View.GONE);
                laySearch.setVisibility(View.VISIBLE);
                break;
            case R.id.img_input:
                if (Build.VERSION.SDK_INT >= 23) {
                    initPermission();
                } else {
                    start();
                }
                KeyBoardUtils.closeKeybord(etSearch,this);
                break;
        }
    }

    protected void start() {
        flag=true;
        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        final Map<String, Object> params = new HashMap<>();
        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印

        // 复制此段可以自动检测常规错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        ToastUtil.showError(message);
                    }
                }
            }
        }, false)).checkAsr(params);

        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        // DEMO集成步骤2.2 开始识别
        myRecognizer.start(params);
    }

    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     */
    protected void stop() {
        // DEMO集成步骤4 (可选） 停止录音
        myRecognizer.stop();
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     */
    protected void cancel() {
        // DEMO集成步骤5 (可选） 取消本次识别
        myRecognizer.cancel();
    }


    private void getCompany(String s) {
        Map<String, String> params = new HashMap<>();
        showProgress();
        params.put("desc",s.trim());
        PublicReq.request(HttpUrl.GET_TRAVEL,
                response -> {
                    hideProgress();
                    if (StringUtils.isNotBlank(response)) {
                        TravelResp resp= GsonHelper.parseObject(response, TravelResp.class);
                        if (resp != null) {
                            updateView(resp);
                        }
                    }

                }, error -> ToastUtil.showError(R.string.network_error), params);
    }

    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }else{
            start();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 权限被用户同意，可以去放肆了。
            start();
        } else {
            ToastUtil.showError("请开启语音输入权限。");
        }
    }

    private void updateView(TravelResp resp) {
        travels = resp.getTravelAgency();
        if(travels !=null&& travels.size()>0){
            tvTip.setVisibility(View.GONE);
            listCompany.setVisibility(View.VISIBLE);
            CompanyAdapter companyAdapter = new CompanyAdapter(resp.getTravelAgency(), this, false, "");
            listCompany.setAdapter(companyAdapter);
            companyAdapter.notifyDataSetChanged();
            listCompany.setOnItemClickListener((parent, v, position, id) -> {
                if(type.equals(CompanyListActivity.TYPE_CHANGE)){
                    View view = LayoutInflater.from(this).inflate(R.layout.dialog_ok,
                            null);
                    NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
                    builder.setCustomView(view, this);
                    builder.withEffect(Effectstype.Fadein);
                    builder.show(); TextView tv_title = view.findViewById(R.id.title);
                    tv_title.setText("提示");
                    TextView tv_msg = view.findViewById(R.id.msg);
                    tv_msg.setText("您的更换公司申请将提交给原公司，原公司同意后进入新公司审核流程，共计约7个工作日，请您耐心等待，在此期间内，您的账号无法进行下单操作。");
                    Button button = view.findViewById(R.id.btn_ok);
                    button.setText("确定");
                    button.setOnClickListener(v1 -> {
                        builder.dismiss();
                        changeCompany(travels.get(position).getId().toString(),position);
                    });
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(COMPANY_ID, travels.get(position).getId().toString());
                    intent.putExtra(COMPANY_NAME, travels.get(position).getName());
                    setResult(COMPANY_FIND_CODE, intent);
                    finish();
                }

            });
        }else{
            tvTip.setVisibility(View.VISIBLE);
            listCompany.setVisibility(View.GONE);
        }

    }

    private void changeCompany(String company_id,int position) {
        if(!company_id.equals(old_travel_agency_id)){
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            params.put("old_travel_agency_id", old_travel_agency_id);
            params.put("new_travel_agency_id", company_id);
            PublicReq.request(HttpUrl.CHANGE_TRAVEL_AGENCY, response -> {
                Resp resp = GsonHelper.parseObject(response, Resp.class);
                if (resp != null && resp.isSuccess()) {
                    ToastUtil.showMes("保存成功");
                    Intent intent = new Intent();
                    intent.putExtra(COMPANY_ID, travels.get(position).getId().toString());
                    intent.putExtra(COMPANY_NAME, travels.get(position).getName());
                    setResult(COMPANY_FIND_CODE, intent);
                    finish();
                } else {
                    ToastUtil.showError(R.string.network_data_error);
                }

            }, error -> ToastUtil.showError(R.string.network_error), params);
        }
    }

    @Override
    protected void onDestroy() {
        // DEMO集成步骤3 释放资源
        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        myRecognizer.release();
        super.onDestroy();
    }
}
