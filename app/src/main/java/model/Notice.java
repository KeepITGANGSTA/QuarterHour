package model;

import android.content.Context;

import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.quarterhour.App;
import callback.NetCallBack;
import entity.BaseEntity;
import entity.UserInfoBean;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;

/**
 * Created by 李英杰 on 2017/12/19.
 * Description：
 */

public class Notice implements INotice{

    private Subscription followSubscribe;
    private ProgressSubscriber userSub;

    @Override
    public void notice(String uid, String followId, NetCallBack netCallBack, Context context) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        followSubscribe = apiService.follow(uid, followId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("关注失败---"+e.getMessage());
                        System.out.println("关注失败---"+e.toString());
                        netCallBack.RequestFail(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseEntity<Object> objectBaseEntity) {
                        System.out.println("关注成功---"+objectBaseEntity.msg);
                        netCallBack.RequestSuccess(objectBaseEntity.msg);
                    }
                });
    }

    @Override
    public void getUserVideoInfo(String uid, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        Observable userInfo = apiService.getUserInfo(uid);
        userSub = new ProgressSubscriber<UserInfoBean>(context) {
            @Override
            public void _Next(UserInfoBean o) {
                netCallBack.RequestSuccess(o);
            }

            @Override
            public void _OnError(String msg) {
                netCallBack.RequestFail(msg);
            }
        };
        HttpUtils.getInstace().toSubscribe(userInfo,userSub,"userInfo",ActivityLifeCycleEvent.PAUSE,publishSubject,false,false);
    }

    public void destroy(){
        if (followSubscribe!=null){
            followSubscribe.unsubscribe();
            followSubscribe=null;
        }
        if (userSub!=null){
            userSub.dismissProgressDialog();
            userSub.unsubscribe();
            userSub=null;
        }
    }

}
