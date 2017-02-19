package yang.hong3.com.ryimnew.httphelper;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.xutils.http.RequestParams;

import java.util.Random;

import yang.hong3.com.ryimnew.util.MD5;

/**
 * Created by hong3 on 2017-2-18.
 */

public class RequestUtil {
    private static final String BASE_RONGYUN = "http://api.cn.ronghub.com";

    private static final String BASE_RONGYUN_USER = BASE_RONGYUN + "/user";

    private static final String APPKEY = "y745wfm8y78yv";
    private static final String APPSECRET = "YsMT4DpIVe13E";

    public RequestParams getServiceParams(String baseUrl) {
        RequestParams params = new RequestParams(baseUrl);
        params.addHeader("CONTENT_TYPE", "application/json");
        params.addHeader("App-Key", APPKEY);
        params.addHeader("charset", "utf-8");

        String rand = (new Random().nextInt(5000) + 5000) + "";
        String time = System.currentTimeMillis() / 1000 + "";
        params.addHeader("Nonce", rand);
        params.addHeader("Timestamp", time);
        params.addHeader("Signature", MD5.encryptToSHA(APPSECRET + rand + time));


        params.setConnectTimeout(5000);
        params.setUseCookie(true);


        return params;
    }

    /*==============================我是等号分割线 以下是用户相关=======================================*/

    /**
     * 获取 token 前置条件需要登录   502 坏的网关 测试环境用户已达上限
     *
     * @param id
     * @param name
     * @param iconurl
     * @return {"code":200, "userId":"jlk456j5", "token":"sfd9823ihufi"}
     */
    public RequestParams getToken(String id, String name, String iconurl) {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/getToken.json");
        params.addBodyParameter("userId", id);
        params.addBodyParameter("name", name);
        params.addBodyParameter("portraitUri", iconurl);
        return params;
    }

    /**
     * 查找用户
     * @param userId
     * @return   {"code":200,"userName":"12345678901","userPortrait":"11.png","createTime":"2017-02-17 15:34:33"}
     */
    public RequestParams queryFriend(String userId){
        RequestParams params = getServiceParams(BASE_RONGYUN_USER+"/info.json");
        params.addBodyParameter("userId",userId);
        return params;
    }

    /**
     * 刷新用户信息  主要用于通知推送使用，刷新此信息不会更新客户端的用户信息，在客户端，应该使用 UserInfoProvider提供用户信息
     *
     * @param id
     * @param name
     * @param iconurl
     * @return {"code":200}
     */
    public RequestParams refresh(@NonNull String id, String name, String iconurl) {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/refresh.json");
        params.addBodyParameter("userId", id);

        if (!TextUtils.isEmpty(name)) {
            params.addBodyParameter("name", name);
        }
        if (!TextUtils.isEmpty(iconurl)) {
            params.addBodyParameter("portraitUri", iconurl);
        }
        return params;
    }

    /**
     * 检查在线状态
     *
     * @param id
     * @return {"code":200,"status":"1"}   在线状态，1为在线，0为不在线。
     */
    public RequestParams checkOnline(@NonNull String id) {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/checkOnline.json");
        params.addBodyParameter("userId", id);
        return params;
    }

    /**
     * 封禁用户
     *
     * @param id
     * @param minute 封禁时长,单位为分钟，最大值为43200分钟
     * @return {"code":200}
     */
    public RequestParams block(@NonNull String id, @NonNull String minute) {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/block.json");
        params.addBodyParameter("userId", id);
        params.addBodyParameter("minute", minute);
        return params;
    }

    /**
     * 解除用户封禁
     *
     * @param id
     * @return {"code":200}
     */
    public RequestParams unblock(@NonNull String id) {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/unblock.json");
        params.addBodyParameter("userId", id);
        return params;
    }

    /**
     * 获取被封禁用户
     *
     * @return {"code":200,"users":[{"userId":"jlk456j5","blockEndTime":"2015-01-11 01:28:20"}]}
     */
    public RequestParams unblockQuery() {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/block/query.json");
        return params;
    }

    /**
     * @param id
     * @param blackUserId 被加黑的用户Id
     * @return {"code":200}
     */
    public RequestParams addBlacklist(@NonNull String id, @NonNull String blackUserId) {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/blacklist/add.json");
        params.addBodyParameter("userId", id);
        params.addBodyParameter("blackUserId", blackUserId);
        return params;

    }

    /**
     * 移除黑名单中用户
     *
     * @param id
     * @param blackUserId
     * @return {"code":200}
     */
    public RequestParams removeBlacklist(@NonNull String id, @NonNull String blackUserId) {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/blacklist/remove.json");
        params.addBodyParameter("userId", id);
        params.addBodyParameter("blackUserId", blackUserId);
        return params;
    }

