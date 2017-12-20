package utils;

import entity.BaseEntity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by 李英杰 on 2017/11/17.
 * Description：
 */

public class RxHelper {

    public static <T> Observable.Transformer<BaseEntity<T>,T> handleResult(final ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject){
        return new Observable.Transformer<BaseEntity<T>,T>(){
            @Override
            public Observable<T> call(Observable<BaseEntity<T>> tObservable) {
                final Observable<ActivityLifeCycleEvent> compareLifecycleObservable=lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
                    @Override
                    public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
                        return activityLifeCycleEvent.equals(event);
                    }
                });
                return tObservable.flatMap(new Func1<BaseEntity<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseEntity<T> result) {
                        System.out.println("-----------------------------请求结果-----------------"+result.msg);
                        if ("0".equals(result.code)){
                            return createData(result.data);
                        } else{
                            int code=Integer.parseInt(result.code);
                            System.out.println("强转code---"+code);
                            return Observable.error(new ApiException(code,result.msg));
                        }
                    }
                }).takeUntil(compareLifecycleObservable).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
















/*                return tObservable.flatMap(new Func1<BaseEntity<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseEntity<T> tBaseEntity) {
                        if (!"success".equals(tBaseEntity.result)){
                            return Observable.error(new ApiException(tBaseEntity.result));
                        }else {
                            return createData(tBaseEntity.member_user);
                        }
                    }
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());*/
            }
        };

    }


    private static <T> Observable<T> createData(final T data){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


}
