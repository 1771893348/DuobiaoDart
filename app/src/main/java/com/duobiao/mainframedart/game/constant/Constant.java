package com.duobiao.mainframedart.game.constant;


/**
 * @author lpmo
 */
public class Constant {


    public static final String GAMETYPE301 = "301";
    public static final String GAMETYPE501 = "501";
    public static final String GAMETYPE701 = "701";
    public static final String GAMETYPEMICKEY = "标准米老鼠";
    public static final String GAMETYPRANDOMEMICKEY = "随机米老鼠";
    public static final String GAMETYPEHIGHSCROE = "五轮高分赛";
    public static final String GAMETYPEHIGHSCROE8 = "八轮高分赛";
    public static final String GAMETYPEMIX = "组合赛";

    public static final int GAMEROUNDSMICKEY = 15;

    public static final String PLAYERNUM2V2 = "2V2";

    public static final int PLAYERSCORE301 = 301;
    public static final int PLAYERSCORE501 = 501;
    public static final int PLAYERSCORE701 = 701;
    public static final int PLAYERSCOREMICKEY = 0;
    public static final int PLAYERSCOREHIGHSCORE = 0;


    public static final String SINGLE = "";
    public static final String DOUBLE = "x2";
    public static final String TRIPLE = "x3";
    public static final String D_BULL = "D-BULL";
    public static final String S_BULL = "S-BULL";
    public static final String HIGH_SCORE = "20b";
    public static final String MISS = "MISS";

    public static final String NUMBER1 = "第一名";
    public static final String NUMBER2 = "第二名";
    public static final String NUMBER3 = "第三名";
    public static final String NUMBER4 = "第四名";

    public static final String[] PlayerName = new String[] {"忍者猫","加菲猫","超人猫","招财猫"};
//    public static final Map<String, GameStatsBean> gameStats = new HashMap<>();

    public static final int DARTS_NET_DATA = 99;
    public static final int DARTS_DATA = 0;//飞镖得分
    public static final int GAMESETTING_PW = 1;
    public static final int BACKDARTS_PW = 2;
    public static final int RULES_PW = 3;
    public static final int GAMEOVER_PW = 4;
    public static final int GAMESETTINGDISMISS_PW = 5;
    public static final int DARTS_REDKEY = 6;  //红色按钮
    public static final int RECONNET_SERVICE = 7;
    public static final int PW_DELAY = 200;
    public static final int NET_ROOM_OK = 10021;

    /**游戏类型*/
    public static final int DART_GAME_TYPE_ALL  = 0;     //全部游戏,数据库预留
    public static final int DART_GAME_TYPE_HIGH_EIGHT   =  8;  //8轮高分赛
    public static final int DART_GAME_TYPE_HIGH_FIVE    =   5; //5轮高分赛
    public static final int DART_GAME_TYPE_ZEROONE_THREE    =  301; //301
    public static final int DART_GAME_TYPE_ZEROONE_FIVE     = 501;   //501
    public static final int DART_GAME_TYPE_ZEROONE_SEVEN   =  701;   //701


    public static final int DART_GAME_TYPE_CRICKET_NORMAL  = 10000; //标准米老鼠
    public static final int DART_GAME_TYPE_CRICKET_RANDOM  = 11000; //随机米老鼠
    public static final int DART_GAME_TYPE_THREE_GROUP  = 20000; //3局组合赛
    public static final int DART_GAME_TYPE_FIVE_GROUP  =  21000; //5局组合赛
    public static final int DART_GAME_TYPE_SEVEN_GROUP = 22000; //7局组合赛

    public final static int TIME_HIDE_TEXTVIEW = 777000;
    public final static int NUMBER_SHOW_TEXTVIEW = 888000;


    /**游戏参与者类型*/
    public static final int DART_GAME_USER_TYPE_ROBOT= 0; //机器人
    public static final int DART_GAME_USER_TYPE_USER=  1; //普通用户
    public static final int DART_GAME_USER_TYPE_ADMIN= 2; //创建人

    /**游戏状态*/
    public static final int DART_GAME_ROOM_STATUS_NORMAL=  0; //等待开始游戏,此状态才能增加游戏玩家
    public static final int DART_GAME_ROOM_STATUS_START=1; //游戏进行中
    public static final int DART_GAME_ROOM_STATUS_END=  2; //游戏结束
    public static final int DART_GAME_ROOM_STATUS_CLOSE=3; //游戏中途结束