    /**
     * 获取某用户黑名单列表
     *
     * @return {"code":200,"users":["jlk454","jlk457"]}
     */
    public RequestParams blacklistQuery() {
        RequestParams params = getServiceParams(BASE_RONGYUN_USER + "/blacklist/query.json");
        return params;
    }


    /*========================这里是等号分割线  以下是聊天信息的配置参数================================*/

    private static final String BASE_RONGYUN_MESSAGE = BASE_RONGYUN + "/message";

    /**
     * 发送单聊消息
     * content={\"content\":\"c#hello\"}&fromUserId=2191&toUserId=2191&toUserId=2192&objectName=RC:TxtMsg&pushContent=thisisapush&pushData={\"pushData\":\"hello\"}&count=4&verifyBlacklist=0&isPersisted=1&isCounted=1&isIncludeSender=0
     *
     * @param fromUserId      发送人用户 Id
     * @param toUserId        接收用户 Id，可以实现向多人发送消息，每次上限为 1000 人
     * @param objectName      消息类型
     * @param content         发送消息内容
     * @param pushContent     定义显示的 Push 内容
     * @param pushData        Android 客户端收到推送消息时对应字段名为 pushData
     * @param verifyBlacklist 是否过滤发送人黑名单列表，0 表示为不过滤、 1 表示为过滤，默认为 0 不过滤
     * @param isPersisted     当前版本有新的自定义消息，而老版本没有该自定义消息时，老版本客户端收到消息后是否进行存储，0 表示为不存储、 1 表示为存储，默认为 1 存储消息
     * @param isCounted       当前版本有新的自定义消息，而老版本没有该自定义消息时，老版本客户端收到消息后是否进行未读消息计数，0 表示为不计数、 1 表示为计数，默认为 1 计数，未读消息数增加 1。(
     * @param isIncludeSender 发送用户自已是否接收消息，0 表示为不接收，1 表示为接收，默认为 0 不接收
     * @return {"code":200}
     */
    public RequestParams publishPrivate(@NonNull String fromUserId, @NonNull String toUserId, @NonNull String objectName, @NonNull String content,
                                        String pushContent, String pushData, String verifyBlacklist, String isPersisted,
                                        String isCounted, String isIncludeSender) {
        RequestParams params = getServiceParams(BASE_RONGYUN_MESSAGE + "/private/publish.json");
        params.addBodyParameter("fromUserId", fromUserId);
        params.addBodyParameter("toUserId", toUserId);
        params.addBodyParameter("objectName", objectName);
        params.addBodyParameter("content", content);
        if (!TextUtils.isEmpty(pushContent))
            params.addBodyParameter("pushContent", pushContent);
        if (!TextUtils.isEmpty(pushData))
            params.addBodyParameter("pushData", pushData);
        if (!TextUtils.isEmpty(verifyBlacklist))
            params.addBodyParameter("verifyBlacklist", verifyBlacklist);
        if (!TextUtils.isEmpty(isPersisted))
            params.addBodyParameter("isPersisted", isPersisted);
        if (!TextUtils.isEmpty(isCounted))
            params.addBodyParameter("isCounted", isCounted);
        if (!TextUtils.isEmpty(isIncludeSender))
            params.addBodyParameter("isIncludeSender", isIncludeSender);

        return params;
    }


//    /**
//     *  发送单聊模板消息(未配置完)
//     * @param fromUserId
//     * @param toUserId
//     * @param objectName
//     * @param values
//     * @param content
//     * @param pushContent
//     * @param pushData
//     * @param verifyBlacklist
//     * @return
//     */
//    public RequestParams publishpPrivateTemplate(@NonNull String fromUserId, @NonNull String toUserId, @NonNull String objectName, @NonNull String values,
//                                                 @NonNull String content, @NonNull String pushContent, String pushData, String verifyBlacklist
//    ) {
//        RequestParams params = getServiceParams(BASE_RONGYUN_MESSAGE + "/private/publish_template.json");
//        params.addBodyParameter("fromUserId", fromUserId);
//        params.addBodyParameter("toUserId", toUserId);
//        params.addBodyParameter("objectName", objectName);
//        params.addBodyParameter("values", values);
//        params.addBodyParameter("pushContent", pushContent);
//        params.addBodyParameter("content", content);
//        if (!TextUtils.isEmpty(pushData))
//            params.addBodyParameter("pushData", pushData);
//        if (!TextUtils.isEmpty(verifyBlacklist))
//            params.addBodyParameter("verifyBlacklist", verifyBlacklist);
//        return params;
//    }

