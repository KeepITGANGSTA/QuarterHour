package bwie.com.quarterhour;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BasePresent;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private AutoLinearLayout login_weixin;
    private AutoLinearLayout login_qq;
    private TextView tv_loginOther;
    private TimerTask tt;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
    }


    public void initView() {
        login_weixin=findViewById(R.id.login_weixinL);
        login_qq=findViewById(R.id.login_qqL);
        tv_loginOther=findViewById(R.id.tv_loginOther);
        login_weixin.setOnClickListener(this);
        login_qq.setOnClickListener(this);
        tv_loginOther.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_qqL:
                Toast.makeText(this, "qq登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_weixinL:
                Toast.makeText(this, "微信登陆", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_loginOther:
                Intent intent=new Intent(LoginActivity.this,PhoneLoginActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                timer=new Timer();
                tt=new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> finishAfterTransition());
                    }
                };
                timer.schedule(tt,1000);
                break;
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
            tt.cancel();
            tt=null;
            timer=null;
        }
    }
}
