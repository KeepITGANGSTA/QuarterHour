package utils;

import entity.BaseEntity;
import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

/**
 * Created by 李英杰 on 2017/11/18.
 * Description：
 */

public class HttpUtils {
    private HttpUtils(){}
    private static class SingletonHolder{
        private static final HttpUtils INSTANCE=new HttpUtils();
    }

    public static HttpUtils getInstace(){
        return SingletonHolder.INSTANCE;
    }


    public void toSubscribe(Observable ob, final ProgressSubscriber subscriber, String cacheKey, ActivityLifeCycleEvent event, PublishSubject<ActivityLifeCycleEvent> publishSubject, boolean isCache, boolean forceRefresh){
        Observable.Transformer<BaseEntity<Object>,Object> result= RxHelper.handleResult(event,publishSubject);
        Observable observable1=ob.compose(result)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        subscriber.showProgressDialog();
                    }
                });
        RetrofitCache.load(cacheKey,observable1,isCache,forceRefresh).subscribe(subscriber);
    }

}