    /**
     * 发送系统消息
     * content={\"content\":\"c#hello\"}&fromUserId=2191&toUserId=2191&toUserId=2192&objectName=RC:TxtMsg&pushContent=thisisapush&
     * pushData={\"pushData\":\"hello\"}
     *
     * @param fromUserId  发送人用户 Id
     * @param toUserId    接收用户 Id，可以实现向多人发送消息，每次上限为 1000 人
     * @param objectName  消息类型
     * @param content     发送消息内容
     * @param pushContent 定义显示的 Push 内容
     * @param pushData    Android 客户端收到推送消息时对应字段名为 pushData
     * @param isPersisted 当前版本有新的自定义消息，而老版本没有该自定义消息时，老版本客户端收到消息后是否进行存储，0 表示为不存储、 1 表示为存储，默认为 1 存储消息。
     * @param isCounted   当前版本有新的自定义消息，而老版本没有该自定义消息时，老版本客户端收到消息后是否进行未读消息计数，0 表示为不计数、 1 表示为计数，
     *                    默认为 1 计数，未读消息数增加 1
     * @return {"code":200}
     */
    public RequestParams publishSystem(@NonNull String fromUserId, @NonNull String toUserId,
                                       @NonNull String objectName, @NonNull String content,
                                       String pushContent, String pushData, String isPersisted, String isCounted) {
        RequestParams params = getServiceParams(BASE_RONGYUN_MESSAGE + "/system/publish.json");
        params.addBodyParameter("fromUserId", fromUserId);
        params.addBodyParameter("toUserId", toUserId);
        params.addBodyParameter("objectName", objectName);
        params.addBodyParameter("content", content);
        if (!TextUtils.isEmpty(pushContent))
            params.addBodyParameter("pushContent", pushContent);
        if (!TextUtils.isEmpty(pushData))
            params.addBodyParameter("pushData", pushData);
        if (!TextUtils.isEmpty(isPersisted))
            params.addBodyParameter("isPersisted", isPersisted);
        if (!TextUtils.isEmpty(isCounted))
            params.addBodyParameter("isCounted", isCounted);


        return params;
    }


    /**
     * 发送群组消息
     * <p>
     * content={\"content\":\"c#hello\"}&fromUserId=2191&toGroupId=2193&toGroupId=2192&objectName=RC:TxtMsg&pushContent=thisisapush&
     * pushData={\"pushData\":\"hello\"}&isPersisted=1&isCounted=1&isIncludeSender=0
     *
     * @param fromUserId      发送人用户 Id
     * @param toGroupId       接收用户 Id，可以实现向多人发送消息，每次上限为 1000 人
     * @param objectName      消息类型
     * @param content         发送消息内容
     * @param pushContent     定义显示的 Push 内容
     * @param pushData        Android 客户端收到推送消息时对应字段名为 pushData
     * @param isPersisted     当前版本有新的自定义消息，而老版本没有该自定义消息时，老版本客户端收到消息后是否进行存储，0 表示为不存储、 1 表示为存储，默认为 1 存储消息。
     * @param isCounted       当前版本有新的自定义消息，而老版本没有该自定义消息时，老版本客户端收到消息后是否进行未读消息计数，0 表示为不计数、 1 表示为计数，
     *                        默认为 1 计数，未读消息数增加 1
     * @param isIncludeSender 发送用户自已是否接收消息，0 表示为不接收，1 表示为接收，默认为 0 不接收。
     * @return
     */
    public RequestParams publishGroup(@NonNull String fromUserId, @NonNull String toGroupId,
                                      @NonNull String objectName, @NonNull String content,
                                      String pushContent, String pushData, String isPersisted,
                                      String isCounted, String isIncludeSender) {
        RequestParams params = getServiceParams(BASE_RONGYUN_MESSAGE + "/group/publish.json");
        params.addBodyParameter("fromUserId", fromUserId);
        params.addBodyParameter("toGroupId", toGroupId);
        params.addBodyParameter("objectName", objectName);
        params.addBodyParameter("content", content);
        if (!TextUtils.isEmpty(pushContent))
            params.addBodyParameter("pushContent", pushContent);
        if (!TextUtils.isEmpty(pushData))
            params.addBodyParameter("pushData", pushData);
        if (!TextUtils.isEmpty(isPersisted))
            params.addBodyParameter("isPersisted", isPersisted);
        if (!TextUtils.isEmpty(isCounted))
            params.addBodyParameter("isCounted", isCounted);
        if (!TextUtils.isEmpty(isIncludeSender))
            params.addBodyParameter("isIncludeSender", isIncludeSender);

        return params;
    }


