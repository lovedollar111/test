package cn.dogplanet.constant;

/**
 * HTTP 相关常量 editor:ztr package_name:cn.dogplanet.constant
 * file_name:HttpUrl.java date:2016-12-6
 */
public class HttpUrl {
    	private static final String HTTP_URL = "http://123.56.154.187:8080/";
//
//    private static final String HTTP_URL = "http://api.dogplanet.cn/";
    /** --------登录、注册相关接口--------- **/
    // 密码登录 或验证码登录
    public static final String EXPERT_LOGIN = HTTP_URL + "v1/b/expert/login";

    //验证手机号是否存在
    public static final String CHECK_EXPERT_PHONE = HTTP_URL + "v1/b/expert/check-expert-phone";
    // 注册时获取区域列表
    public static final String GET_AREA_LIST = HTTP_URL + "v1/b/expert/get-area-list";
    // 注册
    public static final String EXPERT_REG = HTTP_URL + "v1/b/expert/reg";
    //
    public static final String EXPERT_SAVE = HTTP_URL + "v1/b/expert/save-driver-certification-info";
    //判断open_id是否存在
    public static final String CHECK_OPEN_ID = HTTP_URL + "v1/b/expert/check-open-id";


    // 忘记密码
    public static final String EXPERT_FORGOT_PWD = HTTP_URL
            + "v1/b/expert/forgot-pwd";
    //获取图形验证码
    public static final String GET_CAPTCHA = HTTP_URL
            +"v1/b/expert/get-captcha";
    // 修改账号 验证当前账号登录密码
    public static final String VALIDATION_ACCOUNT_BY_PWD = HTTP_URL
            + "v1/b/expert/validation-account-by-pwd";
    // 获取验证码
    public static final String EXPERT_SEND_VERIFY_CODE = HTTP_URL
            + "v1/b/expert/send-verification-code";

    // 找回密码 修改密码 共用
    public static final String UPDATE_PWD = HTTP_URL
            + "v1/b/expert/update-pwd";

    //个人中心 更新图像
    public static final String SAVE_BASE_INFO = HTTP_URL
            + "v1/b/expert/save-expert-base-info";

    // 获取省市区三级联动数据
    public static final String EXPERT_ADDRESS = HTTP_URL
            + "v1/b/expert/get-expert-address";

    // 获取达人有效身份证件类型名称列表
    public static final String GET_CARD_NAME = HTTP_URL
            + "v1/b/expert/get-card-name";


    // 获取银行卡对应银行的信息
    public static final String BANK_CARD = HTTP_URL + "v1/util/get-bank";


    //获取旅行社列表 获取推荐旅行社列表 按照关键字搜索 共用
    public static final String GET_TRAVEL = HTTP_URL
            + "v1/b/expert/get-travel-agency-list";

    //根据关键词搜索获取旅行社列表数据
    public static final String EXPERT_TRAVEL_BY_DESC = HTTP_URL
            + "v1/b/expert/get-travel-agency-by-desc";

    // 保存认证信息
    public static final String SAVE_CERTIFICATION_INFO = HTTP_URL
            + "v1/b/expert/save-expert-certification-info";

    // 根据达人id获取邀请人的公司id
    public static final String GET_INVITE_TRAVEL_AGENCY_ID = HTTP_URL
            + "v1/b/expert/get-invite-travel-agency-id";

    //当用户切换国家时 将达人表的数据更新
    public static final String GET_COUNTRY_LIST = HTTP_URL
            + "v1/b/expert/get-country-list";

    //获取达人职业名称列表
    public static final String GET_EXPERT_JOB_LIST = HTTP_URL
            + "v1/b/expert/get-expert-job-list";

    //获取国家列表
    public static final String SWITCH_EXPERT_AREA = HTTP_URL
            + "v1/b/expert/switch-expert-area";

    //搜索国家列表
    public static final String SEARCH_PLACE = HTTP_URL
            + "v1/b/expert/search-place";


    // 保存达人高级认证信息(司机 商家)
    public static final String SAVE_CATEGORY = HTTP_URL
            + "v1/b/expert/save-expert-category";

    // 获取达人高级认证信息(司机 商家)
    public static final String GET_EXPERT_CATEGORY = HTTP_URL
            + "v1/b/expert/get-expert-category-data";

    // 展示个人资料信息
    public static final String EXPERT_PERSON_DATA = HTTP_URL
            + "v1/b/expert/get-expert-personal-info";

    public static final String SAVE_EXPERT_UUID = HTTP_URL
            + "v1/b/expert/save-expert-uuid";
    // 保存个人资料信息
    public static final String SAVE_EXPERT_DATA = HTTP_URL
            + "v1/b/expert/save-expert-data";
    // 获取一个月的服务日历数据
    public static final String GET_SERIVCE_MONTH = HTTP_URL
            + "v1/b/service-date/get-service-month";

