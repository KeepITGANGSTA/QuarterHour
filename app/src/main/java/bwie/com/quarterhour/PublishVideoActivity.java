package bwie.com.quarterhour;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.List;

import adapter.FullyGridLayoutManager;
import adapter.GridImageAdapter;
import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BasePresent;
import bwie.com.basemodule.SharedPreferencesUtil;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import present.PublishPublish;
import view.PublishView;

public class PublishVideoActivity extends BaseActivity<PublishPublish> implements PublishView,GridImageAdapter.onAddPicClickListener{

    private TextView tv_back,tv_publish;
    private EditText ed_content;
    private RecyclerView recyclerView;
    private GridImageAdapter gridImageAdapter;
    private FullyGridLayoutManager manager;
    private AlertDialog show;
    private AlertDialog.Builder adb;
    private AlertDialog.Builder publishFail;
    private AlertDialog failshow;

    @Override
    protected PublishPublish initBasePresent() {
        return new PublishPublish(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_publish_video;
    }

    @Override
    public void setDayNight() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Window window=this.getWindow();
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.parseColor("#03A9F4"));
        tv_back=findViewById(R.id.tv_creation_back);
        tv_publish=findViewById(R.id.tv_creation_publish);
        ed_content=findViewById(R.id.ed_creation_content);
        recyclerView=findViewById(R.id.recy_pubImg);

        tv_back.setOnClickListener(this);
        tv_publish.setOnClickListener(this);
        manager = new FullyGridLayoutManager(PublishVideoActivity.this, 3, GridLayoutManager.VERTICAL,false);
        gridImageAdapter = new GridImageAdapter(PublishVideoActivity.this,this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(gridImageAdapter);
        gridImageAdapter.setOnItemClickListener((position, v) -> PictureSelector.create(PublishVideoActivity.this).externalPicturePreview(position, list));
    }

    @Override
    public void initData() {

    }

    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.tv_creation_back:
                finishAfterTransition();
                break;
            case R.id.tv_creation_publish:
                String epi_content=ed_content.getText().toString();
                if (TextUtils.isEmpty(epi_content)){
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (list!=null && list.size()!=0){
                    Toast.makeText(this, "集合长度："+list.size(), Toast.LENGTH_SHORT).show();
                    String path = list.get(0).getPath();
                    System.out.println("path---------------"+path);
                    basePresent.publishEpi(SharedPreferencesUtil.getPreferencesValue("uid"),epi_content,list,this,lifecycleSubject);
                }
                if (list==null || list.size()==0){
                    Toast.makeText(this, "集合长度："+list, Toast.LENGTH_SHORT).show();
                    basePresent.publishEpi(SharedPreferencesUtil.getPreferencesValue("uid"),epi_content,null,this,lifecycleSubject);
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(PublishVideoActivity.this,requestCode,permissions,grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething(){
                PictureSelector.create(PublishVideoActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .isGif(true)
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
        System.out.println("返回1");
        if (resultCode==RESULT_OK){
            System.out.println("返回2");
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    System.out.println("返回3");
                    if (list!=null){
                        list.clear();
                    }
                    list= PictureSelector.obtainMultipleResult(data);
                    gridImageAdapter.setList(list);
                    gridImageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }



    @Override
    protected boolean isSteepStateBar() {
        return false;
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
        return false;
    }

    @Override
    public void ShowLoading() {

    }

    @Override
    public void HideLoading() {

    }

    @Override
    public void Fail(String msg) {
        publishFail = new AlertDialog.Builder(PublishVideoActivity.this);
        publishFail.setView(R.layout.publish_fail);
        publishFail.setPositiveButton("重新发送", (dialog, which) -> failshow.dismiss());
        publishFail.create();
        failshow = publishFail.show();
    }

    @Override
    public void publishSuccess(String msg) {
        adb = new AlertDialog.Builder(PublishVideoActivity.this);
        adb.setView(R.layout.publish_success);
        adb.setPositiveButton("去看看~", (dialog, which) -> finishAfterTransition());
        adb.setNegativeButton("再发一个~", (dialog, which) -> show.dismiss());
        adb.create();
        show = adb.show();
    }

    @Override
    public void BaseDestroy() {
        super.BaseDestroy();
        if (list!=null){
            list.clear();
            list=null;
        }
        gridImageAdapter=null;
        PictureFileUtils.deleteCacheDirFile(PublishVideoActivity.this);
        if (adb!=null){
            if (show!=null){
                if (show.isShowing()){
                    show.dismiss();
                    show=null;
                }
            }
            adb=null;
        }
        if (manager!=null){
            manager=null;
        }
        if (gridImageAdapter!=null){
            gridImageAdapter.destroy();
            gridImageAdapter=null;
        }
        if (publishFail!=null){
            if (failshow.isShowing()){
                failshow.dismiss();
                failshow=null;
            }
            publishFail=null;
        }
    }

    @Override
    public void publishVideoSuccess(String msg) {

    }



    @Override
    public void onAddPicClick() {
        PermissionGen.with(PublishVideoActivity.this)
                .addRequestCode(100)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .request();
    }
}
