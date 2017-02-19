package yang.hong3.com.ryimnew.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import yang.hong3.com.ryimnew.httphelper.RequestUtil;
import yang.hong3.com.ryimnew.httphelper.StringCallback;
import yang.hong3.com.ryimnew.ui.view.MainImView;

/**
 * Created by hong3 on 2017-2-18.
 */

public class MainImController {

    MainImView mMainImView;
    RequestUtil util;

    public MainImController(MainImView mainImView) {
        mMainImView = mainImView;
        util = new RequestUtil();
    }

    public void queryFriend(String id) {
        RequestParams params = util.queryFriend(id);
        x.http().post(params, new StringCallback() {
            @Override
            public void onSuccess(String result) {
                //{"code":200,"userName":"12345678901","userPortrait":"11.png","createTime":"2017-02-17 15:34:33"}
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getInt("code") == 200) {
                        mMainImView.quryFriendSuccess(obj.getString("userName"));
                    } else {
                        mMainImView.fails("没有找到指定用户");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mMainImView.fails("查询失败：" + e.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mMainImView.fails(ex.getMessage());
            }
        });
    }
}
