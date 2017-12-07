package view;

import bwie.com.basemodule.BaseView;

/**
 * Created by 李英杰 on 2017/12/4.
 * Description：
 */

public interface PublishView extends BaseView{
    void publishSuccess(String msg);
    void publishVideoSuccess(String msg);
}
