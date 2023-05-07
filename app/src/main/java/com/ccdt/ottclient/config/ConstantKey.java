package com.ccdt.ottclient.config;

/**
 * map键值常量类
 */
public class ConstantKey {

    /**
     * 侧边栏：
     * {lmName=首页, lmId=IndexPage}, {lmName=时事, lmId=CurAffairs}, {lmName=北纬23, lmId=North23},
     * {lmName=品茶普洱, lmId=PuerTea}, {lmName=拍客, lmId=Pack}, {lmName=直播, lmId=LiveYN}, {lmName=商城, lmId=Shop}
     * {lmName=推荐, lmId=Recommand}
     * <p/>
     * 拍客：
     * {lmName=拍客精选, lmId=BestPacks}, {lmName=拍客任务, lmId=PackTasks}
     * <p/>
     * 北纬23和品茶普洱：
     * {lmName=茶源直通车, lmId=TeaExpress}, {lmName=茶人知道, lmId=TeaCommons}
     * {lmName=边地人文, lmId=PuerLife}, {lmName=美丽云南, lmId=BeautyYN}, {lmName=茶马往事, lmId=OldAffairs}
     * <p/>
     * 时事关注：
     * {lmName=焦点, lmId=Focus}, {lmName=市场, lmId=Market}, {lmName=文化, lmId=Culture}, {lmName=生活, lmId=Life}
     * 直播：
     * {lmName=在线直播, lmId=LiveStreams}, {lmName=人气视频, lmId=HotVideos}
     */
    public static final String ROAD_SLIDING = "roadSliging";//侧边菜单
    public static final String PARAM_FOLDER_PARENT = "parent";
    public static final String PARAM_FOLDER = "folder";

    public static final String ROAD_LMID_INDEXPAGE = "IndexPage";//首页
    public static final String ROAD_LMID_MAKE = "Pack";//拍客
    public static final String ROAD_LMID_MAKE_SELECT = "BestPacks";//拍客精选
    public static final String ROAD_LMID_MAKE_TASK = "PackTasks";//拍客任务


    public static final String ROAD_LMID_FOCUS = "CurAffairs";//时事
    public static final String ROAD_LMID_FOCUS_OF = "Focus";//焦点
    public static final String ROAD_LMID_FOCUS_CULTURE = "Culture";//文化
    public static final String ROAD_LMID_FOCUS_MARKET = "Market";//市场
    public static final String ROAD_LMID_FOCUS_LIFE = "Life";//生活

    public static final String ROAD_LMID_LIVE = "LiveYN";//直播
    public static final String ROAD_LMID_LIVE_LINE = "LiveStreams";//在线直播
    public static final String ROAD_LMID_LIVE_HOT = "HotVideos";//人气视频

    public static final String ROAD_LMID_NORTH = "North23";//北纬23
    public static final String ROAD_LMID_NORTH_HUMAN = "PuerLife";//边地人文
    public static final String ROAD_LMID_NORTH_YUNNAN = "BeautyYN";//美丽云南
    public static final String ROAD_LMID_NORTH_OLD = "OldAffairs";//茶马往事

    public static final String ROAD_LMID_TEA = "PuerTea";//品茶普洱
    public static final String ROAD_LMID_TEA_EXPRESS = "TeaExpress";//茶人直通车
    public static final String ROAD_LMID_TEA_KNOWN = "TeaCommons";//茶人知道

    public static final String ROAD_LMID_TEA_SHOP = "Shop";///商城
    public static final String ROAD_LMID_RECOMMAND = "Recommand";//推荐

    public static final String ROAD_LMID_HOME_MAKING = "HomePack";//首页拍客使用
    public static final String ROAD_LMID_STORE = "roadStore";//商城(1.0)


    //    public static final String SITE_ID = "qhsj";
    public static final String SITE_LEVEL_ONE = "0";

