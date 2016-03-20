package com.xyl.mmall.framework.protocol;

/**
 * 响应代码定义
 * 
 * @author Rhythm
 */
public final class ResponseCode {
    /*
     * 通用错误码
     */
    public static final short RES_SUCCESS = 200;

    public static final short RES_ENOTINVITE = 300; // 用户没有被邀请

    public static final short RES_EBAN = 301; // 用户在黑名单中

    public static final short RES_EUIDPASS = 302; // 密码错误

    public static final short RES_EUREG_EXIST = 303; // 要注册的帐号已存在

    public static final short RES_EUREG_NO_EXIST = 305; // 要注销的帐号不存在

    public static final short RES_EBATCH_NO_CHANGE = 304; // 增量接口，没有更改

    public static final short RES_ETOKEN = 306;//token 失效

    public static final short RES_ADDR_BLOCKADED = 310; // 登录IP或MAC被封锁

    public static final short RES_IP_NOT_ALLOWED = 315; // 内部帐户不允许在该地址登陆

    public static final short RES_UID_OR_PASS_ERROR = 316; // 用户名不存在或密码错误

    public static final short RES_ECLIENTVERSION = 317; // 客户端版本不匹配,用于协议升级和登录检查

    public static final short RES_DOMAINNOTEXIST = 320; // 域名不存在

    public static final short RES_DOMAIN_BAN = 321; // 域名被禁用

    public static final short RES_ERROR = 400; // 操作失败

    public static final short RES_ENOTAUTH = 401; // 未授权
    
    public static final short RES_FORBIDDEN = 403; // 禁止操作

    public static final short RES_ENOTEXIST = 404; // 目标(对象或用户)不存在

    public static final short RES_NOT_MODIFIED = 406; // 数据自上次查询以来未发生变化（用于增量更新）

    public static final short RES_ETIMEOUT = 408; // 超时

    public static final short RES_EVERIFY = 413; // 验证失败

    public static final short RES_EPARAM = 414; // 客户端提交了非法参数

    public static final short RES_ECONNECTION = 415; // 网络连接出现问题

    public static final short RES_EFREQUENTLY = 416; // 操作太过频繁

    public static final short RES_EEXIST = 417; // 对象已经存在

    public static final short RES_EHTTP = 418; // http协议访问错误

    public static final short RES_ESIZE_LIMIT = 419; // 大小超过限制

    public static final short RES_EOP_EXCEPTION = 420; // 操作出现异常

    public static final short RES_OP_CANCEL = 421; // 权限被取消

    public static final short RES_ACCOUNT_EXP = 430; // 帐号过期

    public static final short RES_EUNKNOWN = 500; // 不知道什么错误

    public static final short RES_EDB = 501; // 数据库操作失败

    public static final short RES_TOOBUZY = 503;// 服务器太忙

    public static final short RES_ENOTENOUGH = 507; // 不足

    public static final short RES_OVERDUE = 508; // 超过期限

    public static final short RES_INVALID = 509; // 已经失效

    public static final short RES_USER_NOT_EXIST = 510; // 用户不存在

    public static final short RES_APP_CANNOT_REACH = 511; // App 不可达

    public static final short RES_GATE_CANNOT_REACH = 512; // GATE 不可达

    public static final short RES_YIXIN_CANNOT_REACH = 513; // YIXIN 不可达

    // app-famework error
    public static final short RES_ENOSERVICE = 900;

    public static final short RES_ENOCOMMAND = 901;

    public static final short RES_ERUNTASK = 903;

    /*
     * 数据整解编错误代码
     */
    public static final short RES_EPACKET = 999; // 打包错误

    public static final short RES_EUNPACKET = 998; // 解包错误

    /**
     * 批量获取，获取量太大
     */
    public static final short RES_UINFO_BATCH_TO_BIG = 3501; // 批量获取，获取量太大

