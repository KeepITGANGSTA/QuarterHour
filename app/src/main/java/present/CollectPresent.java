package present;

import android.content.Context;

import bwie.com.basemodule.BasePresent;
import callback.NetCallBack;
import model.ColletVideo;
import view.CollectVideoView;

/**
 * Created by 李英杰 on 2017/12/18.
 * Description：
 */

public class CollectPresent extends BasePresent<CollectVideoView> {

    private ColletVideo collectVideo;

    public CollectPresent(CollectVideoView view) {
        super(view);
        collectVideo=new ColletVideo();
    }

    public void collectVideo(String uid, String wid, Context context){
        collectVideo.collectVideo(uid, wid, new NetCallBack<Object>() {
            @Override
            public void RequestSuccess(Object msg) {
                view.collectSuccess("收藏成功");
            }

            @Override
            public void RequestFail(String msg) {
                view.Fail(msg);
            }
        },context);
    }

    public void cancelVideoCollect(String uid, String wid, Context context){
        collectVideo.collectVideo(uid, wid, new NetCallBack<Object>() {
            @Override
            public void RequestSuccess(Object msg) {
                view.cancelSuccess("取消收藏成功");
            }

            @Override
            public void RequestFail(String msg) {
                view.cancelFail(msg);
            }
        },context);
    }

    public void pariseVideo(String uid, String wid, Context context){
        collectVideo.pariseVideo(uid, wid, new NetCallBack<Object>() {
            @Override
            public void RequestSuccess(Object msg) {
                view.pariseSuccess("点赞成功成功");
            }

            @Override
            public void RequestFail(String msg) {
                view.Fail(msg);
            }
        },context);
    }

    public void commentVideo(String uid, String wid,String content, Context context){
        collectVideo.commentsVideo(uid, wid,content, new NetCallBack<Object>() {
            @Override
            public void RequestSuccess(Object msg) {
                view.commentSuccess("评论成功");
            }

            @Override
            public void RequestFail(String msg) {
                view.commentFila(msg);
            }
        },context);
    }

    @Override
    public void destory() {
        super.destory();
        collectVideo.destroy();
        view=null;
    }
}