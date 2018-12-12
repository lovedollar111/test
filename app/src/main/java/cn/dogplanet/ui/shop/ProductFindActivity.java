package cn.dogplanet.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.EditTextWithDel;
import cn.dogplanet.baiduApi.AutoCheck;
import cn.dogplanet.baiduApi.IRecogListener;
import cn.dogplanet.baiduApi.MessageStatusRecogListener;
import cn.dogplanet.baiduApi.MyRecognizer;
import cn.dogplanet.base.BaseActivity;

public class ProductFindActivity extends BaseActivity {


    @BindView(R.id.et_search)
    EditTextWithDel etSearch;
    @BindView(R.id.img_input)
    ImageView imgInput;
    @BindView(R.id.lay_search)
    RelativeLayout laySearch;
    private MyRecognizer myRecognizer;
    private boolean flag = false;//判断是不是语音输入

    public static Intent newIntent(){
        return new Intent(GlobalContext.getInstance(),ProductFindActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_find);
        ButterKnife.bind(this);
        IRecogListener listener = new MessageStatusRecogListener(new Handler() {
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
        if (etSearch != null && msg.obj != null && msg.arg2 == 1) {
            if (!"错误码".contains((CharSequence) msg.obj)) {
                etSearch.setText(msg.obj.toString());
                getProduct(msg.obj.toString());
                flag = false;
            }
            stop();
        }
    }

    private void getProduct(String s) {

    }

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

}
