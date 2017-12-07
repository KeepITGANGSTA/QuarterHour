package model;

import android.app.Application;
import android.content.Context;

import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.quarterhour.App;
import callback.NetCallBack;
import entity.BaseEntity;
import entity.UserInfo;
import rx.Observable;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;

/**
 * Created by 李英杰 on 2017/11/30.
 * Description：
 */

public class LoginModel implements ILoginModel {

    private ProgressSubscriber progressSubscriber;
    private ProgressSubscriber psb;

    @Override
    public void loginModel(String phone, String password, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, context.getApplicationContext()).getApiService(Api.class);
        Observable login = apiService.login(phone, password);
        progressSubscriber = new ProgressSubscriber<UserInfo>(context) {
            @Override
            public void _Next(UserInfo o) {
                if (netCallBack!=null){
                    netCallBack.RequestSuccess(o);
                }
            }

            @Override
            public void _OnError(String msg) {
                if (netCallBack!=null){
                    netCallBack.RequestFail(msg);
                }
            }
        };
        HttpUtils.getInstace().toSubscribe(login,progressSubscriber ,"loginCache",ActivityLifeCycleEvent.PAUSE,publishSubject,false,false);
    }

    @Override
    public void resUserModel(String phone, String password, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject) {
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, context.getApplicationContext()).getApiService(Api.class);
        Observable baseEntityObservable = apiService.resUser(phone, password);
        psb = new ProgressSubscriber<UserInfo>(context) {
            @Override
            public void _Next(UserInfo o) {
                if (netCallBack!=null){
                    netCallBack.RequestSuccess(o);
                }
            }

            @Override
            public void _OnError(String msg) {
                if (netCallBack!=null){
                    netCallBack.RequestFail(msg);
                }
            }
        };
        HttpUtils.getInstace().toSubscribe(baseEntityObservable,psb ,"resCache",ActivityLifeCycleEvent.PAUSE,publishSubject,false,false);
    }

    public void lmdestroy(){
        if (progressSubscriber!=null){
            progressSubscriber.dismissProgressDialog();
            if (progressSubscriber.isUnsubscribed()){
                progressSubscriber.unsubscribe();
            }
            progressSubscriber=null;
        }
        if (psb!=null){
            psb.dismissProgressDialog();
            if (psb.isUnsubscribed()){
                psb.unsubscribe();
            }
            psb=null;
        }

    }

}
