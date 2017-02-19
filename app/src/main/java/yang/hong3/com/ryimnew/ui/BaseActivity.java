package yang.hong3.com.ryimnew.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import yang.hong3.com.ryimnew.util.ToastUtil;

/**
 * Created by Administrator on 2017-2-14.
 */
public abstract class BaseActivity extends FragmentActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        Log.d(TAG, "onCreate: ");


        initView();
        initData();
    }


    public void initData() {
    }

    protected abstract void initView();

    public <T extends View>T  $(int id){
        return (T) findViewById(id);
    }

    public void fastShow(String str){
        ToastUtil.show(this,str);
    }

    public void fastShow(int id){
        ToastUtil.show(this,id);
    }

    public abstract int getLayoutId();

    ProgressDialog mDialog;
    public void showDialog(){
        if (mDialog == null){
            mDialog = new ProgressDialog(this);
        }
        if (mDialog.isShowing()){
            return;
        }
        mDialog.show();
    }

    public void dismissDialog(){
        if (mDialog!= null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }


}
