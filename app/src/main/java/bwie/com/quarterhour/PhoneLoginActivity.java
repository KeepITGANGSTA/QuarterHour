package bwie.com.quarterhour;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.util.Timer;
import java.util.TimerTask;

import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BasePresent;
import bwie.com.basemodule.SharedPreferencesUtil;
import entity.UserInfo;
import present.LoginPresent;
import view.LoginView;

public class PhoneLoginActivity extends BaseActivity<LoginPresent> implements LoginView{

    private TextView tv_resUser;
    private EditText ed_login_phone;
    private EditText ed_login_psd;
    private Button btn_login;
    private TextView tv_forgetPsd;
    private TextView tv_loginNoLogin;

    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected LoginPresent initBasePresent() {
        return new LoginPresent(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_phone_login;
    }

    @Override
    public void setDayNight() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tv_resUser=findViewById(R.id.tv_resUser);
        ed_login_phone=findViewById(R.id.ed_loginPhone);
        ed_login_psd=findViewById(R.id.ed_loginPsd);
        btn_login=findViewById(R.id.btn_Login);
        tv_forgetPsd=findViewById(R.id.tv_forgotPsd);
        tv_loginNoLogin=findViewById(R.id.tv_loginNoLogin);

        tv_resUser.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_forgetPsd.setOnClickListener(this);
        tv_loginNoLogin.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.tv_resUser:
                Intent intent=new Intent(PhoneLoginActivity.this,ResActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(PhoneLoginActivity.this).toBundle());
                timer=new Timer();
                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
                timer.schedule(timerTask,1000);
                break;
            case R.id.btn_Login:
                String phone=ed_login_phone.getText().toString();
                String psd=ed_login_psd.getText().toString();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(psd)){
                    Toast.makeText(this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length()!=11 ){
                    Toast.makeText(this, "账号格式有误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( psd.length()!=6){
                    Toast.makeText(this, "请输入6位数密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                basePresent.login(phone,psd,this,lifecycleSubject);
                break;
            case R.id.tv_forgotPsd:
                Intent forget=new Intent(PhoneLoginActivity.this,ForgetPsdActivity.class);
                startActivity(forget, ActivityOptions.makeSceneTransitionAnimation(PhoneLoginActivity.this).toBundle());
                timer=new Timer();
                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
                timer.schedule(timerTask,1000);
                break;
            case R.id.tv_loginNoLogin:
//                timer=new Timer();
//                timerTask=new TimerTask() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(() -> finishAfterTransition());
//
//                    }
//                };
//                timer.schedule(timerTask,1000);
                finishAfterTransition();
                break;

        }
    }

    @Override
    public void BaseDestroy() {
        super.BaseDestroy();
        if (timer!=null){
            timer.cancel();
            timer=null;
            timerTask.cancel();
            timerTask=null;
        }
    }

    @Override
    protected boolean isSteepStateBar() {
        return false;
    }

    @Override
    public void initIntentAnim() {

    }

    @Override
    public boolean nullis() {
        return false;
    }

    @Override
    public boolean AllowFullScreen() {
        return true;
    }



    private long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK==keyCode){
            if (System.currentTimeMillis()-exitTime>2000){
                Toast.makeText(this, "双击退出登录", Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else {
                finishAfterTransition();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void ShowLoading() {

    }

    @Override
    public void HideLoading() {

    }

    @Override
    public void Fail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void LoginSuccess(UserInfo userInfo) {
        Toast.makeText(this, "登陆成功，进入主页", Toast.LENGTH_SHORT).show();
        System.out.println("登录成功---"+userInfo.nickname);
        SharedPreferencesUtil.putPreferences("uid",userInfo.uid+"");
        SharedPreferencesUtil.putPreferences("token",userInfo.token);
        Hawk.delete("UserInfo");
        Hawk.put("UserInfo",userInfo);
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> finishAfterTransition());
            }
        };
        timer.schedule(timerTask,1000);

    }

    @Override
    public void ResSuccess(String msg) {

    }
}
