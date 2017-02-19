package yang.hong3.com.ryimnew.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hong3 on 2017-2-18.
 */

public class ToastUtil {

    private static Toast toast;


    public static void show(Context context,String str){
        if (toast == null){
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }

    public static void show(Context context,int id){
        if (toast == null){
            toast = Toast.makeText(context,context.getString(id),Toast.LENGTH_SHORT);
        }else{
            toast.setText(context.getString(id));
        }
        toast.show();
    }

}