    // 获取日历单日的数据
    public static final String GET_SERIVCE_DATE = HTTP_URL
            + "v1/b/service-date/get-service-date";
    // 保存接单日历数据
    public static final String SAVE_SERIVCE_DATE = HTTP_URL
            + "v1/b/service-date/save-service-date";
    // 拒绝接单、取消拒绝接单
    public static final String REFUSE_ORDER = HTTP_URL
            + "v1/b/service-date/refuse-order";

    // 修改密码
    public static final String MODIFY_PASSOWORD = HTTP_URL
            + "v1/b/expert/modify-password";

    // 获取达人分享图片
    public static final String GET_SHARE_IMAGES = HTTP_URL
            + "v1/b/expert/get-expert-share-images";

    // 删除图片
    public static final String DELETE_EXPERT_IMAGE = HTTP_URL
            + "v1/b/expert/delete-expert-image";

    // 上传图片 (分享照片：10 ， 达人头像：20， 身份证或导游证照片：30， 商城封面：40)
    public static final String UPLOAD_IMG = HTTP_URL + "v1/b/expert/upload-img";

    // 保存导游认证信息
    public static final String SAVE_VERIFY = HTTP_URL
            + "v1/b/expert/save-verify";

    //达人更换旅行社
    public static final String CHANGE_TRAVEL_AGENCY = HTTP_URL
            + "v1/b/expert/change-travel-agency";

    // 查看导游认证信息
    public static final String GET_GUIDE_VERIFY = HTTP_URL
            + "v1/b/expert/get-guide-verify";
    // 保存达人认证信息 type=10
    public static final String SAVE_PERSON_VERIFY = HTTP_URL
            + "/v1/b/expert/save-expert";

    // 查看商户认证信息 type=30
    public static final String GET_SHOP_VERIFY = HTTP_URL
            + "v1/b/expert/get-expert-verify";

    // 保存商户认证信息 type=30
    public static final String SAVE_SHOP_VERIFY = HTTP_URL
            + "v1/b/expert/save-expert";

    // 查看达人认证信息 type=10
    public static final String GET_PERSON_VERIFY = HTTP_URL
            + "v1/b/expert/get-expert-verify";

    // category的值 1:美食 2:住宿 3:娱乐 4:购物 5:出行 6:游玩
    // 获取分类商品、已删除产品(平台产品)
    public static final String GET_EXPERT_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-expert-product";

    // 获取我的商城里面的所有分类下的产品（新）
    public static final String GET_MALL_EXPERT_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-my-mall-expert-product";

    // 获取达人产品
    public static final String GET_NEW_EXPERT_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-new-expert-product";

    // 获取有达人产品的分类
    public static final String GET_NEW_PRODUCT_CATEGORY = HTTP_URL
            + "v1/b/expert-product/get-new-expert-product-category";

    // 获取取景区分类产品
    public static final String GET_SCENIC_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-scenic-product";

    // 修改推荐产品
    public static final String EDIT_RECOMMEND_PRODUCT = HTTP_URL
            + "v1/b/expert-product/edit-recommend-product";

    // 获取推荐商品
    public static final String GET_RECOMM_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-recommend-product";

    // 获取景区列表
    public static final String GET_SCENIC = HTTP_URL
            + "v1/b/expert-product/get-scenic";

    // 获取景区列表
    public static final String ADD_ALL = HTTP_URL
            + "v1/b/expert-product/add-all-scenic-product";

    /** 装修商城 相关接口 **/
    // 根据子产品id和category获取主产品信息
    public static final String GET_PRI_FROM_SUB = HTTP_URL
            + "v1/b/expert-product/get-product-info";

    // 已删除商品加入到店铺
    public static final String RECOVER_PRODUCT = HTTP_URL
            + "v1/b/expert-product/recover-expert-product";

    // 删除商品
    public static final String DELETE_PRODUCT = HTTP_URL
            + "v1/b/expert-product/delete-expert-product";

    // 保存推荐商品
    public static final String SAVE_REMM_PRODUCT = HTTP_URL
            + "v1/b/expert-product/save-recommend-product";

    // 获取主产品信息
    public static final String GET_PRI_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-primary-product";

    // 获取子产品信息
    public static final String GET_SUB_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-sub-product";

    // 获取达人商城里的产品分类
    public static final String GET_PRODUCT_CATEGORY = HTTP_URL
            + "v1/b/expert-product/get-product-category";
    // 景区产品列表加入商城、取消加入
    public static final String JOIN_STORE = HTTP_URL
            + "v1/b/expert-product/join-store";

    // 保存打包产品
    public static final String SAVE_PACKAGE = HTTP_URL
            + "v1/b/package/save-package";

