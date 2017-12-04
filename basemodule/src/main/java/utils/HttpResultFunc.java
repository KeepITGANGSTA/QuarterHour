package utils;

import entity.BaseEntity;
import rx.functions.Func1;

/**
 * Created by 李英杰 on 2017/11/17.
 * Description：
 */

public class HttpResultFunc<T> implements Func1<BaseEntity<T>,T> {
    @Override
    public T call(BaseEntity<T> tBaseEntity) {
        System.out.println("HttpResultFunc");
        if (!"0".equals(tBaseEntity.code)){
            System.out.println("失败----"+tBaseEntity.msg);
        }
        return tBaseEntity.data;
    }
}