    /**
     * 批量获取，获取量太大
     */
    public static final short RES_UINFO_BATCH_ZERO = 3502; // 批量获取，获取量太大

    /**
     * 好友列表错误码
     */
    public static final short RES_ULIST_FID_INVALID = 4000; // 不是好友

    /**
     * SP服务错误码
     */
    public static final short RES_SP_USER_INVALID = 4100; // 用户不存在

    public static final short RES_SP_OTHER_ERR = 4101; // 超时或其他异常

    public static final short RES_SP_CRC_OVERLOAD = 4102; // 超过会议人数上限

    public static final short RES_SP_CRC_CREATE_FAILED = 4103; // 创建会场失败

    public static final short RES_SP_UAVAILABLE = 4104; // 服务不可用

    public static final short RES_SP_EPARAM = 4105; // 参数错误

    public static final short RES_SP_BIZPHONE_NULL = 4106; // 无业务号码

    /**
     * 10000以上表示服务器错误
     */
    public static final short RES_SE_URS = 10001;// 访问URS服务器错误(注意，不是说URS服务器返回错误)

    public static final short RES_SE_LINK = 10002;// LINK服务器错误

    public static final short RES_SE_APP = 10003;// APP服务器错误

    public static final short RES_SER_PERSIST = 10004;// PERSIST服务器错误

    public static final short RES_SE_MEMCACHE = 10005;// MEMCACHE服务器错误

    public static final short RES_SE_MASTERDB = 10006;// Master DB 错误

    public static final short RES_SE_DDB = 10007;// DDB 错误

    public static final short RES_SE_LOCATE = 10008;// Locate 错误

    public static final short RES_SE_GAMEMASTER = 10009;// GameMaster 错误

    /**
     * 20000+的分配给简约版特有code
     */
    public static final short RES_OPENAPI_ERROR = 20001;// openapi操作失败

    /**
     * 超过30000的，零头表示由URS返回给我们的错误码
     */
    public static final short RES_RETURN_BY_URS = 30000;

    /**
     * 超过40000的，webim 特有的返回错误代码
     */
    public static final int RES_WEBIM_COOKIE_NULL = 40000; // cookie值为null

    public static final int RES_WEBIM_COOKIE_INVALID = 40001; // cookie验证无效

    public static final int RES_WEBIM_COOKIE_IP_INVALID = 40002; // cookie验证ip无效

    /**
     * 不存在绑定的帐号
     */
    public static final short RES_MAIL_NOTEXISTS_BOUND_ACCOUNT = 4501; // 解绑时，不存在绑定的帐号

    /**
     * 绑定时，帐号已经绑定了此帐号
     */
    public static final short RES_MAIL_EXISTS_BOUND_ACCOUNT = 4502; // 绑定时，帐号已经绑定其它帐号，请先解绑

    /**
     * deviceId is null
     */
    public static final short RES_MAIL_LAST_DEVICEID_NULL = 4503;

    /**
     * too much account
     */
    public static final short RES_MAIL_ACCOUNT_TOO_MUCH = 4504;

    /**
     * grand sid and cookie 密码认证不对
     */
    public static final short RES_MAIL_ACCOUNT_PASSWORD_WRONG = 4505;

    /**
     * 以下为权限认证的相关错误码
     */
    public static final short TP_APPKEY_WRONG = 4601;

    public static final short TP_UNKNOW_WRONG = 4602;

    /**
     * 请求语音服务器失败
     */
    public static final short TRANCODE_SERVER_ERROR = 7001;

    /**
     * 转码失败
     */
    public static final short TRANCODE_TRANCODE_ERROR = 7002;

    /**
     * 超过限制
     */
    public static final short TRANCODE_AUDIOTTME_LIMITED = 7003;

    /**
     * radar 某用户访问次数受限
     */
    public static final short RADAR_REPORT_LIMIT = 7004;

    /**
     * radar 某用户进入雷达轮询次数受限
     */
    public static final short RADAR_POLL_LIMIT = 7005;

}
