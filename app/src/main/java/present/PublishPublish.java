package present;

import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import bwie.com.basemodule.BasePresent;
import callback.NetCallBack;
import model.PublishModel;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import view.PublishView;

/**
 * Created by 李英杰 on 2017/12/4.
 * Description：
 */

public class PublishPublish extends BasePresent<PublishView> {

    private PublishModel publishModel;

    public PublishPublish(PublishView view) {
        super(view);
        publishModel=new PublishModel();
    }

    public void publishEpi(String uid, String content, List<LocalMedia> imgPath, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject){
        publishModel.publishEpi(uid, content, imgPath, new NetCallBack<Object>() {
            @Override
            public void RequestSuccess(Object msg) {
                view.publishSuccess("发布成功");
            }

            @Override
            public void RequestFail(String msg) {
                view.Fail(msg);
            }
        },context,publishSubject);
    }

    public void publishVideo(String uid, String content, List<LocalMedia> imgPath, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject){
        publishModel.publishVideoi(uid, content, imgPath, new NetCallBack<Object>() {
            @Override
            public void RequestSuccess(Object msg) {
                view.publishVideoSuccess("发布视频成功");
            }

            @Override
            public void RequestFail(String msg) {
                view.Fail(msg);
            }
        },context,publishSubject);
    }

    @Override
    public void destory() {
        super.destory();
        publishModel.destroy();
    }
}
