package bwie.com.quarterhour;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BasePresent;

public class AlertPsdActivity extends BaseActivity {

    private TextView tv_alertGotUser;
    private EditText ed_alertPsd;
    private EditText ed_alertPsdAgain;
    private Button btn_AlertFinish;
    private TextView tv_alertLoginNoLogin;

    private Timer timer;
    private TimerTask tt;

    @Override
    protected BasePresent initBasePresent() {
        return null;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_alert_psd;
    }

    @Override
    public void setDayNight() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tv_alertGotUser=findViewById(R.id.tv_alertGotUser);
        ed_alertPsd=findViewById(R.id.ed_AlterPsd);
        ed_alertPsdAgain=findViewById(R.id.ed_AlterPsdAgain);
        btn_AlertFinish=findViewById(R.id.btn_Alterfinish);
        tv_alertLoginNoLogin=findViewById(R.id.tv_alterloginNoLogin);

        tv_alertGotUser.setOnClickListener(this);
        btn_AlertFinish.setOnClickListener(this);
        tv_alertLoginNoLogin.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.tv_alertGotUser:
                Intent intent=new Intent(AlertPsdActivity.this,PhoneLoginActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AlertPsdActivity.this).toBundle());
                timer=new Timer();
                tt=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
                timer.schedule(tt,1000);
                break;
            case R.id.btn_Alterfinish:

                break;
            case R.id.tv_alterloginNoLogin:
                finishAfterTransition();
                break;
        }
    }

    @Override
    public void BaseDestroy() {
        super.BaseDestroy();
        if (timer!=null){
            timer.cancel();
            tt.cancel();
            tt=null;
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
}
