package model;

import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import callback.NetCallBack;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;

/**
 * Created by 李英杰 on 2017/12/4.
 * Description：
 */

public interface IPublishEpi {
    void publishEpi(String uid, String content, List<LocalMedia> list, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject);
    void publishVideoi(String uid,String VideoDesc,List<LocalMedia> list, NetCallBack netCallBack, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject);
}
