package yang.hong3.com.ryimnew.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * Created by hong3 on 2017-2-18.
 */

public class MyApp extends Application {

    public static String token;
    public static String connectId;

    @Override
    public void onCreate() {
        super.onCreate();

        RongIM.init(this);

        x.Ext.init(this);
        x.Ext.setDebug(true);
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
