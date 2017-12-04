package model;

import android.content.Context;

import callback.NetCallBack;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;

/**
 * Created by 李英杰 on 2017/11/30.
 * Description：
 */

public interface ILoginModel {
    void loginModel(String phone, String password, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject);
    void resUserModel(String phone, String password, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject);
}
