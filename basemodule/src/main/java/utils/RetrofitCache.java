package utils;

import com.orhanobut.hawk.Hawk;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 李英杰 on 2017/11/18.
 * Description：
 */

public class RetrofitCache {

    public static <T> Observable<T> load(final String cacheKey, Observable<T> fromNetWork, boolean isCache, final boolean forceRefresh){

        Observable<T> fromCache = Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                T cache = (T) Hawk.get(cacheKey);
                if (cache != null) {
                    subscriber.onNext(cache);
                } else {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        if (isCache){
            fromNetWork=fromNetWork.map(new Func1<T, T>() {
                @Override
                public T call(T t) {
                    boolean put = Hawk.put(cacheKey, t);
                    return t;
                }
            });
        }
        if (forceRefresh){
            return fromNetWork;
        }else {
            return Observable.concat(fromCache,fromNetWork).first();
        }
    }
}
