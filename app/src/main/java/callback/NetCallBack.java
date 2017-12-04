package callback;

/**
 * Created by 李英杰 on 2017/11/28.
 * Description：
 */

public interface NetCallBack<T> {
    void RequestSuccess(T msg);
    void RequestFail(String msg);
}
