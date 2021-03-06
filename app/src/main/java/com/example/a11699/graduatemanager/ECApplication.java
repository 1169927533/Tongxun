package com.example.a11699.graduatemanager;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class ECApplication extends Application {

    // 上下文菜单
    private static Context mContext;

    // 记录是否已经初始化
    private boolean isInit = false;

    private static String s_id;
    private static String s_name;
    private SharedPreferences pref;//喜好设置
    private SharedPreferences.Editor editor;

//个人的照片
    public static Bitmap personImage;
    //对方照片
    public static Bitmap duifangImage;
    //
    public  static List<Bitmap> list=new ArrayList<>();
    @Override public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.i("friend","ECAMplcationd的Oncreatd是在什么时候被执行");
        // 初始化环信SDK
        initEasemob();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();


    }
    public static Bitmap getDuifangImage() {
        return duifangImage;
    }

    public static void setDuifangImage(Bitmap duifangImage) {
        ECApplication.duifangImage = duifangImage;
    }

    public static Bitmap getPersonImage() {
        return personImage;
    }

    public static void setPersonImage(Bitmap personImage) {
        ECApplication.personImage = personImage;
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        ECApplication.mContext = mContext;
    }

    /**
     *
     */
    private void initEasemob() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        Log.i("asd","我执行了");
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回

            return;
        }
        if (isInit) {

            return;
        }

        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(mContext, initOptions());
        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);

        // 设置初始化已经完成
        isInit = true;
    }

    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);
        return   options;
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager =
                (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info =
                    (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return processName;
    }

    public static String getS_id() {
        return s_id;
    }

    public static void setS_id(String s_id) {
        ECApplication.s_id = s_id;
    }

    public static String getS_name() {
        return s_name;
    }

    public static void setS_name(String s_name) {
        ECApplication.s_name = s_name;
    }
}