package com.zige.robot;

/**
 * Created by zhanghuan on 2016/3/17.
 */
public class HttpLink {
    //测试环境   http://115.159.123.33:61/app/
    //生产环境   http://115.159.123.33:60/app/

    //    private final static String BASE_URL ="http://115.159.123.33:61/app/"; 正式环境
    public final static String BASE_URL = BuildConfig.BASE_URL;
    //    private final static String BASE_URL ="http://m.vrdube.com/app/";
    public static final String COMMON_URL = BASE_URL + "app/common/";
    public static final String CheckMobileLink = BASE_URL + "app/robot/checkMobile";
    public static final String LoginLink = BASE_URL + "app/robot/login";
    public static final String RegisterLink = BASE_URL + "app/robot/register";

    public static final String SmsGenerateLink = BASE_URL + "app/robot/smsGenerate";
    public static final String BindRobotLink = BASE_URL + "app/robot/bindRobot";
    public static final String ResetParswordLink = BASE_URL + "app/robot/pwdReset";
    public static final String GetInfByTimeDesc = BASE_URL + "app/robot/getInfByTimeDesc"; //获取养成记录
    public static final String DelCmcatlg = BASE_URL + "app/robot/delCmcatlg"; //删除养成记录
    public static final String AddCmcatlg = BASE_URL + "app/robot/addCmcatlg"; //添加养成记录
    public static final String EdCmcatlg = BASE_URL + "app/robot/edCmcatlg"; //修改养成记录
    public static final String GetRecentWork = BASE_URL + "app/robot/getRecentWork"; //获取最近添加记录
    public static final String GetPastWorks = BASE_URL + "app/robot/getPastWorks"; //获取历史作业记录
    public static final String GetUsableRoles = BASE_URL + "app/robot/getUsableRoles"; //获取可用角色列表
    public static final String UnbindRobot = BASE_URL + "app/robot/unbindRobot"; //解绑机器人
    public static final String FamilyList = BASE_URL + "app/robot/familyList"; //机器人家庭列表
    public static final String FriendList = BASE_URL + "app/robot/friendList"; //机器人朋友列表
    public static final String SearchFriend = BASE_URL + "app/robot/searchFriend"; //搜索朋友
    public static final String AddFriend = BASE_URL + "app/robot/addFriend"; //添加朋友
    public static final String RunRecordStat = BASE_URL + "app/robot/runRecordStat"; //运行记录数据统计
    public static final String RunRecordList = BASE_URL + "app/robot/runRecordList"; //历史运行记录数据查询
    public static final String DelFamily = BASE_URL + "app/robot/delFamily"; //删除家庭成员
    public static final String DelFriend = BASE_URL + "app/robot/delFriend"; //删除好友
    public static final String SetGameTime = BASE_URL + "app/robot/setGameTime"; //设置游戏时间
    public static final String UpdateRobotInfo = BASE_URL + "app/robot/updateRobotInfo"; //修改领养信息
    public static final String AddFaceInfo = BASE_URL + "app/robot/updateUserFaceInfo"; //添加用户脸部信息
    public static final String RobotDeviceList = BASE_URL + "app/robot/robotDeviceList"; //用户绑定的机器人列表
    public static final String SetClock = BASE_URL + "app/robot/setClock"; //设置闹钟
    public static final String DeleteClock = BASE_URL + "app/robot/deleteClock"; //删除闹钟
    public static final String GetClock = BASE_URL + "app/robot/getClock"; //获取闹钟
    public static final String CloseClock = BASE_URL + "app/robot/closeClock"; //关闭闹钟
    public static final String ChangeDevice = BASE_URL + "app/robot/changeDevice"; //切换设备
    public static final String Feedback = BASE_URL + "app/robot/feedback"; //吐槽


    //获取七牛token
    public final static String GET_NIQIU_TOKEN = BASE_URL + "app/robot/getQiniuToken";


}
