package view;

import bwie.com.basemodule.BaseView;

/**
 * Created by 李英杰 on 2017/12/18.
 * Description：
 */

public interface CollectVideoView extends BaseView{
    void collectSuccess(String msg);
    void cancelSuccess(String msg);
    void cancelFail(String msg);
    void pariseSuccess(String msg);
    void commentSuccess(String msg);
    void commentFila(String msg);
}