    // 删除打包产品
    public static final String DEL_PACKAGE = HTTP_URL
            + "v1/b/package/delete-package";

    // 获取同一景区下的打包产品数量
    public static final String GET_PACKAGE_NUM = HTTP_URL
            + "v1/b/package/get-package-num";

    // 获取一个打包的产品
    public static final String GET_PACKAGE = HTTP_URL
            + "v1/b/package/get-package";

    // 获取门船票时间
    public static final String GET_MAX_TICKET = HTTP_URL
            + "v1/b/expert-product/check-buy-ticket";

    // 获取可购买二消的数量
    public static final String GET_MAX_NUM = HTTP_URL
            + "v1/b/expert-product/check-buy-product";

    /** 达人首页 相关接口 **/
    // 首页
    public static final String GET_FIRST_PAGE = HTTP_URL
            + "v1/b/expert-product/first-page";
    // 热销商品
    public static final String GET_HOT_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-hot-product";

    // 获取二维码图片
    public static final String GET_QR_CODE = HTTP_URL
            + "v1/b/expert-product/expert-q-r-code";

    /** 我的订单 相关接口 **/
    // 最近订单
    public static final String GET_RECENT_ORDER = HTTP_URL
            + "v1/b/expert-order/get-recent-order";

    // 搜索
    public static final String SEARCH_ORDER = HTTP_URL
            + "v1/b/expert-order/search-order";

    // 获取退款中、已退款订单列表
    public static final String GET_REFUND_ORDER = HTTP_URL
            + "v1/b/expert-order/get-refund-order";

    // 交易成功和已付款的订单
    public static final String GET_SUCCESS_ORDER = HTTP_URL
            + "v1/b/expert-order/get-success-order";

    // 获取上传产品的达人的账户的购买订单
    public static final String GET_EXPERT_ORDER = HTTP_URL
            + "v1/b/expert-order/get-upload-product-expert-success-order";

    // 新增订单

    public static final String GET_NEW_ORDER = HTTP_URL
            + "v1/b/expert-order/get-new-order";

    // 加入购物车
    public static final String ADD_TO_CART = HTTP_URL
            + "v1/b/expert-product/add-to-cart";

    // 创建订单
    public static final String CREATE_ORDER = HTTP_URL
            + "v1/b/expert-product/create-order";

    // 打包产品下单
    public static final String SAVE_PACKAGE_ORDER = HTTP_URL
            + "v1/b/package/save-package-order";
    // 创建订票订单
    public static final String CREATE_TICKET_ORDER = HTTP_URL
            + "v1/b/expert-product/save-ticket-order";
    // 获取订单信息
    public static final String GET_ORDER_DATA = HTTP_URL
            + "v1/b/expert-order/get-order-data";

    // 获取购物车列表
    public static final String GET_CART_PRODUCT = HTTP_URL
            + "v1/b/expert-product/get-cart-product";

    // 删除购物车里的产品
    public static final String DEL_CART_PRODUCT = HTTP_URL
            + "v1/b/expert-product/del-cart-product";

    // 获取购物车里产品的数量 、获取订单列表未查看订单的数量
    public static final String GET_NUM = HTTP_URL
            + "v1/b/expert-product/get-num";

    //商城产品搜索
    public static final String GET_SEARCH_KEYWORDS = HTTP_URL
            + "v1/b/expert-product/get-search-keywords";

    //商城产品搜索  类别 (1 平台产品 2 达人产品 3 全部)
    public static final String GET_SEARCH = HTTP_URL
            + "v1/b/expert-product/search-expert-product";

    // 购物车下单
    public static final String CREATE_CART_ORDER = HTTP_URL
            + "v1/b/expert-product/create-order";

    // 获取订单提醒信息
    public static final String GET_ORDER_REMIND = HTTP_URL
            + "v1/b/expert-order/get-order-remind";

    // 我的评论
    public static final String GET_EXPERT_COMMENT = HTTP_URL
            + "v1/b/expert/get-expert-comment";

    // 获取分享图片
    public static final String GET_EXPERT_SHARE_IMAGES = HTTP_URL
            + "v1/b/expert/get-expert-share-images";

    // 订单详情页
    public static final String GET_ORDER_DETAIL = HTTP_URL
            + "v1/b/expert-order/get-order-detail";

    // 达人商品订单详情页
    public static final String GET_EXPERT_ORDER_DETAIL = HTTP_URL
            + "v1/b/expert-order/get-upload-product-expert-order-detail";

    // 取消主订单
    public static final String CANCEL_PRIMARY_ORDER = HTTP_URL
            + "v1/b/expert-order/cancel-primary-order";

    // 子订单退款
    public static final String CANCEL_CHILD_ORDER = HTTP_URL
            + "v1/b/expert-order/refund-order";

