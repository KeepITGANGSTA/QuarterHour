package present;

import android.content.Context;

import bwie.com.basemodule.BasePresent;
import callback.NetCallBack;
import entity.UserInfo;
import model.LoginModel;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import view.LoginView;

/**
 * Created by 李英杰 on 2017/11/30.
 * Description：
 */

public class LoginPresent extends BasePresent<LoginView> {

    private LoginModel loginModel;

    public LoginPresent(LoginView view) {
        super(view);
        loginModel=new LoginModel();
    }

    public void login(String phone, String password, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject){
        loginModel.loginModel(phone, password, new NetCallBack<UserInfo>() {
            @Override
            public void RequestSuccess(UserInfo msg) {
                view.LoginSuccess(msg);
            }

            @Override
            public void RequestFail(String msg) {
                view.Fail(msg);
            }
        },context,publishSubject);
    }

    public void res(String phone, String password, Context context, PublishSubject<ActivityLifeCycleEvent> publishSubject){
        loginModel.resUserModel(phone, password, new NetCallBack<UserInfo>() {
            @Override
            public void RequestSuccess(UserInfo msg) {
                view.ResSuccess("注册成功");
            }

            @Override
            public void RequestFail(String msg) {
                view.Fail(msg);
            }
        },context,publishSubject);
    }

    @Override
    public void destory() {
        super.destory();
        loginModel.lmdestroy();
    }
}
