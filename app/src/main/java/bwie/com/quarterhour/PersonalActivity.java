package bwie.com.quarterhour;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.hawk.Hawk;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.util.List;

import api.Api;
import api.Common;
import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BaseApp;
import bwie.com.basemodule.BasePresent;
import bwie.com.basemodule.FileUtils;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.basemodule.SharedPreferencesUtil;
import cn.jzvd.JZMediaManager;
import entity.BaseEntity;
import entity.UserInfo;
import glide.GlideCache;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;

public class PersonalActivity extends BaseActivity {

    private AutoRelativeLayout rel_personIcon,rel_clearCache,rel_versionUpdate;
    private RoundedImageView iv_personIcon;
    private TextView tv_cacheSize,tv_personNick;
    private View inflate;
    private Button choosePhoto;
    private Button takePhoto;
    private Button btn_can;
    private Dialog dialog;
    private File cacheFile;
    private File glideCache;
    private UserInfo userInfo;
    private ProgressSubscriber iconSub;
    private Api apiService;
    private Subscription uid;

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
        apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#03A9F4"));
        rel_personIcon=findViewById(R.id.rel_personIcon);
        rel_clearCache=findViewById(R.id.rel_clearCache);
        rel_versionUpdate=findViewById(R.id.rel_VersionUpdate);
        iv_personIcon=findViewById(R.id.iv_personIcon);
        tv_cacheSize=findViewById(R.id.tv_personCacheSize);
        tv_personNick=findViewById(R.id.tv_personNickName);

        rel_personIcon.setOnClickListener(this);
        rel_clearCache.setOnClickListener(this);
        rel_versionUpdate.setOnClickListener(this);
    }

    @Override
    public void initData() {
        userInfo = Hawk.get("UserInfo");
        if (userInfo !=null){
            tv_personNick.setText(userInfo.nickname);
            Glide.with(this).applyDefaultRequestOptions(GlideCache.NoMemoryDiskCache()).load(userInfo.icon).into(iv_personIcon);
        }
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
                PermissionGen.with(PersonalActivity.this)
                        .addRequestCode(100)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA)
                        .request();
                dialog.dismiss();
                break;
            case R.id.btn_qu:
                Hawk.delete("UserInfo");
                SharedPreferencesUtil.clearPreferences("uid");
                SharedPreferencesUtil.clearPreferences("token");
                iv_personIcon.setImageResource(R.drawable.lefticon);
                tv_personNick.setText("尚未登录");
                dialog.dismiss();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(PersonalActivity.this,requestCode,permissions,grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething(){
        PictureSelector.create(PersonalActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .sizeMultiplier(0.5f)
                .previewEggs(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    @PermissionFail(requestCode = 100)
    public void doFailSomething(){
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> list= PictureSelector.obtainMultipleResult(data);
                    File icon=new File(list.get(0).getPath());
                    updateIcon(icon);
                    break;
            }
        }
    }

    private void updateIcon(File icon) {

        if (userInfo!=null){
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            RequestBody body=RequestBody.create(MediaType.parse("multipart/form-data"),icon);
            builder.addFormDataPart("uid",userInfo.uid+"");
            builder.addFormDataPart("file",icon.getName(),body);
            Observable baseEntityObservable = apiService.updateIcon(builder.build().parts());
            iconSub = new ProgressSubscriber<Object>(this) {
                @Override
                public void _Next(Object o) {
                    Toast.makeText(App.AppContext, "上传成功", Toast.LENGTH_SHORT).show();
                    refreshUserInfo();
                }

                @Override
                public void _OnError(String msg) {
                    Toast.makeText(App.AppContext, "上传失败", Toast.LENGTH_SHORT).show();
                }
            };
            HttpUtils.getInstace().toSubscribe(baseEntityObservable,iconSub,"iconCache", ActivityLifeCycleEvent.PAUSE,lifecycleSubject,false,false);
        }

    }

    private void refreshUserInfo() {
        uid = apiService.UserInfo(SharedPreferencesUtil.getPreferencesValue("uid")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<UserInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseEntity<UserInfo> userInfoBaseEntity) {
                        Hawk.delete("UserInfo");
                        Hawk.put("UserInfo",userInfoBaseEntity.data);
                        userInfo=null;
                        userInfo=Hawk.get("UserInfo");
                        Glide.with(PersonalActivity.this).applyDefaultRequestOptions(GlideCache.NoMemoryDiskCache()).load(userInfo.icon).into(iv_personIcon);
                    }
                });
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

    @Override
    public void BaseDestroy() {
        super.BaseDestroy();
        if (uid!=null){
            uid.unsubscribe();
            uid=null;
        }
        if (dialog!=null) {
            dialog.dismiss();
            dialog.cancel();
            dialog = null;
        }
        if (inflate!=null){
            inflate=null;
        }
        if (iconSub!=null){
            iconSub.dismissProgressDialog();
            iconSub.unsubscribe();
            iconSub=null;
        }
        if (cacheFile!=null){
            cacheFile=null;
            glideCache=null;
        }


    }
}
