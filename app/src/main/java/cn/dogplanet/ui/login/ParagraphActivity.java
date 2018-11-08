package cn.dogplanet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import butterknife.ButterKnife;
import cn.dogplanet.GlobalContext;
import cn.dogplanet.R;
import cn.dogplanet.base.BaseActivity;

/**
 * 免责申明
 * editor:ztr
 * package_name:cn.dogplanet.ui.login
 * file_name:Paragraph.java
 * date:2016-12-6
 */
public class ParagraphActivity extends BaseActivity implements OnClickListener {

	public static Intent newIntent() {
        return new Intent(GlobalContext.getInstance(),
				ParagraphActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paragraph);
		ButterKnife.bind( this ) ;
		findViewById(R.id.btn_back).setOnClickListener(this);
		TextView textView= this.findViewById(R.id.pa_text);
		textView.setText("一、总则\n汪汪星球www.dogplanet.cn（三亚汪汪信息科技有限公司）秉承“我们自己的服务理念”的优质待客理念，以客人满意为标准，以保障客人安全为前提，为实现境内外旅游完美协作而共同努力。\n" +
				"二、服务说明\n达人、导游、司机的基本服务为实地向导，额外项目由达人、导游、司机与客人双方商定，相关责任与汪汪星球www.dogplanet.cn（三亚汪汪信息科技有限公司）无关。\n" +
				"达人、导游、司机单次接待的人数上限根据实际情况而定，客人与达人具体协商，汪汪星球www.dogplanet.cn（三亚汪汪信息科技有限公司）不作实际设定，但需达到优质服务每一个客人这一宗旨。\n\n" +
				"三、服务范围\n达人、导游、司机服务可涵盖两大部分：伴游向导服务和旅行生活服务。\n伴游向导服务：伴游向导服务包括讲解服务、行程规划、摄影摄像、代订门票和餐饮等。\n" +
				"(一）讲解服务：包括景点讲解和陪同翻译。\n(二）行程规划：包括帮客人设计安排新鲜优质的观光线路和观光中所需的其他产品。用私家车兼任客人的专属司机。\n(三）摄影摄像：包括照相和录像。部分达人、导游、司机有专业相机可在伴游的过程中帮客人拍照，客人也可自带相机或DV让达人、导游、司机帮助拍摄。\n" +
				"(四）代订门票和餐饮：在伴游过程中达人、导游、司机可帮客人代订景点门票和游玩当天所需的餐饮服务。\n\n旅行生活服务：旅行生活服务包括接送机、目的地到达、接送出入境等其他生活服务。\n" +
				"(一）出入境接送。包括接送客人航班、轮船、火车、汽车等。\n(二）代订酒店和机票。帮助客人代订境内外当地酒店、宾馆、民宿等以及客人返程机票。\n(三）购物和退税服务。带领客人购买特产、纪念品等，并协助客人办理商品退税等服务。\n" +
				"(四）医疗和整容服务。协助客人到正规医疗服务机构体检、就诊及整容整形服务。\n(五）娱乐服务。协助客人享用正规休闲娱乐服务。\n\n" +
				"四、安全要求\n达人、导游、司机应自觉维护自身形象，耐心细致，尽职尽责，竭力维护保障每一位客人的正当权益和人身财产安全。\n" +
				"（一）为旅客代订住宿时须遵守如下要求：\n1、了解酒店的安全通道或紧急通道情况，熟悉逃生路线；\n2、清楚客人所住房间的楼层、位置；\n3、提醒客人出门时及睡前，检查门窗是否关好，同时保管好自己的房间钥匙；\n4、提醒客人将贵重物品随身保管或在酒店寄存。\n\n" +
				"（二）为旅客提供车辆服务时须遵守如下要求：\n1、确保车况，确保行车安全；\n2、提前确认路况，极端天气时谨慎驾驶；\n3、不疲劳驾驶、超速行驶；\n4、客人上车坐稳以后方可开车；\n5、提示客人在行车途中不要随意走动，以免发生危险；\n6、提示客人在车辆行驶时，不要使用剪刀、水果刀等锐器，以免发生危险。\n\n" +
				"（三）陪同客人游览时须遵守如下要求：\n1、预先告知客人景区的安全须知和宗教风俗禁忌；\n2、注意周围环境的安全，防止游客走失迷路或发生其他事故；\n3、陪同过程中不得私自脱离客人独自行动；\n4、遇天气不好时，要及时了解前往地点是否安全，如需变更行程计划，须向游客作好解释说明工作。\n\n" +
				"（四）带客人就餐时须遵守如下要求：\n1、按照客人需求，带客人到正规餐厅用餐；\n2、尽量让客人在餐厅门口下车，然后再泊车，避免让客人直接穿越马路；\n3、带客人通过餐厅通道、楼梯时，如发现地面油腻、台阶破损、地毯卷曲时，要适时提醒客人注意脚下安全；\n4、提醒客人注意饮食卫生。\n\n" +
				"五、未尽事项按国家法律法规及相关规章规定执行。\n本守则最终解释权和增补权属汪汪星球www.dogplanet.cn（三亚汪汪信息科技有限公司）所有。");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()== R.id.btn_back){
			finish();
		}
	}
}