    /**     身份常量定义,0为普通用户     */
    public static final int USER_ROLE_MINGREN=               8; //名人
    public static final int USER_ROLE_ZHUBO=                10;//主播
    public static final int USER_ROLE_ZHUCHANGJIAOLIAN=     11;//驻场教练
    public static final int USER_ROLE_JIAOLIAN=             12;//教练
    public static final int USER_ROLE_DAILIREN=             13;//代理人
    public static final int USER_ROLE_KEFU=                 101;//客服
    public static final int USER_ROLE_DUCHA=                102;//督查
    public static final int USER_ROLE_YIYUAN=               103;//议员
    public static final int USER_ROLE_FAGUAN=               104;//法官

    /**排名统计类型*/
    public static final int CANCEL_PW = 1102;                       // POP 取消
    public static final int DART_ORDER_TYPE_GAME=               100;//游戏局数排名
    public static final int DART_ORDER_TYPE_GAME_TIME=          101;//游戏时长
    public static final int DART_ORDER_TYPE_GAME_HAT_TRICK=     102;//完成HAT TRICK次数
    public static final int DART_ORDER_TYPE_GAME_BULL=          103;//命中BULL次数
    public static final int DART_ORDER_TYPE_GAME_LOW_TON=       104;//完成LOW TON次数
    public static final int DART_ORDER_TYPE_HIGH_SCORE=         200;//八轮高分排名,单局最高分
    public static final int DART_ORDER_TYPE_ZEROONE_DART=       300;//501排名,单局最少结镖数
    public static final int DART_ORDER_TYPE_CRICKET_MPR=        400;//标准米老鼠排名,单局MPR最高分
    public static final int DART_ORDER_TYPE_LEVEL=              500;//用户综合水平等级排行
    public static final int DART_ORDER_TYPE_USER_EXP=           501;//用户经验等级榜
    public static final int DART_ORDER_TYPE_MEDAL_COUNT=        502;//用户勋章榜
    public static final int DART_ORDER_TYPE_GOLD=               503;//用户金币榜



    public static final int DART_ORDER_TYPE_WIN_GOLD = 600;
    /**
     * 联网大厅胜率榜
     **/
    public static final int DART_ORDER_TYPE_WINNING = 601;



    /**统计日期类型*/
    public static final int DART_ORDER_DATE_TYPE_WEEK=          0;//周
    public static final int DART_ORDER_DATE_TYPE_MONTH=         1;//月
    public static final int DART_ORDER_DATE_TYPE_YEAR=          2;//年
    public static final int DART_ORDER_DATE_TYPE_ALL=           3;//总

    /**系统消息通知类型定义*/
    public static final int SYSMSG_TYPE_USER_UPLEVEL=                 100;//用户等级提升
    public static final int SYSMSG_TYPE_USER_FINISH_TASK=             200;//完成任务
    public static final int SYSMSG_TYPE_USER_GET_MEDAL=               300;//获得成就勋章
    public static final int SYSMSG_TYPE_USER_MEDAL_UPLEVEL=           301;//勋章升级

    /**金币流水类型*/


    public static final int USER_GOLD_TYPE_INCRE=          0;//增加
    public static final int USER_GOLD_TYPE_DECRE=          1;
    public static final int USER_GOLD_OPTYPE_TASK=         1000;//任务奖励
    public static final int USER_GOLD_OPTYPE_UPLEVEL=      1001;//升级奖励
    public static final int USER_GOLD_OPTYPE_MEDAL=        1002;//成就奖励
    public static final int USER_GOLD_OPTYPE_ADMIN=        1003;//后台修改
    public static final int USER_GOLD_OPTYPE_EXCHANGE=     1004;//兑换码兑换
    public static final int USER_GOLD_OPTYPE_LOGIN_SEND     = 1005;
    public static final int USER_GOLD_OPTYPE_BUY            = 1006;
    public static final int USER_GOLD_OPTYPE_HALL_GAME_WIN  = 1007;
    public static final int USER_GOLD_OPTYPE_HALL_TICKET    = 2000;
    public static final int USER_GOLD_OPTYPE_HALL_CASH      = 2001;
    public static final int USER_GOLD_OPTYPE_HALL_GAME_LOSE = 2002;

