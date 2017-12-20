package model;

import android.content.Context;

import callback.NetCallBack;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;

/**
 * Created by 李英杰 on 2017/12/18.
 * Description：
 */

public interface ICollectVideo {
    void collectVideo(String uid, String wid, NetCallBack netCallBack, Context context);
    void pariseVideo(String uid, String wid, NetCallBack netCallBack, Context context);
    void cancelVideoCollect(String uid, String wid, NetCallBack netCallBack, Context context);
    void commentsVideo(String uid,String wid,String content,NetCallBack netCallBack,Context context);
}
