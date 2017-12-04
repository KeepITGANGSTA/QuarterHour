package view;

import bwie.com.basemodule.BaseView;
import entity.UserInfo;

/**
 * Created by 李英杰 on 2017/11/30.
 * Description：
 */

public interface LoginView extends BaseView{
    void LoginSuccess(UserInfo userInfo);
    void ResSuccess(String msg);
}
