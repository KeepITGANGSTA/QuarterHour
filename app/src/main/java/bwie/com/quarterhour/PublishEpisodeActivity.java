package bwie.com.quarterhour;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.SharedPreferencesUtil;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import present.PublishPublish;
import view.PublishView;

public class PublishEpisodeActivity extends BaseActivity<PublishPublish> implements PublishView{


    private AutoRelativeLayout rel_video;
    private ImageView iv_gifVideo;
    private TextView tv_video_back,tv_video_publish;
    private Timer timer;
    private TimerTask tt;
    private LinearLayout linearLayout;
    private EditText ed_videoDesc;
    private AlertDialog show;
    private AlertDialog.Builder adb;
    private AlertDialog.Builder publishFail;
    private AlertDialog failshow;

    @Override
    protected boolean isSteepStateBar() {
        return false;
    }

    @Override
    protected PublishPublish initBasePresent() {
        return new PublishPublish(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_publish_episode;
    }
    @Override
    public void setDayNight() {
    }
    @Override
    public void initView(Bundle savedInstanceState) {
        rel_video=findViewById(R.id.rel_video);
        iv_gifVideo=findViewById(R.id.iv_gifVideo);
        linearLayout=findViewById(R.id.lin_ppppp);
        tv_video_back=findViewById(R.id.tv_video_back);
        tv_video_publish=findViewById(R.id.tv_video_publish);
        ed_videoDesc=findViewById(R.id.ed_videoDesc);
        tv_video_publish.setOnClickListener(this);
        tv_video_back.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        iv_gifVideo.setOnClickListener(this);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(App.screen_width,App.screen_height/7*4);
        rel_video.setLayoutParams(ll);
    }
    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public void initData() {
        Glide.with(App.AppContext).asGif().load(R.drawable.video_gif).into(iv_gifVideo);
        linearLayout.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP==event.getAction()){
                PermissionGen.with(PublishEpisodeActivity.this)
                        .addRequestCode(100)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA)
                        .request();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(PublishEpisodeActivity.this,requestCode,permissions,grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething(){
        PictureSelector.create(PublishEpisodeActivity.this)
                .openGallery(PictureMimeType.ofVideo())
                .maxSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.SINGLE)
                .previewVideo(true)
                .isCamera(true)
                .sizeMultiplier(0.5f)
                .previewEggs(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    @PermissionFail(requestCode = 100)
    public void doFailSomething(){
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }


    private List<LocalMedia> list;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            System.out.println("返回2");
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    System.out.println("返回3");
                    if (list!=null){
                        list.clear();
                    }
                    list= PictureSelector.obtainMultipleResult(data);
                    initVideo(list);
                    break;
            }
        }
    }

    private void initVideo(List<LocalMedia> list) {
        LocalMedia media = list.get(0);
        Glide.with(App.AppContext)
                    .load(media.getPath())
                    .into(iv_gifVideo);

    }
    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.tv_video_back:
                Intent intent=new Intent(PublishEpisodeActivity.this,PublishEpiActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(PublishEpisodeActivity.this).toBundle());
                timer=new Timer();
                tt=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
               timer.schedule(tt,1000);
                break;
            case R.id.lin_ppppp:
                break;
            case R.id.iv_gifVideo:
                if (list!=null &&  list.size()!=0){
                    PictureSelector.create(PublishEpisodeActivity.this).externalPictureVideo(list.get(0).getPath());
                }
                break;
            case R.id.tv_video_publish:
                if (list!=null && list.size()!=0){
                    String s = ed_videoDesc.getText().toString();
                    if (TextUtils.isEmpty(s)){
                        basePresent.publishVideo(SharedPreferencesUtil.getPreferencesValue("uid"),"",list,PublishEpisodeActivity.this,lifecycleSubject);
                    }else {
                        basePresent.publishVideo(SharedPreferencesUtil.getPreferencesValue("uid"),s,list,PublishEpisodeActivity.this,lifecycleSubject);
                    }
                }else {
                    Toast.makeText(this, "先录制一个视频吧~", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }



    @Override
    public void initIntentAnim() {

    }

    @Override
    public boolean nullis() {
        return true;
    }

    @Override
    public boolean AllowFullScreen() {
        return true;
    }

    @Override
    public void ShowLoading() {

    }

    @Override
    public void HideLoading() {

    }

    @Override
    public void Fail(String msg) {
        Toast.makeText(this, "发布视频失败："+msg, Toast.LENGTH_SHORT).show();
        publishFail = new AlertDialog.Builder(PublishEpisodeActivity.this);
        publishFail.setView(R.layout.publish_fail);
        publishFail.setPositiveButton("重新发送", (dialog, which) -> failshow.dismiss());
        publishFail.create();
        failshow = publishFail.show();
    }

    @Override
    public void publishSuccess(String msg) {

    }

    @Override
    public void publishVideoSuccess(String msg) {
        adb = new AlertDialog.Builder(PublishEpisodeActivity.this);
        adb.setView(R.layout.publish_success);
        adb.setPositiveButton("去看看~", (dialog, which) -> finishAfterTransition());
        adb.setNegativeButton("再发一个~", (dialog, which) -> show.dismiss());
        adb.create();
        show = adb.show();
    }

    @Override
    public void BaseDestroy() {
        super.BaseDestroy();
        if (timer!=null){
            timer.cancel();
            tt.cancel();
            timer=null;
            tt=null;
        }
        if (adb!=null){
            if (show!=null){
                if (show.isShowing()){
                    show.dismiss();
                    show=null;
                }
            }
            adb=null;
        }
        if (list!=null){
            list.clear();
            list=null;
        }
        if (publishFail!=null){
            if (failshow.isShowing()){
                failshow.dismiss();
                failshow=null;
            }
            publishFail=null;
        }
    }
}