    /**
     * 发送讨论组消息
     * content={\"content\":\"c#hello\"}&fromUserId=2191&toDiscussionId=2193&objectName=RC:TxtMsg&pushContent=thisisapush&
     * pushData={\"pushData\":\"hello\"}&isPersisted=1&isCounted=1&isIncludeSender=0
     *
     * @param fromUserId
     * @param toDiscussionId
     * @param objectName
     * @param content
     * @param pushContent
     * @param pushData
     * @param isPersisted
     * @param isCounted
     * @param isIncludeSender
     * @return
     */
    public RequestParams publishDiscussion(@NonNull String fromUserId, @NonNull String toDiscussionId,
                                           @NonNull String objectName, @NonNull String content,
                                           String pushContent, String pushData, String isPersisted,
                                           String isCounted, String isIncludeSender) {

        RequestParams params = getServiceParams(BASE_RONGYUN_MESSAGE + "/discussion/publish.json");
        params.addBodyParameter("fromUserId", fromUserId);
        params.addBodyParameter("toDiscussionId", toDiscussionId);
        params.addBodyParameter("objectName", objectName);
        params.addBodyParameter("content", content);
        if (!TextUtils.isEmpty(pushContent))
            params.addBodyParameter("pushContent", pushContent);
        if (!TextUtils.isEmpty(pushData))
            params.addBodyParameter("pushData", pushData);
        if (!TextUtils.isEmpty(isPersisted))
            params.addBodyParameter("isPersisted", isPersisted);
        if (!TextUtils.isEmpty(isCounted))
            params.addBodyParameter("isCounted", isCounted);
        if (!TextUtils.isEmpty(isIncludeSender))
            params.addBodyParameter("isIncludeSender", isIncludeSender);

        return params;
    }


    /**
     * 发送广播消息
     * content={\"content\":\"c#hello\"}&fromUserId=2191&objectName=RC:TxtMsg&pushContent=thisisapush&pushData={\"pushData\":\"hello\"}&os=iOS
     *
     * @param fromUserId  发送人用户 Id
     * @param objectName  消息类型
     * @param content     发送消息内容
     * @param pushContent 定义显示的 Push 内容
     * @param pushData    Android 客户端收到推送消息时对应字段名为 pushData
     * @param os          针对操作系统发送 Push，值为 iOS 表示对 iOS 手机用户发送 Push ,为 Android 时表示对 Android 手机用户发送
     *                    Push ，如对所有用户发送 Push 信息，则不需要传 os 参数。
     * @return
     */
    public RequestParams broadcast(String fromUserId, @NonNull String objectName, @NonNull String content,
                                   String pushContent, String pushData, String os) {
        RequestParams params = getServiceParams(BASE_RONGYUN_MESSAGE + "/broadcast.json");
        params.addBodyParameter("fromUserId", fromUserId);
        params.addBodyParameter("objectName", objectName);
        params.addBodyParameter("content", content);
        if (!TextUtils.isEmpty(pushContent))
            params.addBodyParameter("pushContent", pushContent);
        if (!TextUtils.isEmpty(pushData))
            params.addBodyParameter("pushData", pushData);
        if (!TextUtils.isEmpty(os))
            params.addBodyParameter("os", os);

        return params;
    }


    /**
     * 消息历史记录下载地址获取(消息记录以日志方式提供，并对文件进行 ZIP 压缩，通过该接口返回消息记录文件下载地址后，请自行下载。 )
     *
     *
     *  日志文件中，消息格式为 json
     * {
     * "appId": "8w7jv4qb7k5wy",
     * "fromUserId": "99921",
     * "targetId": "4974",
     * "targetType": 3,
     * "GroupId": "4971",
     * "classname": "RC:TxtMsg",
     * "content": {
     * "content": "求帮助"
     * },
     * "dateTime": "2015-05-27 08:18:30.709",
     * "msgUID": "596E-P5PG-4FS2-7OJK"
     * }
     *
     * @param date
     * @return   {"code":200,"url":"http://aa.com/1/c6720eea-452b-4f93-8159-7af3046611f1.gz","date":"2014010101"}
     */
    public RequestParams history(@NonNull String date) {
        RequestParams params = getServiceParams(BASE_RONGYUN_MESSAGE + "/history.json");
        params.addBodyParameter("date", date);
        return params;
    }

    /**
     * 消息历史记录删除
     *
     *
     * @param date
     * @return   {"code":200}
     */
    public RequestParams historyDelete(@NonNull String date) {
        RequestParams params = getServiceParams(BASE_RONGYUN_MESSAGE + "/history/delete.json");
        params.addBodyParameter("date", date);
        return params;
    }

/*==================================我是等号分割线  以下是群组相关=======================================*/



}