    /**对战房间状态*/
    public static final int NETROOM_STATUS_NORMAL       =       1; //等待开始
    public static final int NETROOM_STATUS_GAME_START   =       2; //游戏中
    public static final int NETROOM_STATUS_GAME_END     =       3; //游戏结束

    /**对战房间用户类型*/
    public static final int NETROOM_USER_TYPE_MASTER    =   1; //房主
    public static final int NETROOM_USER_TYPE_GAME      =   2; //参与者
    public static final int NETROOM_USER_TYPE_LOOK      =   3; //旁观者
    public static final int NETROOM_USER_TYPE_WAIT      =   4; //等待再来一局

    /**对战房间邀请人类型*/
    public static final int NETROOM_INVITE_USER_TYPE_FRIEND         =   1; //好友
    public static final int NETROOM_INVITE_USER_TYPE_FIGHTING       =   2; //最近战斗
    public static final int NETROOM_INVITE_USER_TYPE_NEARBY         =   3; //附近推荐
    public static final int NETROOM_INVITE_USER_TYPE_SEARCH         =   4; //搜索
    public static final int NETROOM_INVITE_USER_TYPE_INVITE         =   5; //已邀请
    public static final int NETROOM_INVITE_USER_TYPE_BLUE           =   6; //蓝牙在线
    public static final int NETROOM_INVITE_USER_TYPE_FIGHTING_NEW   =   7; //最近24小时战斗

    /**对战房间邀请人状态*/
    public static final int NETROOM_INVITE_USER_STATUS_NORMAL=          0; //正常
    public static final int NETROOM_INVITE_USER_STATUS_INVITE=          1; //已邀请,等待响应
    public static final int NETROOM_INVITE_USER_STATUS_INVITE_YES=      2; //已同意
    public static final int NETROOM_INVITE_USER_STATUS_INVITE_NO=       3; //已拒绝
    public static final int NETROOM_INVITE_USER_STATUS_GAME=            4; //游戏中
    public static final int NETROOM_INVITE_USER_STATUS_INVITE_NO_MAX=   5; //已拒绝次数达到上限,不允许再次邀请

    /**房间聊天消息类型*/
    public static final int NETROOM_CONTENT_TYPE_SYSTEM=    1; //系统消息
    public static final int NETROOM_CONTENT_TYPE_USER=      2; //用户聊天

    /**兑换码状态*/
    public static final int REDEEMCODE_NOT_USED_STATUS=     1; //未激活
    public static final int REDEEMCODE_HAV_USED_STATUS=     2; //已激活

    /**兑换码类型*/
    public static final int BUY_DART_GIVE_TYPE=           1; //买飞镖靶赠送
    public static final int DIRECT_GIVE_TYPE=             2; //直接赠送

    /**商城订单状态*/
    public static final int SHOP_ORDER_STATUS_NOT=        0; //未支付
    public static final int SHOP_ORDER_STATUS_WAIT=       1; //待发货
    public static final int SHOP_ORDER_STATUS_SENT=       2; //已发货
    public static final int SHOP_ORDER_STATUS_COMPLETE=   3; //已完成
    public static final int SHOP_ORDER_STATUS_CLOSE=      4; //交易关闭
    public static final int SHOP_ORDER_STATUS_DELETE=     8; //已删除

    /**分润信息状态*/
    public static final int SHOP_SHAREPROFITRATE_STATUS_YES= 1; //已启用
    public static final int SHOP_SHAREPROFITRATE_STATUS_NO=  2; //未启用
    public static final int SHOP_SHAREPROFITRATE_STATUS_DEL= 3; //已删除

    /**发票状态*/
    public static final int SHOP_INVOICE_STATUS_WAIT=     1; //待开发票
    public static final int SHOP_INVOICE_STATUS_YES=      2; //已开发票
    public static final int SHOP_INVOICE_STATUS_NO=       3; //不开发票

    /**代理人状态*/
    public static final int SHOP_AGENT_STATUS_YES=        1; //启用
    public static final int SHOP_AGENT_STATUS_NO=         2; //禁用

    /**轮播图状态*/
    public static final int SHOP_CAROUSELFIGURE_STATUS_NO=1; //禁用
    public static final int SHOP_CAROUSELFIGURE_STATUS_YES=  2; //启用

