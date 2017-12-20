package model;

import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import callback.NetCallBack;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;

/**
 * Created by 李英杰 on 2017/12/19.
 * Description：
 */

public interface INotice {
    void notice(String uid, String followId,NetCallBack netCallBack, Context context);
    void getUserVideoInfo(String uid, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject);
}
