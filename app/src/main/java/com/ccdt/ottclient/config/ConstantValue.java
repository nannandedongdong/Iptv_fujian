package com.ccdt.ottclient.config;

/**
 * 静态值
 */
public class ConstantValue {

//    public static String COMM_SERVER_URL = "183.224.178.28";
//    public static int COMM_SERVER_PORTAL = 15415;
//    public static String COMM_SERVER_SEARCH_URL = "183.224.178.28";
//    public static int COMM_SERVER_SEARCH_PORTAL = 15413;

    /** 站点ID */
    public static final String SITE_ID = "ott";
    /** 顶级栏目ID 凭此获取子栏目 */
    public static final String SITE_LEVEL_ONE = "0";
    
    //----------以下为栏目id
    /** 热门推荐 */
    public static final String LMID_RMTJ = "qhystj";
    /** 直播频道 回看也用*/
    public static final String LMID_ZB = "qhyszb";
    /** 影视分类 */
    public static final String LMID_YSFL = "qhysysfl";
    /** 广告位 */
    public static final String LMID_GG = "qhysgg";
    /** 专题 */
    public static final String LMID_ZT = "qhyszt";

    /** 青海影视-中国电视院线 */
    public static final String LMID_ZGDSYX="ZGDSYX";
    /** 青海影视-中国电视院线-最热HOT */
    public static final String LMID_ZRHOT="ZRHOT";
    /** 青海影视-中国电视院线-今日排行 */
    public static final String LMID_JRPH="JRPH";
    /** 青海影视-中国电视院线-百事通 */
    public static final String LMID_BST="BST";
    /** 青海影视-中国电视院线-华数高清 */
    public static final String LMID_HSGQ="HSGQ";
    /** 青海影视-中国电视院线-图片广告 */
    public static final String LMID_TPGG="TPGG";
    /** 青海影视-藏语频道-电影 */
    public static final String LMID_ZYDY="ZYDY";
    /** 青海影视-藏语频道-动漫 */
    public static final String LMID_ZYDM="ZYDM";
    /** 青海影视-藏语频道-科教片 */
    public static final String LMID_DYKJP="DYKJP";
    /** 青海影视-藏语频道-电视剧 */
    public static final String LMID_ZYDSJ="ZYDSJ";

    /**
     * Vod點播
     */
    public static final String VOD_VODJCDB = "VODJCDB";
    public static final  String VOD_ZXSX = "ZXSX";

    public static final  String VOD_RBYP = "RBYP";

    public static final String VOD_NDJ = "NDJ";//内地剧

    public static final String VOD_GTJ = "GTJ";
    public static final String VOD_RHJ ="RHJ";
    public static final String VOD_HWJ = "HWJ";
    public static final String VOD_DMJ = "DMJ";
    
    /** 默认媒资条数 */
    public static final String PAGE_SIZE = "10";
    
    /** 启动应用详情页 */
    public static final String EXTRA_APK_DETIAL_URL = "apkDetialURL";
    
    
    /** 新闻需要字段 */
    public static final String SHOWFIELDS_NEWS = "mzName,shortTitle,siteLm,source,editorCharge,editPerson,mzDesc,tags,outLink,titleImg,updateTime,clickNum,video,newsImgs,commentUids,collectUids,praiseUids,content";
    
    /** 设备类型 终端 */
    public static final String EQUIPTYPE_TERMINAL = "terminal";
    /** 设备类型 基础 */
    public static final String EQUIPTYPE_BASE = "base";
    
    /** 海报展示类型 竖版 */
    public static final String SHOWTYPE_VERTICAL = "1";
    /** 海报展示类型 横版 */
    public static final String SHOWTYPE_HORIZONTAL = "0";
    /** 海报展示类型 推荐 */
    public static final String SHOWTYPE_RECOMMEND = "r";
    /** 海报展示类型 所有 */
    public static final String SHOWTYPE_ALL = "all";

    /** 首页所需要的字段 */
    public static final String SHOWFIELDS_RECOMMEND = "language,origin,year,showUrl,id,mzName,haibao,ifCollect";

    /**
     *
     */
    public static final String SHOWFIELDS_VOD_DETAIL = "collectUids,language,origin,year,mzDesc,runTime,director,mzName,star,vodType,category,haibao,actors,juji,jishu,realJishu,updateTime,ifCollect";

    /** 广告所需要的字段 */
    public static final String SHOWFIELDS_GG = "showUrl,id,mzName,haibao,juji";

    /** 盒子设备类型 */
    public static final String TERMINAL_STB = "stb";
    /** 手机设备类型 */
    public static final String TERMINAL_PHONE = "phone";
    
    /** 包月产品自动续订 */
    public static final String PRODUCT_SUBSCRIBE = "1";
    /** 包月产品已退订 */
    public static final String PRODUCT_UNSUBSCRIBE = "0";
    
}
