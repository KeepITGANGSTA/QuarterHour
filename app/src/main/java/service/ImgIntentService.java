package service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import api.Common;
import bwie.com.quarterhour.App;

/**
 * Created by 李英杰 on 2017/12/20.
 * Description：
 */

public class ImgIntentService extends IntentService {

    private Bitmap bitmap=null;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  Used to name the worker thread, important only for debugging.
     */
    public ImgIntentService() {
        super("ImgIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("ImgIntentService创建-----");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("ImgIntentService执行-----");
        String url=intent.getStringExtra("ivUrl");
        Bitmap bitmap = GetImageInputStream(url);
        SavaImage(bitmap, Common.Load_Dir);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bitmap=null;
        System.out.println("ImgIntentService销毁-----");
        Toast.makeText(App.AppContext, "已保存至本地："+Common.Load_Dir, Toast.LENGTH_SHORT).show();
    }

    private Bitmap GetImageInputStream(String imageurl){
        URL url;
        HttpURLConnection connection=null;
        try {
            url = new URL(imageurl);
            connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream=connection.getInputStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static void SavaImage(Bitmap bitmap, File dir){
        // File file=new File(path);
        FileOutputStream fileOutputStream=null;
        //文件夹不存在，则创建它
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            fileOutputStream=new FileOutputStream(dir+"/"+System.currentTimeMillis()+".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