    /**商品状态*/
    public static final int SHOP_ITEM_STATUS_NO =1; //待上架
    public static final int SHOP_ITEM_STATUS_YES=  2; //上架
    public static final int SHOP_ITEM_STATUS_WAIT= 3; //待补货
    public static final int SHOP_ITEM_STATUS_DEL=  3; //冻结

    /**商品是否推荐*/
    public static final int SHOP_ITEM_RECOMMEND_NO = 0; //不推荐
    public static final int SHOP_ITEM_RECOMMEND_YES =1; //推荐

    /**库存状态*/
    public static final int SHOP_STOCK_STATUS_LESS = 0; //手动输入
    public static final int SHOP_STOCK_STATUS_MORE = 1; //货源充足

    public static final int SHOP_SHAREPROFIG_STATUS_YES =  1; //分润状态 启用
    public static final int SHOP_SHAREPROFIG_STATUS_NO = 2; //分润状态 禁用
    public static final int SHOP_SHAREPROFIG_STATUS_MERGE = 3; //分润状态 合并


    /**
     * 证件类型
     **/
    public static final int CERTIFICATE_TYPE_HZ = 1;//护照（外籍）
    public static final int CERTIFICATE_TYPE_SFZ = 2;//身份证
    public static final int CERTIFICATE_TYPE_TXZ = 3;//通行证（港澳台）

    /**
     * 游戏大厅类型
     **/
    public static final int GAME_HALL_TYPE_NEW = 1;//新手训练营
    public static final int GAME_HALL_TYPE_HIGHT_EIGHT = 2;//八轮高分区
    public static final int GAME_HALL_TYPE_ZERO_FIVE = 3;//501区
    public static final int GAME_HALL_TYPE_CRICKET = 4;//标准米老鼠
    public static final int GAME_HALL_TYPE_MASTER = 5;//大师区
    /***/
    public static final String NOTIFY_GAME_START = "parseTypeGameStartN";
    public static final String NOTIFY_GAME_UPDATE = "parseTypeUpdateGameN";
    public static final String NOTIFY_GAME_UPDATE_CONTENT = "parseTypeUpdateContentN";
    public static final String NOTIFY_GAME_ROOM_UPDATE = "parseTypeUpdateRoomN";
    public static final String NOTIFY_GAME_GOTO_ROOM = "parseTypeGotoRoomN";
    public static final String NOTIFY_GAME_GET_ROOMINFON = "parseTypeGetNetRoomInfoN";
    public static final String NOTIFY_GAME_EXIT_ROOM = "parseTypeExitNetRoomN";
    public static final String NOTIFY_GAME_INVITE = "parseTypeInviteN";
    public static final String NOTIFY_GAME_DART_DETAIL= "parseTypeGetGameDartDataN";
    public static final String NOTIFY_GAME_BLUE_STATUS = "parseTypeSetBlueStatus";
    public static final String NOTIFY_GAME_ROOM_DESTORY_ = "parseTypeDestroyNetRoom";

    /**
     * 游戏大厅聊天消息类型
     **/
    public static final int GAME_HALL_CONTENT_TYPE_TEXT = 1;//文字消息
    public static final int GAME_HALL_CONTENT_TYPE_INVATE = 2;//邀请消息
    public static final int GAME_HALL_CONTENT_TYPE_SYSTEM = 3;//系统消息


    public static final String REQUEST_GAME_START = "parseTypeStartGame";
    public static final String REQUEST_GAME_EXIT_ROOM = "parseTypeExitNetRoom";
    public static final String REQUEST_GAME_GET_ROOMINFO = "parseTypeGetNetRoomInfo";
    public static final String REQUEST_GAME_INVITE = "parseTypeInvite";
    public static final String REQUEST_GAME_GET_DART_DATA = "parseTypeGetGameDartData";
    public static final String REQUEST_GAME_FAILED_EXIT_ROOM = "parseTypeFailExitNetRoom";
    public static final String REQUEST_GAME_FAIL_INVITE = "parseTypeFailResponseInvite";
    public static final String REQUEST_GAME__INVITE = "parseTypeResponseInvite";
    public static final String REQUEST_GAME_BLUE_STATUS = "parseTypeSetBlueStatus";
    public static final String REQUEST_GAME_UPDATE_ROOM = "parseTypeUpdateRoom";
    public static final String REQUEST_GAME_UPLOAD_DARTDATA = "parseTypeUploadDartData";

}
