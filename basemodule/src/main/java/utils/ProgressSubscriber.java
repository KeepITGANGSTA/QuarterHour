package utils;

import android.content.Context;

import rx.Subscriber;

/**
 * Created by 李英杰 on 2017/11/18.
 * Description：
 */

public abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SimpleLoadDialog dialogHandler;

    public ProgressSubscriber(Context context){
        dialogHandler=new SimpleLoadDialog(context,this,true);
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (false){
            _OnError("网络不可用");
        }else if (e instanceof ApiException){
            _OnError(e.getMessage());
        }else {
            _OnError("请求失败");
        }
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        _Next(t);
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()){
            this.unsubscribe();
        }
    }

    public void dismissProgressDialog(){
        if (dialogHandler!=null){
            dialogHandler.obtainMessage(SimpleLoadDialog.DISMISS_PROGRESS_DIALOG).sendToTarget();
            dialogHandler=null;
        }
    }

    public void showProgressDialog(){
        if (dialogHandler!=null){
            dialogHandler.obtainMessage(SimpleLoadDialog.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }


    public abstract void _Next(T t);
    public abstract void _OnError(String msg);

}
