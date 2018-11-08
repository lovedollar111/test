package cn.dogplanet.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.app.util.KeyBoardUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.app.util.ToastUtil;
import cn.dogplanet.app.widget.niftymodaldialogeffects.Effectstype;
import cn.dogplanet.app.widget.niftymodaldialogeffects.NiftyDialogBuilder;
import cn.dogplanet.constant.HttpUrl;
import cn.dogplanet.net.volley.Response;
import cn.dogplanet.net.volley.VolleyError;
import cn.dogplanet.net.volley.toolbox.ImageRequest;

/**
 * Created by zhangtianrui on 17/10/13.
 */

public class GraphicCodeDialog {

    public static final int TYPE_CANCEL=1;
    public static final int TYPE_OK=2;

    private Context context;
    private EditText editText;
    private String phoneNumber;
    private ImageView btn_change;
    private ImageView imageView;
    private NiftyDialogBuilder builder;
    private setOnDismissListener setOnDismissListener;

    public GraphicCodeDialog(Context context, String phoneNumber, setOnDismissListener setOnDismissListener) {
        this.context = context;
        this.phoneNumber=phoneNumber;
        this.setOnDismissListener=setOnDismissListener;
        init();
        getGraphicCode();
    }

    private void init() {
        View view= LayoutInflater.from(context).inflate(R.layout.graphic_code_view,null);
        builder = NiftyDialogBuilder.getInstance(context);
        builder.setCustomViewWithoutClose(view,context);
        builder.withEffect(Effectstype.Fadein);
        builder.setCanceledOnTouchOutside(false);
        editText= (EditText) view.findViewById(R.id.et_graphic_code);
        imageView= (ImageView) view.findViewById(R.id.img_graphic_code);
        btn_change= (ImageView) view.findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGraphicCode();
            }
        });
        btn_change.setImageResource(R.mipmap.change_one_normal);
        btn_change.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    btn_change.setImageResource(R.mipmap.change_one_highlight);
                }else{
                    btn_change.setImageResource(R.mipmap.change_one_normal);
                }
                return false;
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnDismissListener.onDismissListener(TYPE_CANCEL,null);
                dismiss();
            }
        });

        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isBlank((editText.getText().toString()))){
                    ToastUtil.showError("请输入图中验证码");
                    return;
                }
                setOnDismissListener.onDismissListener(TYPE_OK,editText.getText().toString());
            }
        });
    }

    private void getGraphicCode(){
        String img_path= HttpUrl.GET_CAPTCHA;
        ImageRequest imageRequest = new ImageRequest(img_path,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        GlobalContext.getInstance().getRequestQueue().add(imageRequest);
    }

    public void show(){
        KeyBoardUtils.openKeybord(editText,context);
        builder.show();
    }
    public void dismiss(){
        KeyBoardUtils.closeKeybord(editText,context);
        builder.dismiss();
    }

    public void clear(){
        editText.setText("");
    }


    public void refresh(){
        getGraphicCode();
        KeyBoardUtils.openKeybord(editText,context);
        clear();
    }

    public String getCode(){
        return editText.getText().toString();
    }

    public interface setOnDismissListener{
        void onDismissListener(int type, String code);
    }
}
