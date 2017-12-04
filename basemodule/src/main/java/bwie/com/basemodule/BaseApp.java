package bwie.com.basemodule;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;


/**
 * Created by 李英杰 on 2017/11/20.
 * Description：
 */

public abstract class BaseApp extends Application {

    public static Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        AppContext=this;
        Hawk.init(this).build();
        init();
    }



    public abstract void init();



}