    /**
     * mzType
     */
    public static final String ROAD_TYPE_SEARCH_EPISODE = "0";//剧集
    public static final String ROAD_TYPE_SEARCH_MOVIE = "1";//电影
    public static final String ROAD_TYPE_SEARCH_CHANNEL = "2";// 频道
    public static final String ROAD_TYPE_SEARCH_NEWS = "3";//新闻
    public static final String ROAD_TYPE_SEARCH_MAKING = "4";// 拍客
    public static final String ROAD_TYPE_SEARCH_IMAGE = "5";//图片
    public static final String ROAD_TYPE_SEARCH_POSTER = "6";//海报
    public static final String ROAD_TYPE_SEARCH_TEXT = "7";//文本
    public static final String ROAD_TYPE_SEARCH_BUSINESS = "8";//增值业务
    public static final String ROAD_TYPE_SEARCH_MAKING_TASK = "9";//拍客任务

    /**
     * status，视频上传状态
     */
    public static final String ROAD_TYPE_MAKING_UPLOADING_STATUS = "6";//已上线

    public static final String ROAD_TYPE_LMNAME = "lmName";//站点编码
    public static final String ROAD_TYPE_SITEID = "siteId";//站点编码
    public static final String ROAD_TYPE_KWORDS = "kwords";//搜索关键词
    public static final String ROAD_TYPE_SEARCHTYPE = "searchType";//搜索类型
    public static final String ROAD_TYPE_PAGESIZE = "pageSize";//每页显示信息个数
    public static final String ROAD_TYPE_PAGEYEMA = "pageYema";//当前显示第几页
    public static final String ROAD_TYPE_ID = "id";//标识资产 ID
    public static final String ROAD_TYPE_SHOWFIELDS = "showFields";//需要的字段
    public static final String ROAD_TYPE_MZID = "mzId";//每种资产 ID
    public static final String ROAD_TYPE_LMID = "lmId";//栏目 ID
    public static final String ROAD_TYPE_USERID = "userId";//用户 ID
    public static final String ROAD_TYPE_COMMID = "commId";//评论 ID

    //搜索业务类型 searchType 所对应的值
    public static final String ROAD_TYPE_COMMENTS = "comments";//评论搜索
    public static final String ROAD_TYPE_COLLECTS = "collects";///收藏搜索
    public static final String ROAD_TYPE_PRAISE = "praise";//点赞搜索
    public static final String ROAD_TYPE_VODS = "vods";//vod 搜索
    public static final String ROAD_TYPE_ZHIBO = "zhibo";//直播
    public static final String ROAD_TYPE_EPGS = "epgs";//直播 EPG
    public static final String ROAD_TYPE_PAIKE = "paike";//拍客搜索
    public static final String ROAD_TYPE_PKJOB = "pkJob";//拍客任务搜索
    public static final String ROAD_TYPE_NEWS = "news";//新闻搜索
    public static final String ROAD_TYPE_GOODS = "goods";//商品搜索
    public static final String ROAD_TYPE_CONTENT = "content";//评论内容
    public static final String ROAD_TYPE_VERSION = "version";//版本号
    public static final String ROAD_TYPE_USERNAME = "userName";//用户名
    public static final String ROAD_TYPE_PASSWD = "passwd";//密码
    public static final String ROAD_TYPE_NICKNAME = "nickName";//昵称
    public static final String ROAD_TYPE_MOB = "mob";//电话号码
    public static final String ROAD_TYPE_EMAIL = "email";//邮件
    public static final String ROAD_TYPE_SITE = "site";//
    public static final String ROAD_TYPE_USERICON = "userIcon";//用户头像
    public static final String ROAD_TYPE_PLATFORMTYPE = "platformType";//第三方登录平台


    //列表信息
    public static final String ROAD_TYPE_MZNAME = "mzName";//新闻标题
    public static final String ROAD_TYPE_MZDESC = "mzDesc";//新闻描述
    public static final String ROAD_TYPE_SOURCE = "source";//来源
    public static final String ROAD_TYPE_EDITPERSON = "editPerson";//作者
    public static final String ROAD_TYPE_UPDATETIME = "updateTime";//时间
    public static final String ROAD_TYPE_SHORTTITLE = "shortTitle";//第二标题
    public static final String ROAD_TYPE_TITLEIMG = "titleImg";//标题图片
    public static final String ROAD_TYPE_EDITORCHARGE = "editorCharge";//内容


