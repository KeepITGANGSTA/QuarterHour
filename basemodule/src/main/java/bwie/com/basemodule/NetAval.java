package bwie.com.basemodule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 李英杰 on 2017/12/8.
 * Description：
 */

public class NetAval {
    public static boolean NetAvailable(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo!=null && activeNetworkInfo.isConnected()){
            return true;
        }else {
            return false;
        }
    }
}
