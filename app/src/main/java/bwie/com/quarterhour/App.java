package bwie.com.quarterhour;

import android.content.Context;
import android.util.DisplayMetrics;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import bwie.com.basemodule.BaseApp;

/**
 * Created by 李英杰 on 2017/11/30.
 * Description：
 */

public class App extends BaseApp {
    public static int screen_width=0;
    public static int screen_height=0;
    private RefWatcher install;

    @Override
    public void init() {

        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        install = LeakCanary.install(this);
        getRefWatcher(this);
        getScreen();
    }

    public static RefWatcher getRefWatcher(Context context){
        App app=(App)context.getApplicationContext();
        return app.install;
    }
    private void getScreen() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screen_width=displayMetrics.widthPixels;
        screen_height=displayMetrics.heightPixels;
    }
}