    //拍客精选和拍客任务列表信息
    public static final String ROAD_TYPE_JOBDESC = "jobDesc";//描述
    public static final String ROAD_TYPE_JOBNAME = "jobName";//标题
    public static final String ROAD_TYPE_THUMBIMG = "thumbImg";//图片
    public static final String ROAD_TYPE_JOBID = "jobId";//任务 ID
    public static final String ROAD_TYPE_MEMBERID = "memberId";//会员 ID
    public static final String ROAD_TYPE_MEMBERNAME = "memberName";//用户名
    public static final String ROAD_TYPE_MEMBERICON = "memberIcon";//用户头像
    public static final String ROAD_TYPE_BFURL = "bfUrl";//视频地址
    public static final String ROAD_TYPE_SHOWURL = "showUrl";//大图
    public static final String ROAD_TYPE_POSTERURL = "posterUrl";//快速入口人气视频海报
    public static final String ROAD_TYPE_PRAISEUIDS = "praiseUids";//用户自己是否点赞
    public static final String ROAD_TYPE_COMMENTNUM = "commentNum";//评论数
    public static final String ROAD_TYPE_PRAISENUM = "praiseNum";//点赞数
    public static final String ROAD_TYPE_IFPRAISE = "ifPraise";//当前用户是否点赞，1点赞0未点赞
    public static final String ROAD_TYPE_IFCOLLECT = "ifCollect";//当前用户是收藏，1收藏0未收藏
    public static final String ROAD_TYPE_COLLECTNUM = "collectNum";//大于0代表用户收藏了，等于0代表没有收藏

    public static final String ROAD_TYPE_PACK_SHOWFILED = "jobId,updateTime,bfUrl,mzDesc,mzName,showUrl,collectNum,collectUids,commentNum,commentUids,praiseUids,praiseNum,source,memberId,memberName,memberIcon";
    public static final String ROAD_TYPE_PACK_DETAIL = "updateTime,bfUrl,mzDesc,mzName,showUrl,collectNum,collectUids,commentNum,commentUids,praiseUids,praiseNum,source,memberName";
    public static final String ROAD_TYPE_NEWS_COLLECTION_SHOWFILED = "id,mzId,createTime,mzCreateTime,mzName,haibao,mzAuthor,mzSource,detailHref";

    //在线直播
    public static final String ROAD_TYPE_LOGOURL = "logoUrl";//logo图片

    //收藏
    public static final String ROAD_TYPE_MZCREATETIME = "mzCreateTime";//创建时间
    public static final String ROAD_TYPE_CREATETIME = "createTime";//创建时间
    public static final String ROAD_TYPE_HAIBAO = "haibao";//海报地址，一般针对电影电视剧
    public static final String ROAD_TYPE_MZTYPE = "mzType";//资产类型
    public static final String ROAD_TYPE_VALIDFLAG = "validFlag";//是否有效
    public static final String ROAD_TYPE_MZSOURCE = "mzSource";//
    public static final String ROAD_TYPE_MZAUTHOR = "mzAuthor";//

    //评论
    public static final String ROAD_TYPE_ISANONYMOUS = "isAnonymous";//是否匿名评论
    public static final String ROAD_TYPE_ISREPLY = "isReply";//是否回复评论
    public static final String ROAD_TYPE_MZHAIBAO = "mzHaibao";//资产海报
    public static final String ROAD_TYPE_PID = "pid";//被回复评论的id
    public static final String ROAD_TYPE_TOMEMBERID = "toMemberId";//只针对拍客，拍客宿主id
    public static final String ROAD_TYPE_TOMEMBERNAME = "toMemberName";//只针对拍客，拍客宿主名字
    public static final String ROAD_TYPE_STATUS = "status";//

