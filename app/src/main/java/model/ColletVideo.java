package model;

import android.content.Context;

import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.quarterhour.App;
import callback.NetCallBack;
import entity.BaseEntity;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李英杰 on 2017/12/18.
 * Description：
 */

public class ColletVideo implements ICollectVideo {

    private Subscription collectSubscribe,cancelCollectSub,commentSub;


    @Override
    public void collectVideo(String uid, String wid, NetCallBack netCallBack, Context context) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        collectSubscribe = apiService.collectVideo(uid, wid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        netCallBack.RequestFail(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseEntity<Object> objectBaseEntity) {
                        netCallBack.RequestSuccess(objectBaseEntity.msg);
                    }
                });
    }

    @Override
    public void pariseVideo(String uid, String wid, NetCallBack netCallBack, Context context) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        cancelCollectSub = apiService.pariseVideo(uid, wid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        netCallBack.RequestFail(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseEntity<Object> objectBaseEntity) {
                        netCallBack.RequestSuccess(objectBaseEntity.msg);
                    }
                });
    }

    @Override
    public void cancelVideoCollect(String uid, String wid, NetCallBack netCallBack, Context context) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        cancelCollectSub = apiService.cancelVideoCollect(uid, wid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        netCallBack.RequestFail(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseEntity<Object> objectBaseEntity) {
                        netCallBack.RequestSuccess(objectBaseEntity.msg);
                    }
                });
    }

    @Override
    public void commentsVideo(String uid, String wid,String content, NetCallBack netCallBack, Context context) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        commentSub = apiService.commentsVideo(uid, wid,content).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        netCallBack.RequestFail(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseEntity<Object> objectBaseEntity) {
                        netCallBack.RequestSuccess(objectBaseEntity.msg);
                    }
                });
    }

    public void destroy(){
        if (collectSubscribe!=null){
            collectSubscribe.unsubscribe();
            collectSubscribe=null;
        }
        if (cancelCollectSub!=null){
            cancelCollectSub.unsubscribe();
            cancelCollectSub=null;
        }
        if (commentSub!=null){
            commentSub.unsubscribe();
            commentSub=null;
        }
    }
}
