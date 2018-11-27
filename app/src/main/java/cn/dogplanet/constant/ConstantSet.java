/**
 */
package cn.dogplanet.constant;

/**
 * 保存常量的类
 * editor:ztr
 * package_name:cn.dogplanet.app.constant
 * file_name:ConstantSet.java
 * date:2016-12-6
 */
public class ConstantSet {

    public static String HTTP_URL = "";
    public static int SCREEN_WIDTH = 0; // 屏幕宽度
    public static int SCREEN_HEIGHT = 0; // 屏幕高度
    public static final String APP_SIGN = "Meishai";
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String APP_ID_QQ = "1104295927";// qq第三方登录的appid
    public static final String APP_ID_WX = "wx6e03e62c61953966";// 微信第三方登录的appid
    // 其他变量
    public static final String ACTION_WEIXIN_LOGIN = "weixin login";
    public static final String EXTRA_OPENAPI_AUTH_RESPONSE = "token";
    /**
     * 新浪微博第三方登录
     */
    public static final String APP_KEY = "2045436852";
    public static final String WX_APP_ID="wxb12e168f6cf407c9";
    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p/>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
     * <p/>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
     * <p/>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p/>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    // 每页数据数量
    public static final String PAGE_SIZE = "10";
    public static final String USERID = "userid";
    public static final String C_MEMBER = "member";
    public static final String BUNDLE_ID = "ID";
    // 图片选择 广播ACTION
    public static final String ACTION_NAME = "IMAGE_CHOOSE";
    public static final String ACTION_NAME_2 = "IMAGE_CHOOSE_2";

    // 图片选择 评价截图
    public static final String IMAGE_SCREEN = "IMAGE_SCREEN";

    public static final String ACTION_DEC_NAME = "IMAGE_DEC_CHOOSE";

    public static final String UPDATE_EXPERT = "UPDATE_EXPERT";

    public static final String UPDATE_PRODUCT_DATA = "UPDATE_PRODUCT_DATA";

    public static final String UPDATE_ORDER_UP = "UPDATE_ORDER_UP";
    // 分类选择
    public static final String ACTION_CATE = "CATE_CHOOSE";
    public static final String CHOOSE_CATE = "choose_cate";
    public static final String CHOOSE_DATA = "choose_data";
    // 登录成功广播
    public static final String ACTION_LOGIN_SUCCESS = "LOGIN_SUCCESS";
    // 显示主页广播
    public static final String ACTION_SHOW_HOME = "SHOW_HOME";
    // 使用照相机拍照获取图片
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1001;
    // 裁剪相片通知
    public static final int PHOTO_RESOULT = 1003;

    public static final int PHOTO_RESOULT_2 = 1004;

    // 下拉
    public static final int PULLTOREFRESH_START = 0;
    // 上拉
    public static final int PULLTOREFRESH_END = 0;
}
