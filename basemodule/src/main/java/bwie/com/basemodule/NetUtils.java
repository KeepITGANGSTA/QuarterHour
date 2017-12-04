package bwie.com.basemodule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 李英杰 on 2017/9/5.
 */

public class NetUtils {
    private static Context context;
    public NetUtils(Context context, NetWork netWork){
        this.context=context;

        ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = systemService.getActiveNetworkInfo();
        if (activeNetworkInfo!=null){
            if (activeNetworkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                netWork.NetWorkWifi();
            }else if (activeNetworkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                netWork.NetWorkPhone();
            }else {
                netWork.NoneNetWork();
            }
        }else {
            netWork.NoneNetWork();
        }
    }




    public interface NetWork{
        void NetWorkWifi();
        void NetWorkPhone();
        void NoneNetWork();
    }
}
