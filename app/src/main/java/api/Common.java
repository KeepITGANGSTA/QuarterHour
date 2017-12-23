package api;

import android.os.Environment;

import java.io.File;

import bwie.com.quarterhour.App;

/**
 * Created by 李英杰 on 2017/11/30.
 * Description：
 */

public class Common {
    public static final String BASE_URL="https://www.zhaoapi.cn/";
    public static final File Load_Dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/quarter_load_iv");
}
