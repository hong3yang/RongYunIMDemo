package yang.hong3.com.ryimnew.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import yang.hong3.com.ryimnew.httphelper.RequestUtil;
import yang.hong3.com.ryimnew.httphelper.StringCallback;
import yang.hong3.com.ryimnew.ui.view.LoginView;
import yang.hong3.com.ryimnew.util.NLog;

/**
 * Created by hong3 on 2017-2-18.
 */

public class LoginController {
    private static final String TAG = "LoginController";
    LoginView mLoginView;
    RequestUtil mUtil;

    public LoginController(LoginView loginView) {
        mLoginView = loginView;
        mUtil = new RequestUtil();
    }

    public void getToken(String id, String name, String iconurl){
        RequestParams params = mUtil.getToken(id,name,iconurl);
        x.http().post(params, new StringCallback() {
            @Override
            public void onSuccess(String result) {
                NLog.d(TAG, "onSuccess: "+result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getInt("code")==200){
                        String userId = obj.getString("userId");
                        String token = obj.getString("token");

                        mLoginView.loginSuccess(userId,token);
                    }else{
                        mLoginView.loginFails("登陆失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    mLoginView.loginFails(e.getMessage());
                }

//                /**
//                 * code : 200
//                 * userId : vubDrMkVQ
//                 * token : wwN2/ZOW54HLhCKA/llLvjKKgtRL5ulWrGtEFwDB6EI7j0Xz6ng4iQFitc1IbK7sRSVZvvhLidVrsUqliZ3aH6ubr2bH7kcm
//                 */
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoginView.loginFails(ex.getMessage());
            }
        });
    }
}
