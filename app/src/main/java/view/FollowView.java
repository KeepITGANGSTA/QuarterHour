package view;

import bwie.com.basemodule.BaseView;
import entity.UserInfoBean;

/**
 * Created by 李英杰 on 2017/12/19.
 * Description：
 */

public interface FollowView extends BaseView {
    void followSuccess(String msg);
    void followFaile(String msg);

    void getUserSuccess(UserInfoBean o);
    void getUserFail(String msg);
}