    //新闻
    public static final String ROAD_TYPE_AUTHOR = "author";//作者
    public static final String ROAD_TYPE_CLICKNUM = "clickNum";//点击量
    public static final String ROAD_TYPE_COLLECTUIDS = "collectUids";//收藏会员数组
    public static final String ROAD_TYPE_COMMENTUIDS = "commentUids";//评论会员数组
    public static final String ROAD_TYPE_DETAILHREF = "detailHref";//资产详情地址
    public static final String ROAD_TYPE_HTMLINDEXNUM = "htmlIndexNum";//
    public static final String ROAD_TYPE_INDEXNUM = "indexNum";//
    public static final String ROAD_TYPE_ISCOMMENT = "isComment";//是否评论
    public static final String ROAD_TYPE_ISDEL = "isDel";//是否删除
    public static final String ROAD_TYPE_ISHOT = "isHot";//是否热门
    public static final String ROAD_TYPE_ISSCORE = "isScore";//是否被评分
    public static final String ROAD_TYPE_ISSIGN = "isSign";//是否签收
    public static final String ROAD_TYPE_ISTOP = "isTop";//是否置顶
    public static final String ROAD_TYPE_NEWSTYPE = "newsType";//新闻类型，图片新闻，普通新闻，视频新闻
    public static final String ROAD_TYPE_OPENDTIME = "openDtime";//
    public static final String ROAD_TYPE_OPENTYPE = "openType";//
    public static final String ROAD_TYPE_OPENTIMETYPE = "opentimeType";//
    public static final String ROAD_TYPE_OUTLINK = "outLink";//该新闻来源链接
    public static final String ROAD_TYPE_MZORDER = "mzOrder";//资产排序
    public static final String ROAD_TYPE_SOURCEID = "sourceId";//资产来源id
    public static final String ROAD_TYPE_STATERESULT = "stateResult";//
    public static final String ROAD_TYPE_TAGS = "tags";//新闻标签
    public static final String ROAD_TYPE_TITLEBOLD = "titleBold";//标题粗体
    public static final String ROAD_TYPE_TITLECOLOR = "titleColor";//
    public static final String ROAD_TYPE_TOPENDTIME = "topEndTime";//置顶结束时间
    public static final String ROAD_TYPE_VIDEO = "video";//视频地址
    public static final String ROAD_TYPE_VIEWTYPE = "viewType";//展示类型

    public static final String ROAD_TYPE_NEWSIMGS = "newsImgs";//组图
    public static final String ROAD_TYPE_IMGDESC = "imgDesc";//组图描述
    public static final String ROAD_TYPE_IMGTITLE = "imgTitle";//
    public static final String ROAD_TYPE_IMGURL = "imgUrl";//
    public static final String ROAD_TYPE_ORDERNUM = "orderNum";//组图排序

    //拍客及拍客任务
    public static final String ROAD_TYPE_VIEWNUM = "viewNum";//观看次数
    public static final String ROAD_TYPE_BANNERIMG = "bannerImg";//banner图片
    public static final String ROAD_TYPE_CREATEUSERID = "createUserId";//拍客上传者
    public static final String ROAD_TYPE_ISTASK = "isTask";//是否默认任务
    public static final String ROAD_TYPE_UPDATEUSERID = "updateUserId";//


    //点赞
    public static final String ROAD_TYPE_ZAN = "zan";//

