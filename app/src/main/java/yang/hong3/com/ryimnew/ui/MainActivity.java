package yang.hong3.com.ryimnew.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import yang.hong3.com.ryimnew.R;
import yang.hong3.com.ryimnew.app.MyApp;
import yang.hong3.com.ryimnew.global.Constants;
import yang.hong3.com.ryimnew.util.NLog;


public class MainActivity extends BaseActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    String token;

    @Override
    protected void initView() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        token = sp.getString(Constants.SEALTALK_LOGING_TOKEN,"");
        if (!TextUtils.isEmpty(token)){
            connect(token);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void click(View view){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
                    startActivity(new Intent(MainActivity.this, MainImActivity.class));
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
