package com.ccdt.ottclient.config;

public final class Config {
    // http://ip:port
    public static String HOST;
    public static String SITE_ID;
    public static String APP_SHOP_URL;
    public static String OttSearchService_IP;
    public static int OttSearchService_PORT;
    public static String OttCommActionService_IP;
    public static int OttCommActionService_PORT;

    /**
     * 认证地址 /auth/terauth/authTerminal
     */
    public static final String PATH_AUTHTERMINAL = "/auth/terauth/authTerminal";

    /**
     * VOD鉴权地址 /auth/product/authorization
     */
    public static final String PATH_AUTHORIZATION = "/auth/product/authorization";

    /**
     * 购买产品地址 /auth/product/productOrder
     */
    public static final String PATH_PRODUCTORDER = "/auth/product/productOrder";

    /**
     * 我的包月产品地址 /auth/userinfo/getMyMonthOrder
     */
    public static final String PATH_GETMYMONTHORDER = "/auth/userinfo/getMyMonthOrder";

    /**
     * 获取包月产品的消费记录 http://ip:port/auth/userinfo/getMyMonthOrderDetail?ticket=xxxx&productId=xxxx
     */
    public static final String PATH_GETMYMONTHORDERDETAIL = "/auth/userinfo/getMyMonthOrderDetail";

    /**
     * 我的按次产品地址 /auth/userinfo/getMyNumOrder
     */
    public static final String PATH_GETMYNUMORDER = "/auth/userinfo/getMyNumOrder";

    /**
     * 包月产品续订，以便下个月自动续订 /auth/product/subscribe
     */
    public static final String PATH_SUBSCRIBE = "/auth/product/subscribe";

    /**
     * 退订包月产品，以便下个月不再自动续订 /auth/product/unsubscribe
     */
    public static final String PATH_UNSUBSCRIBE = "/auth/product/unsubscribe";

    /**
     * 获取我尚未订购的包月产品 /userinfo/getOtherProducts
     */
    public static final String PATH_GETOTHERPRODUCTS = "/auth/userinfo/getOtherProducts";

    /**
     * 获取信息
     */
    public static final String PATH_GETACCOUNT = "/auth/userinfo/myInfo";


    /**
     * 订购产品
     */
    public static final String PATH_ORDERPRODUCT= "/auth/product/productOrder";

    /**
     * 支付产品
     */
    public static final String PATH_PAYPRODUCT= "/auth/product/productPay";


}