    //点播 电影和电视剧
    public static final String ROAD_TYPE_ACTORS = "actors";//演员
    public static final String ROAD_TYPE_CATEGORY = "category";//电影电视剧的分类：科幻，动作（手机端使用）
    public static final String ROAD_TYPE_DIRECTOR = "director";//导演
    public static final String ROAD_TYPE_FACTORS = "factors";//
    public static final String ROAD_TYPE_FCATEGORY = "fcategory";//电影电视剧的分类：科幻，动作（后台查询使用）
    public static final String ROAD_TYPE_FDIRECTOR = "fdirector";//导演
    public static final String ROAD_TYPE_EQUIPTYPE = "equipType";//设备(手机，盒子)类型
    public static final String ROAD_TYPE_SHOWTYPE = "showType";//展示类型（横竖）
    public static final String ROAD_TYPE_JISHU = "jishu";//总集数（40集）
    public static final String ROAD_TYPE_REALJISHU = "realJishu";//实际集数
    public static final String ROAD_TYPE_JUJI = "juji";//里面的每个电影电视剧的信心
    public static final String ROAD_TYPE_CHAPTER = "chapter";//第几集
    public static final String ROAD_TYPE_JUJIID = "jujiId";//每一集的id
    public static final String ROAD_TYPE_JUJINAME = "jujiName";//每一集名称
    public static final String ROAD_TYPE_PUBTIME = "pubTime";//发行时间
    public static final String ROAD_TYPE_RUNTIME = "runTime";//时长
    public static final String ROAD_TYPE_VISITADDRESS = "visitAddress";//播放地址，分高清，标清
    public static final String ROAD_TYPE_FORMAT = "format";//码率格式
    public static final String ROAD_TYPE_MALV = "malv";//码率值
    public static final String ROAD_TYPE_KEYWORDS = "keywords";//关键词
    public static final String ROAD_TYPE_LANGUAGE = "language";//语言
    public static final String ROAD_TYPE_ORIGIN = "origin";//产地
    public static final String ROAD_TYPE_RATE = "rate";//
    public static final String ROAD_TYPE_SCORE = "score";//评分
    public static final String ROAD_TYPE_SECONDLANG = "secondLang";//第二语言
    public static final String ROAD_TYPE_STAR = "star";//明星
    public static final String ROAD_TYPE_SUBTITLE = "subTitle";//副标题
    public static final String ROAD_TYPE_VODTYPE = "vodType";//是否电影，电视剧，综艺节目
    public static final String ROAD_TYPE_YEAR = "year";//哪年拍的

    //直播(频道和epg)
    public static final String ROAD_TYPE_CHANNEL = "channel";//频道名
    public static final String ROAD_TYPE_CHANNELID = "channelId";//官方的频道id
    public static final String ROAD_TYPE_IFBACKL = "ifBack";//是否回看
    public static final String ROAD_TYPE_IFLIVE = "ifLive";//是否直播
    public static final String ROAD_TYPE_IFMOVE = "ifMove";//是否时移
    public static final String ROAD_TYPE_ISMOVE = "isMove";
    public static final String ROAD_TYPE_MOVETIME = "moveTime";//时移时间
    public static final String ROAD_TYPE_RECORDTIME = "recordTime";//记录时间
    public static final String ROAD_TYPE_SOURCETYPE = "sourceType";//直播源头来源类型
    public static final String ROAD_TYPE_ZBSOURCE = "zbSource";//直播源列表，高清，标清：
    //zbSource：
    public static final String ROAD_TYPE_CODE = "code";//直播源的编码
    public static final String ROAD_TYPE_LIVEURL = "liveUrl";//直播地址
    public static final String ROAD_TYPE_MOVEURL = "moveUrl";//时移地址
    public static final String ROAD_TYPE_NAME = "name";//名称
    public static final String ROAD_TYPE_OUTPUTTYPE = "outputType";//输出类型
    public static final String ROAD_TYPE_PATH = "path";//路径
    public static final String ROAD_TYPE_TYPE = "type";//直播源类型

    public static final String ROAD_TYPE_USERLOGIN = "userLogin";//用户登录
    public static final String ROAD_TYPE_FINDUSERNAME = "findUserName";//查找用户名


    public static final String ROAD_TYPE_EPG = "epg";//
    public static final String ROAD_TYPE_BACKADDRESS = "backAddress";//回看地址
    public static final String ROAD_TYPE_SOURCECODE = "sourceCode";//对应直播源列表的编码---->OAD_TYPE_CODE= "code";//直播源的编码
    public static final String ROAD_TYPE_STARTTIME = "startTime";//节目单开始时间
    public static final String ROAD_TYPE_ENDTIME = "endTime";//节目单结束时间
    public static final String ROAD_TYPE_EPGNAME = "epgName";//名称
    public static final String ROAD_TYPE_EPGTIME = "epgTime";//节目单的时间段
    public static final String ROAD_TYPE_LZSTATE = "lzState";//录制成功与否
    public static final String ROAD_TYPE_PROGRAMTIME = "programTime";//
    public static final String ROAD_TYPE_VODSTATE = "vodState";//
    public static final String ROAD_TYPE_ZBMZID = "zbMzId";//所属频道id,ott系统频道id，非官方id
    public static final String ROAD_TYPE_DATE = "date";//epg日期

