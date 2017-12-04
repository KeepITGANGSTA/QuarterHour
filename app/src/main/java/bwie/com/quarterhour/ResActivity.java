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

import java.util.Timer;
import java.util.TimerTask;

import bwie.com.basemodule.BaseActivity;
import entity.UserInfo;
import present.LoginPresent;
import view.LoginView;

public class ResActivity extends BaseActivity<LoginPresent> implements LoginView{

    private TextView tv_resToLogin;
    private EditText ed_resPhone;
    private EditText ed_resPsd;
    private Button btn_res;
    private TextView tv_resLoginNoLogin;

    private Timer timer;
    private TimerTask timerTask;

    private Timer timer2;
    private TimerTask tt2;

    @Override
    protected LoginPresent initBasePresent() {
        return new LoginPresent(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_res;
    }

    @Override
    public void setDayNight() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tv_resToLogin=findViewById(R.id.tv_resToLogin);
        ed_resPhone=findViewById(R.id.ed_Resphone);
        ed_resPsd=findViewById(R.id.ed_Respsd);
        btn_res=findViewById(R.id.btn_res);
        tv_resLoginNoLogin=findViewById(R.id.tv_ResloginNoLogin);

        tv_resToLogin.setOnClickListener(this);
        btn_res.setOnClickListener(this);
        tv_resLoginNoLogin.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.tv_resToLogin:
                Intent intent=new Intent(ResActivity.this,PhoneLoginActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ResActivity.this).toBundle());
                timer=new Timer();
                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
                timer.schedule(timerTask,1000);
                break;
            case R.id.btn_res:
                String phone=ed_resPhone.getText().toString();
                String psd=ed_resPsd.getText().toString();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(psd)){
                    Toast.makeText(this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length()!=11 ){
                    Toast.makeText(this, "账号格式有误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( psd.length()>9){
                    Toast.makeText(this, "密码长度不能超过8位", Toast.LENGTH_SHORT).show();
                    return;
                }
                basePresent.res(phone,psd,ResActivity.this,lifecycleSubject);
                break;
            case R.id.tv_ResloginNoLogin:
                timer=new Timer();
                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
                timer.schedule(timerTask,1000);
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
        if (timer2!=null){
            timer2.cancel();
            timer2=null;
            tt2.cancel();
            tt2=null;
        }
    }

    @Override
    public void initIntentAnim() {

    }

    @Override
    public boolean nullis() {
        return true;
    }

    @Override
    public boolean AllowFullScreen() {
        return true;
    }

    @Override
    public void ShowLoading() {

    }

    @Override
    public void HideLoading() {

    }

    @Override
    public void Fail(String msg) {

    }

    @Override
    public void LoginSuccess(UserInfo userInfo) {

    }

    @Override
    public void ResSuccess(String msg) {
        Toast.makeText(this, "注册成功，进入登录界面", Toast.LENGTH_SHORT).show();

        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    Intent intent=new Intent(ResActivity.this,PhoneLoginActivity.class);
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ResActivity.this).toBundle());
                    timer2=new Timer();
                    tt2=new TimerTask() {
                        @Override
                        public void run() {
                            finishAfterTransition();
                        }
                    };
                    timer2.schedule(tt2,1000);

                });
            }
        };
        timer.schedule(timerTask,1000);
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
}
