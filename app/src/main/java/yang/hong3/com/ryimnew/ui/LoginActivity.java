package yang.hong3.com.ryimnew.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import yang.hong3.com.ryimnew.R;
import yang.hong3.com.ryimnew.app.MyApp;
import yang.hong3.com.ryimnew.controller.LoginController;
import yang.hong3.com.ryimnew.global.Constants;
import yang.hong3.com.ryimnew.ui.view.LoginView;
import yang.hong3.com.ryimnew.util.NLog;

public class LoginActivity extends BaseActivity implements LoginView {

    EditText etUser, etNick;
    String userNIckName, userId,userPortrait="11.png";
    Button login;
    LoginController mController;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();

        String oldId = sp.getString(Constants.SEALTALK_LOGIN_ID, "");
        String oldNickName = sp.getString(Constants.SEALTALK_LOGIN_NAME, "");

        etUser = ((TextInputLayout) $(R.id.edt_password)).getEditText();
        etNick = ((TextInputLayout) $(R.id.edt_user)).getEditText();
        login = $(R.id.btn_login);


        mController = new LoginController(this);
        click();
    }

    private void click() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });
    }

    private void getToken() {
        userNIckName = etNick.getText().toString();
        userId = etUser.getText().toString();

        if (!TextUtils.isEmpty(userNIckName) && !TextUtils.isEmpty(userId)) {
            mController.getToken(userId, userNIckName, userPortrait);
            editor.putString(Constants.SEALTALK_LOGIN_ID,userId);
            editor.putString(Constants.SEALTALK_LOGIN_NAME,userNIckName);
            editor.putString(Constants.SEALTALK_LOGING_PORTRAIT,userPortrait);
            editor.apply();
        } else {
            fastShow("请完善用户信息");
        }
    }


    @Override
    public void loginSuccess(String userId, String token) {
        MyApp.connectId = userId;
        MyApp.token = token;
        connect(token);

    }

    @Override
    public void loginFails(String str) {
        fastShow(str);
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 { #init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(final String token) {

        if (getApplicationInfo().packageName.equals(MyApp.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    fastShow("连接通讯服务器异常");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    NLog.d("LoginActivity", "--onSuccess" + userid);
                    editor.putString(Constants.SEALTALK_LOGIN_ID,userid);
                    editor.putString(Constants.SEALTALK_LOGING_TOKEN,token);
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainImActivity.class));
                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    fastShow("连接通讯服务器失败：" + errorCode);
                }
            });
        }
    }
}