    // 同意／拒绝子订单退款
    public static final String CANCEL_EDIT_ORDER_REFUND = HTTP_URL
            + "v1/b/expert-order/expert-edit-order-refund";

    // 验证消费码
    public static final String CHECK_ORDER_CODE = HTTP_URL
            + "v1/b/expert-order/update-order-status";

    // 达人确认／拒绝退款
    public static final String EXPERT_CHANGE_ORDER_STATUS = HTTP_URL
            + "v1/b/expert-order/change-expert-order-status";

    // 申请退款
    public static final String REFUND_PRIMARY_ORDER = HTTP_URL
            + "v1/b/expert-order/refund-primary-order";

    // 获取可退款的门票数量
    public static final String GET_REFUND_TICKET = HTTP_URL
            + "v1/b/expert-order/get-total-tickets-number";

    // 改变订单状态
    public static final String CHANGE_ORDER_STATUS = HTTP_URL
            + "v1/b/expert-order/change-order-status";

    // 订单提醒
    public static final String SAVE_ORDER_REMIND = HTTP_URL
            + "v1/b/expert-order/save-order-remind";

    //通过子产品ID获取主产品的退改规则
    public static final String GET_PRODUCT_RETURNS = HTTP_URL
            + "v1/b/expert-product/get-product-returns";
    /** 达人商品 **/
    // 获取商品分类
    public static final String GET_CATEGORY = HTTP_URL
            + "v1/b/expert-product/get-category";

    // 保存达人产品
    public static final String SAVE_PRODUCT = HTTP_URL
            + "v1/b/expert/save-expert-product";
    // 获取达人产品信息
    public static final String GET_PRODUCT = HTTP_URL
            + "v1/b/expert/get-product-by-pro-id";
    // 获取达人产品列表
    public static final String GET_PRODUCT_LIST = HTTP_URL
            + "v1/b/expert/get-product-list";
    // 上下架达人产品
    public static final String CHANGE_PRODUCT_STATUS = HTTP_URL
            + "v1/b/expert/change-product-status";

    /** 虚拟账户 **/
    // 根据卡号获取银行信息
    public static final String GET_BANK_NUMBER = HTTP_URL
            + "v1/b/expert/get-bank-info-by-card-number";

    // 保存达人银行卡信息
    public static final String SAVE_EXPERT_BANK_NUMBER = HTTP_URL
            + "v1/b/expert/save-expert-bank-card";

    // 获取达人银行卡列表
    public static final String GET_EXPERT_BANK_CARD = HTTP_URL
            + "v1/b/expert/get-expert-bank-card";

    // 提现列表
    public static final String WITHDRAW_LIST = HTTP_URL
            + "v1/b/expert/withdraw-list";

    // 获取达人余额和账户总收入
    public static final String GET_EXPERT_PRICE = HTTP_URL
            + "v1/b/expert/get-expert-price";

    // 获取达人账户收入/支出数据
    public static final String GET_EXPERT_FINANCE = HTTP_URL
            + "v1/b/expert/get-expert-finance";

    // 删除达人绑定的银行卡
    public static final String DELETE_EXPERT_BANK_CARD = HTTP_URL
            + "v1/b/expert/delete-expert-bank-card";

    // 申请提现
    public static final String EXPERT_WITHDRAW = HTTP_URL
            + "v1/b/expert/expert-withdraw";

    // 获取收入/支出详情信息
    public static final String GET_FINANCE_DETAIL = HTTP_URL
            + "v1/b/expert/get-finance-detail";

    // 核对手机验证码是否正确 不需要达人信息
    public static final String CHECK_VERIFICATION_CODE = HTTP_URL
            + "v1/b/expert/check-verification-code";

    // 修改账号时检查达人的验证码是否正确  需要达人信息
    public static final String CHECK_EXPERT_CODE = HTTP_URL
            + "v1/b/expert/check-expert-code";

    //修改账号 更换新手机号
    public static final String UPDATE_ACCOUNT = HTTP_URL
            + "v1/b/expert/update-account";

    // 设置或修改支付密码
    public static final String EDIT_PAYMENT_PASSWORD = HTTP_URL
            + "v1/b/expert/edit-payment-password";

    // 余额支付订单
    public static final String PAY_ORDER_BY_BALANCE = HTTP_URL
            + "v1/b/expert/paying-order-by-balance";

    // 微信支付
    public static final String PAY_ORDER_BY_WX = HTTP_URL + "v1/b/wx-pay/sign";
    // 支付宝支付
    public static final String PAY_ORDER_BY_ZFB = HTTP_URL
            + "v1/b/ali-pay/sign";

    //推荐下载 显示邀请码和成功邀请人数
    public static final String RECOMMEND_DOWNLOAD = HTTP_URL
            + "v1/b/expert/recommend-download";

}
