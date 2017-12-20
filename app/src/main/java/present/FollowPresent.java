package present;

import android.content.Context;

import bwie.com.basemodule.BasePresent;
import callback.NetCallBack;
import entity.UserInfoBean;
import model.Notice;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import view.FollowView;

/**
 * Created by 李英杰 on 2017/12/19.
 * Description：
 */

public class FollowPresent extends BasePresent<FollowView> {

    private Notice notice;

    public FollowPresent(FollowView view) {
        super(view);
        notice=new Notice();
    }

    public void follow(String uid, String followId, Context context ){
        notice.notice(uid, followId, new NetCallBack<Object>() {
            @Override
            public void RequestSuccess(Object msg) {
                view.followSuccess("关注成功");
            }

            @Override
            public void RequestFail(String msg) {
                view.followFaile(msg);
            }
        },context);
    }


    public void getUserInfo(String uid, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject){
        notice.getUserVideoInfo(uid, new NetCallBack<UserInfoBean>() {
            @Override
            public void RequestSuccess(UserInfoBean msg) {
                view.getUserSuccess(msg);
            }

            @Override
            public void RequestFail(String msg) {
                view.getUserFail(msg);
            }
        },context,publishSubject);
    }



}
