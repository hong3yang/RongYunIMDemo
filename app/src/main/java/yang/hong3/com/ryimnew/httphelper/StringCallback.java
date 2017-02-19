package yang.hong3.com.ryimnew.httphelper;

import org.xutils.common.Callback;

/**
 * Created by hong3 on 2017-2-18.
 */

public abstract class StringCallback implements Callback.CommonCallback<String> {
    @Override
    public abstract void onSuccess(String result);

    @Override
    public abstract void onError(Throwable ex, boolean isOnCallback);

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
