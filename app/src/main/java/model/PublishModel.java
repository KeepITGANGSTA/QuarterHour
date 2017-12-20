package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.widget.Toast;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.quarterhour.App;
import callback.NetCallBack;
import entity.BaseEntity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;

/**
 * Created by 李英杰 on 2017/12/4.
 * Description：
 */

public class PublishModel implements IPublishEpi {

    private ProgressSubscriber progressSubscriber;
    private MultipartBody.Builder builder;
    private MultipartBody.Builder Videobuilder;
    private List<MultipartBody.Part> parts;
    private List<MultipartBody.Part> Videoparts;
    private FileOutputStream fileOutputStream1;
    private BufferedOutputStream bos;
    private File file;
    private RequestBody requestBody;
    private MediaMetadataRetriever media;
    private Bitmap frameAtTime;
    private File file1;
    private File file11;
    private RequestBody coverIv;
    private ProgressSubscriber videoProgress;
    @Override
    public void publishEpi(String uid, String content, List<LocalMedia> imgPath, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("uid",uid);
        builder.addFormDataPart("content",content);
        if (imgPath!=null){
            for (LocalMedia s : imgPath) {
                file1 = new File(s.getPath());
                RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                builder.addFormDataPart("jokeFiles", file1.getName(),requestBody);
            }
        }
        parts = builder.build().parts();
        Observable baseEntityObservable = apiService.publishEpi(parts);
        progressSubscriber = new ProgressSubscriber<Object>(context) {
            @Override
            public void _Next(Object o) {
                netCallBack.RequestSuccess("发布成功");
                Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
                destroy();
            }

            @Override
            public void _OnError(String msg) {
                destroy();
                netCallBack.RequestFail(msg);
                Toast.makeText(context, "发布失败："+msg, Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtils.getInstace().toSubscribe(baseEntityObservable,progressSubscriber,"publishEpi",ActivityLifeCycleEvent.STOP,publishSubject,false,false);
    }

    @Override
    public void publishVideoi(String uid,String VideoDesc, List<LocalMedia> list, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject) {
        getCoverImg(context,list.get(0));
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        Videobuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Videobuilder.addFormDataPart("uid",uid);
        for (LocalMedia localMedia : list) {
                System.out.println("视频路径---"+localMedia.getPath());
                file = new File(localMedia.getPath());
                System.out.println("视频文件是否存在---======="+file.exists());
                requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                Videobuilder.addFormDataPart("videoFile", file.getName(), requestBody);
        }

        coverIv = RequestBody.create(MediaType.parse("multipart/form-data"), file11);
        Videobuilder.addFormDataPart("coverFile",file11.getName(), coverIv);
        if (!TextUtils.isEmpty(VideoDesc)){
            Videobuilder.addFormDataPart("videoDesc",VideoDesc);
        }
        Videobuilder.addFormDataPart("latitude","");
        Videobuilder.addFormDataPart("longitude","");
        Videoparts=Videobuilder.build().parts();
        videoProgress = new ProgressSubscriber<Object>(context) {
            @Override
            public void _Next(Object o) {
                netCallBack.RequestSuccess("发布视频成功");
                destroy();
            }

            @Override
            public void _OnError(String msg) {
                destroy();
                netCallBack.RequestFail(msg);
            }
        };
        Observable baseEntityObservable = apiService.publishVideo(Videoparts);
        HttpUtils.getInstace().toSubscribe(baseEntityObservable,videoProgress,"publi_video",ActivityLifeCycleEvent.DESTROY,publishSubject,false,false);
    }

    private void getCoverImg(Context context,LocalMedia localMedia){
        try {
            media = new MediaMetadataRetriever();
            media.setDataSource(localMedia.getPath());
            frameAtTime = media.getFrameAtTime();
            file11 = new File(context.getCacheDir()+"/cover.jpg");
            if (!file11.exists()){
                file11.createNewFile();
            }

            System.out.println("封面图文件是否存在----"+file11.exists()+"----路径---"+file11.getAbsolutePath());
            System.out.println("封面图---"+frameAtTime);
            fileOutputStream1 = new FileOutputStream(file11);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream1);
            frameAtTime.compress(Bitmap.CompressFormat.JPEG,100,bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void destroy(){
        if (parts!=null){
            parts=null;
        }
        if (progressSubscriber!=null){
            progressSubscriber.dismissProgressDialog();
            progressSubscriber.unsubscribe();
            progressSubscriber=null;
        }
        if (videoProgress!=null){
            videoProgress.dismissProgressDialog();
                videoProgress.unsubscribe();
                videoProgress=null;
        }
        if (builder!=null){
            builder=null;
        }
        if (Videobuilder!=null){
            Videobuilder=null;
        }
        if (frameAtTime!=null){
            frameAtTime=null;
        }
        if (requestBody!=null){
            requestBody=null;
        }
        if (file!=null){
            file.delete();
        }
        if (file1!=null){
            file1.delete();
        }
        if (file11!=null){
            if (file11.exists()){
                file11.delete();
            }
        }
        if (coverIv!=null){
            coverIv=null;
        }
        if (media!=null){
                try {
                    fileOutputStream1.close();
                    media.release();
                    media=null;
                    if (frameAtTime!=null){
                        frameAtTime=null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

//    private void saveImg(Bitmap bitmap) {
//        File file=new File(getFilesDir()+"/shang.jpg");
//        try {
//            FileOutputStream fileOutputStream=new FileOutputStream(file);
//            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bufferedOutputStream);
//            bufferedOutputStream.flush();
//            bufferedOutputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
