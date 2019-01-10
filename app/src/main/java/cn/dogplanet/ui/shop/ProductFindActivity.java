package cn.dogplanet.ui.shop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.aip.asrwakeup3.core.recog.MyRecognizer;
import com.baidu.aip.asrwakeup3.core.recog.RecogResult;
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
import cn.dogplanet.app.util.AndroidUtil;
import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.ListDataSave;
import cn.dogplanet.app.util.PullToRefreshHelper;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.FlowLayout;
import cn.dogplanet.app.widget.VoiceLineView;
import cn.dogplanet.app.widget.library.PullToRefreshBase;
import cn.dogplanet.app.widget.library.PullToRefreshListView;
import cn.dogplanet.base.BaseActivity;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.constant.WCache;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.KeyWordResp;
import cn.dogplanet.entity.Product;
import cn.dogplanet.entity.ProductListResp;
import cn.dogplanet.net.PublicReq;
import cn.dogplanet.ui.shop.adapter.ProductListAdapter;

public class ProductFindActivity extends BaseActivity {

    private static final String SHOP_FIND = "shop_find";
    private static final String SHOP_FIND_HISTORY = "shop_find_history";

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_input)
    ImageView imgInput;
    @BindView(R.id.lay_search)
    RelativeLayout laySearch;
    @BindView(R.id.list_result)
    PullToRefreshListView listResult;
    @BindView(R.id.recycler_history)
    FlowLayout flowHistory;
    @BindView(R.id.recycler_hot)
    FlowLayout flowHot;
    @BindView(R.id.lay_history)
    RelativeLayout layHistory;
    @BindView(R.id.lay_hot)
    LinearLayout layHot;
    @BindView(R.id.tv_touch)
    TextView tvTouch;
    @BindView(R.id.img_background)
    ImageView imgBackground;
    @BindView(R.id.img_rec_input)
    ImageView imgRecInput;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.volume_view)
    VoiceLineView volumeView;
    @BindView(R.id.tv_hit1)
    TextView tvHit1;
    @BindView(R.id.tv_hit2)
    TextView tvHit2;
    @BindView(R.id.tv_hit3)
    TextView tvHit3;
    @BindView(R.id.tv_hit4)
    TextView tvHit4;
    @BindView(R.id.layout_recog)
    RelativeLayout layoutRecog;
    @BindView(R.id.img_tip)
    ImageView imgTip;


    private boolean flag = false;//判断是不是语音输入
    private Expert expert;

    private ListDataSave listDataSave;
    private List<String> histories;
    private int pageId = 1;

    private ProductListAdapter adapter;

    private long mExitTime;
    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;

    protected Handler handler = new Handler() {

        /*
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }

    };


    public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(), ProductFindActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_find);
        ButterKnife.bind(this);
        histories = new ArrayList<>();
        listDataSave = new ListDataSave(this, SHOP_FIND);
        expert = WCache.getCacheExpert();
        // DEMO集成步骤 1.1 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
        IRecogListener listener = new MessageStatusRecogListener(handler);
        // DEMO集成步骤 1.2 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例
        myRecognizer = new MyRecognizer(this, listener);
        myRecognizer.setEventListener(new IRecogListener() {
            @Override
            public void onAsrReady() {

            }

            @Override
            public void onAsrBegin() {

            }

            @Override
            public void onAsrEnd() {

            }

            @Override
            public void onAsrPartialResult(String[] results, RecogResult recogResult) {

            }

            @Override
            public void onAsrOnlineNluResult(String nluResult) {

            }

            @Override
            public void onAsrFinalResult(String[] results, RecogResult recogResult) {
                tvMsg.setText(results[0]);
                etSearch.setText(results[0]);
                getProduct(results[0]);
            }

            @Override
            public void onAsrFinish(RecogResult recogResult) {

            }

            @Override
            public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecogResult recogResult) {
                if (StringUtils.isNotBlank(descMessage)) {
                    if (errorCode == 7001) {
                        ToastUtil.showError("未监测到语音");
                    } else if (errorCode == 9001) {
                        ToastUtil.showError("请开启语音输入权限");
                    } else if (errorCode >= 2000 && errorCode <= 2100) {
                        ToastUtil.showError("网络连接异常");
                    } else if (errorCode == 6001) {
                        ToastUtil.showError("语音过长");
                    }
                    tvMsg.setVisibility(View.GONE);
                    volumeView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAsrLongFinish() {

            }

            @Override
            public void onAsrVolume(int volumePercent, int volume) {
                Log.i("info", volumePercent + " ");
                volumeView.setVolume(volumePercent * 4);
            }

            @Override
            public void onAsrAudio(byte[] data, int offset, int length) {

            }

            @Override
            public void onAsrExit() {

            }

            @Override
            public void onOfflineLoaded() {

            }

            @Override
            public void onOfflineUnLoaded() {

            }
        });
        initView();
        getHot();
        updateHistory();
    }

    private void initPopup(List<String> hints) {
        imgRecInput.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    imgRecInput.setImageResource(R.mipmap.img_input_select);
                    imgBackground.setVisibility(View.VISIBLE);
                    tvTouch.setVisibility(View.INVISIBLE);
                    tvMsg.setVisibility(View.VISIBLE);
                    tvMsg.setText("聆听中…");
                    volumeView.setVisibility(View.VISIBLE);
                    mExitTime = System.currentTimeMillis();
                    start();
                    break;
                case MotionEvent.ACTION_UP:
                    imgRecInput.setImageResource(R.mipmap.img_input_normal);
                    imgBackground.setVisibility(View.INVISIBLE);
                    tvTouch.setVisibility(View.VISIBLE);
                    if (System.currentTimeMillis() - mExitTime < 500) {
                        cancel();
                        ToastUtil.showError("时间过短未能识别");
                        tvMsg.setVisibility(View.VISIBLE);
                    } else {
                        stop();
                    }
                    break;
            }
            return true;
        });
        tvHit1.setText(hints.get(0));
        tvHit2.setText(hints.get(1));
        tvHit3.setText(hints.get(2));
        tvHit4.setText(hints.get(3));
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    protected void start() {
        flag = true;
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
                        // 可以用下面一行替代，在logcat中查看代码
                        Log.w("AutoCheckMessage", message);
                        if (StringUtils.isNotBlank(message)) {
                            ToastUtil.showError("语音识别暂时无法使用");
                            layoutRecog.setVisibility(View.GONE);
                            tvMsg.setVisibility(View.GONE);
                            volumeView.setVisibility(View.GONE);
                            layHot.setVisibility(View.VISIBLE);
                            layHistory.setVisibility(View.VISIBLE);
                        }
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


    private void updateHistory() {
        histories = listDataSave.getDataList(SHOP_FIND_HISTORY);
        if (histories != null && histories.size() > 0) {
            layHistory.setVisibility(View.VISIBLE);
            if (flowHistory.getChildCount() > 0) {
                flowHistory.removeAllViews();
            }
            for (String s : histories) {
                TextView tv = new TextView(this);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setTextColor(getResources().getColor(R.color.color_c7));
                tv.setTextSize(13);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(AndroidUtil.dip2px(20), AndroidUtil.dip2px(12), AndroidUtil.dip2px(20), AndroidUtil.dip2px(12));
                tv.setLayoutParams(params);
                tv.setText(s);
                tv.setOnClickListener(v -> etSearch.setText(s));
                flowHistory.addView(tv);
            }
        } else {
            layHistory.setVisibility(View.GONE);
        }
    }

    private void getHot() {
        if (expert != null) {
            Map<String, String> params = new HashMap<>();
            params.put("expert_id", expert.getId().toString());
            params.put("access_token", expert.getAccess_token());
            PublicReq.request(HttpUrl.GET_SEARCH_KEYWORDS, response -> {
                if (response != null) {
                    KeyWordResp resp = GsonHelper.parseObject(response, KeyWordResp.class);
                    if (resp != null) {
                        if (resp.isSuccess()) {
                            updateHot(resp.getKeywordsList());
                        }
                    }
                }

            }, error -> ToastUtil.showError(R.string.network_error), params);
        }
    }

    private void updateHot(List<KeyWordResp.KeywordsList> keywordsList) {
        layHot.setVisibility(View.VISIBLE);
        if (flowHot.getChildCount() > 0) {
            flowHot.removeAllViews();
        }
        List<String> hints = new ArrayList<>();
        for (KeyWordResp.KeywordsList s : keywordsList) {
            TextView tv = new TextView(this);
            tv.setBackgroundResource(R.drawable.back_round_e5);
            tv.setPadding(AndroidUtil.dip2px(15), 0, AndroidUtil.dip2px(15), 0);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.color_8e));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AndroidUtil.dip2px(44f));
            params.setMargins(AndroidUtil.dip2px(12), AndroidUtil.dip2px(12), AndroidUtil.dip2px(12), AndroidUtil.dip2px(12));
            tv.setLayoutParams(params);
            tv.setText(s.getKeywords());
            tv.setOnClickListener(v -> etSearch.setText(s.getKeywords()));
            flowHot.addView(tv);
            hints.add(s.getKeywords());
        }
        initPopup(hints);
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
                if (flag) {
                    return;
                }
                if (StringUtils.isNotBlank(s.toString())) {
                    getProduct(s.toString());
                }
            }
        });
        listResult.setMode(PullToRefreshBase.Mode.BOTH);
        PullToRefreshHelper.initIndicator(listResult);
        PullToRefreshHelper.initIndicatorStart(listResult);
        listResult.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                pageId = 1;
                getProduct(etSearch.getText().toString());
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 刷新
                pageId++;
                getProduct(etSearch.getText().toString());
            }
        });
    }


    private void getProduct(String s) {
        Map<String, String> params = new HashMap<>();
        params.put("expert_id", expert.getId().toString());
        params.put("access_token", expert.getAccess_token());
        params.put("desc", s.trim());
        params.put("page_id", String.valueOf(pageId));

        boolean isSingle = true;
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).equals(s)) {
                isSingle = false;
            }
        }
        if (isSingle || histories.size() == 0) {
            histories.add(s);
        }
        showProgress();
        listDataSave.setDataList(SHOP_FIND_HISTORY, histories);
        updateHistory();
        PublicReq.request(HttpUrl.GET_SEARCH,
                response -> {
                    hideProgress();
                    flag = false;
                    if (StringUtils.isNotBlank(response)) {
                        ProductListResp resp = GsonHelper.parseObject(response, ProductListResp.class);
                        if (resp != null) {
                            if (pageId == 1) {
                                if (adapter != null) {
                                    adapter.clear();
                                }
                            }
                            updateView(resp.getProducts());
                        }
                    }

                }, error -> ToastUtil.showError(R.string.network_error), params);
    }

    private void updateView(List<Product> products) {
        layoutRecog.setVisibility(View.GONE);
        tvMsg.setVisibility(View.GONE);
        volumeView.setVisibility(View.GONE);
        if (pageId == 1) {
            if (products == null || products.isEmpty()) {
                listResult.setVisibility(View.GONE);
                imgTip.setVisibility(View.VISIBLE);
                layHistory.setVisibility(View.GONE);
                layHot.setVisibility(View.GONE);
            } else {
                listResult.setVisibility(View.VISIBLE);
                imgTip.setVisibility(View.GONE);
                layHistory.setVisibility(View.GONE);
                layHot.setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new ProductListAdapter(products, this);
                    listResult.setAdapter(adapter);
                } else {
                    adapter.clear();
                    adapter.addAll(products);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            listResult.setVisibility(View.VISIBLE);
            imgTip.setVisibility(View.GONE);
            layHistory.setVisibility(View.GONE);
            layHot.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new ProductListAdapter(products, this);
                listResult.setAdapter(adapter);
            } else {
                adapter.addAll(products);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick({R.id.btn_cancel, R.id.tv_clear, R.id.img_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.tv_clear:
                histories.clear();
                SharedPreferences sharedPreferences = getSharedPreferences(SHOP_FIND, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                updateHistory();
                break;
            case R.id.img_input:
                if (Build.VERSION.SDK_INT >= 23) {
                    initPermission();
                } else {
                    if (layoutRecog.getVisibility() == View.GONE) {
                        layoutRecog.setVisibility(View.VISIBLE);
                        layHot.setVisibility(View.GONE);
                        layHistory.setVisibility(View.GONE);
                    } else {
                        layoutRecog.setVisibility(View.GONE);
                        layHot.setVisibility(View.VISIBLE);
                        tvMsg.setVisibility(View.GONE);
                        volumeView.setVisibility(View.GONE);
                        updateHistory();
                        flag = false;
                    }
                }
                break;
        }
    }

    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        } else {
            if (layoutRecog.getVisibility() == View.GONE) {
                layoutRecog.setVisibility(View.VISIBLE);
                layHot.setVisibility(View.GONE);
                layHistory.setVisibility(View.GONE);
            } else {
                layoutRecog.setVisibility(View.GONE);
                layHot.setVisibility(View.VISIBLE);
                tvMsg.setVisibility(View.GONE);
                volumeView.setVisibility(View.GONE);
                updateHistory();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 权限被用户同意，可以去放肆了。
            start();
        } else {
            ToastUtil.showError("请开启语音输入权限。");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRecognizer.cancel();
    }
}
