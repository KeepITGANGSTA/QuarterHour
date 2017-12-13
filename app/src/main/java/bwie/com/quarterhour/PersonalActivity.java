package bwie.com.quarterhour;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.hawk.Hawk;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;

import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BaseApp;
import bwie.com.basemodule.BasePresent;
import bwie.com.basemodule.FileUtils;
import bwie.com.basemodule.SharedPreferencesUtil;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersonalActivity extends BaseActivity {

    private AutoRelativeLayout rel_personIcon,rel_clearCache,rel_versionUpdate;
    private RoundedImageView iv_personIcon;
    private TextView tv_cacheSize;
    private View inflate;
    private Button choosePhoto;
    private Button takePhoto;
    private Button btn_can;
    private Dialog dialog;
    private AlertDialog.Builder adb;
    private AlertDialog show;
    private File cacheFile;
    private File glideCache;

    @Override
    protected boolean isSteepStateBar() {
        return false;
    }

    @Override
    protected BasePresent initBasePresent() {
        return null;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_personal;
    }

    @Override
    public void setDayNight() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#03A9F4"));
        rel_personIcon=findViewById(R.id.rel_personIcon);
        rel_clearCache=findViewById(R.id.rel_clearCache);
        rel_versionUpdate=findViewById(R.id.rel_VersionUpdate);
        iv_personIcon=findViewById(R.id.iv_personIcon);
        tv_cacheSize=findViewById(R.id.tv_personCacheSize);

        rel_personIcon.setOnClickListener(this);
        rel_clearCache.setOnClickListener(this);
        rel_versionUpdate.setOnClickListener(this);
    }

    @Override
    public void initData() {
        cacheFile = new File(BaseApp.AppContext.getCacheDir(),"/netCache");
        glideCache = new File(App.AppContext.getCacheDir()+"/glide_cache/");
        System.out.println("cacheFile----------------"+ cacheFile);
        try {
            long folderSize = FileUtils.getFolderSize(cacheFile);
            long folderSize1 = FileUtils.getFolderSize(glideCache);
            String formatSize = FileUtils.getFormatSize(folderSize + folderSize1);
            tv_cacheSize.setText(formatSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.rel_personIcon:
                showUserDialog();
                break;
            case R.id.rel_clearCache:
                Thread thread=new Thread(){
                    @Override
                    public void run() {
                        Glide.get(App.AppContext).clearDiskCache();
                    }
                };
                thread.start();
                Glide.get(App.AppContext).clearMemory();
                Hawk.delete("videoCache");
                Hawk.delete("epiCache");
                if (cacheFile==null){
                    cacheFile = new File(BaseApp.AppContext.getCacheDir(),"/netCache");
                }
                FileUtils.delete(cacheFile);
                Toast.makeText(this, "清除缓存", Toast.LENGTH_SHORT).show();
                tv_cacheSize.setText("0K");
                break;
            case R.id.rel_VersionUpdate:
                Toast.makeText(this, "版本更新", Toast.LENGTH_SHORT).show();
                break;
            case R.id.takePhoto:
                Toast.makeText(this, "修改昵称", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.choosePhoto:
                Toast.makeText(this, "修改头像", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.btn_qu:
                dialog.dismiss();
                break;
        }
    }


    private void showUserDialog() {
        dialog = new Dialog(this,R.style.DialogSelect);
        inflate = LayoutInflater.from(this).inflate(R.layout.cancle, null);
        choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        btn_can = (Button) inflate.findViewById(R.id.btn_qu);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        btn_can.setOnClickListener(this);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 10;
        lp.width=App.screen_width-20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void initIntentAnim() {

    }

    @Override
    public boolean nullis() {
        return false;
    }

    @Override
    public boolean AllowFullScreen() {
        return false;
    }
}