    public static final String ROAD_TYPE_IFSELECTALL = "ifSelectAll";

    //升级数据
    public static final String UPGRADE_APKURL = "appUrl";//app下载地址
    public static final String UPGRADE_REMARK = "remark";//软件说明
    public static final String UPGRADE_PAGEURL = "pageUrl";//软件下载页面
    public static final String UPGRADE_TERMINALTYPE = "terminalType";//手机类型
    public static final String UPGRADE_VERSIONCODE = "versionCode";//手机版本
    public static final String UPGRADE_VERSIONNAME = "versionName";//版本名称
    public static final String UPGRADE_SITECODE = "siteCode";//站点编码
    public static final String UPGRADE_APPPACKAGE = "appPackage";//app包名
    public static final String UPGRADE_APPNAME = "appName";//app名称
    public static final String UPGRADE_APPICON = "appIcon";//app图标
    public static final String UPGRADE_SHAREINFO = "shareInfo";//分享语

    public static final String ROAD_TYPE_RECOMMENDS = "recommends";//点播推荐
    public static final String ROAD_TYPE_ISRECOMMEND = "isRecommend";//点播是否需要推荐
    public static final String ROAD_TYPE_RECOMMENDNUM = "recommendNum";//点播推荐数
    public static final String ROAD_TYPE_RECOMMEND_SHOWFIELDS = "recommendShowFields";//点播推荐需要字段

    public static final String ROAD_TYPE_IMG = "img";//首页推荐新闻海报
    public static final String ROAD_TYPE_TERMINAL = "terminal";//搜索要传设备类型
    public static final String ROAD_TYPE_LMICON = "lmIcon";//栏目海报及背景
    public static final String ROAD_TYPE_SN = "sn";//设备SN
    public static final String ROAD_TYPE_TICKET = "ticket";//认证鉴权返回的token
    public static final String ROAD_TYPE_ASSETID = "assetId";//媒资ID
    public static final String ROAD_TYPE_PRODUCTID = "productId";//产品ID
    public static final String ROAD_MY_PAGE_NUMBER = "pageNumber";//产品ID

    public static final String PRODUCT_NAME = "productName";//产品名
    public static final String PRODUCT_SUBSCRIBE = "productSubscribe";//产品描述
    public static final String PRODUCT_FLAG = "productFlag";//包月产品标志  0已退订  1自动续订
    public static final String PRODUCT_ID = "productId";//包月产品ID
    public static final String PRODUCT_PRICE = "productPrice";//产品价格
    public static final String PRODUCT_TIME = "productTime";//产品订购时间
    public static final String PRODUCT_POSTER_URL = "posterUrl";
    public static final String PRODUCT_EQUIP_TYPE = "equipType";
    public static final String PRODUCT_POSTER_TYPE = "posterType";
    public static final String PRODUCT_ASSET_ID = "assetId";
    public static final String PRODUCT_ASSET_NAME = "assetName";

    /** 订单状态。1-为使用中；0为已使用。也可不设置该参数，查询所有状态。 */
    public static final String PRODUCT_MY_STATE = "state";
    /** 海报类型。0-横版；1-竖版。也可不设置，获取所有海报 */
    public static final String PRODUCT_MY_POSTER_TYPE = "posterType";
    /** 设备类型。base-机顶盒；terminal-手机。也可不设置，获取所有海报 */
    public static final String PRODUCT_MY_TERMINAL_TYPE = "terminalType";

    public static final String NEED_ALL_RESULT = "needAllResult";//需要所有结果


}
