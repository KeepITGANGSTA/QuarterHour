package bwie.com.quarterhour;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.mob.MobSDK;
import com.zhy.autolayout.attr.PaddingRightAttr;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import api.Api;
import bwie.com.basemodule.RetrofitHelper;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import entity.UserInfo;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;

public class ForgetPsdActivity extends AppCompatActivity implements View.OnClickListener{
    private  PublishSubject<ActivityLifeCycleEvent> lifecycleSubject=PublishSubject.create();
    private TextView tv_getUser;
    private EditText ed_forgetPhone;
    private EditText ed_veriCode;
    private Button btn_getCode;
    private Button btn_codeNext;
    private TextView tv_loginNoLogin;

    private Timer timer;
    private TimerTask timerTask;
    private Timer timer2;
    private TimerTask tt2;

    private EventHandler eventHandler;
    private ProgressSubscriber progressSubscriber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_psd);
        initView();
        MobSDK.init(this,"22bd5fa3035b0","c66f4b79c050a0477a0c36cf77551405");
        initSMSS();

        Observable<Void> voidObservable = RxView.clicks(btn_getCode)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(ForgetPsdActivity.this, "获取验证码", Toast.LENGTH_SHORT).show();
                        String phone = ed_forgetPhone.getText().toString();
                        if (TextUtils.isEmpty(phone)) {
                            Toast.makeText(ForgetPsdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (phone.length() != 11) {
                            Toast.makeText(ForgetPsdActivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SMSSDK.getVerificationCode("86", phone);
                        RxView.enabled(btn_getCode).call(false);
                        //testLogin(phone);
                    }
                });
        voidObservable.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Observable.interval(1,TimeUnit.SECONDS,AndroidSchedulers.mainThread())
                        .take(60)
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onCompleted() {
                                RxTextView.text(btn_getCode).call("获取验证码");
                                RxView.enabled(btn_getCode).call(true);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Long aLong) {
                                RxTextView.text(btn_getCode).call((60-aLong)+"秒");
                            }
                        });
            }
        });

    }

    private void initSMSS() {
        eventHandler=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable){
                    Throwable throwable= (Throwable) data;
                    String msg=throwable.getMessage();
                    runOnUiThread(() -> Toast.makeText(ForgetPsdActivity.this, msg, Toast.LENGTH_SHORT).show());
                }else {
                    if (result==SMSSDK.RESULT_COMPLETE){
                        // 验证成功，子线程
                        if (event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                            System.out.println("提交验证码成功");

                            runOnUiThread(() -> {
                                Toast.makeText(ForgetPsdActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                            });
                            timer=new Timer();
                            timerTask=new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(() -> {
                                        Intent intent=new Intent(ForgetPsdActivity.this,AlertPsdActivity.class);
                                        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ForgetPsdActivity.this).toBundle());
                                    });
                                    timer2=new Timer();
                                    tt2=new TimerTask() {
                                        @Override
                                        public void run() {
                                            finishAfterTransition();
                                        }
                                    };
                                    timer2.schedule(tt2,1000);
                                }
                            };
                            timer.schedule(timerTask,1000);

                        }else if (event==SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            System.out.println("获取验证码成功");
                        }else if (event==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                            System.out.println("获取支持国家列表成功");
                        }
                    }

                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initView() {
        tv_getUser=findViewById(R.id.tv_gotUser);
        ed_forgetPhone=findViewById(R.id.ed_Forgetphone);
        ed_veriCode=findViewById(R.id.ed_forgetCode);
        btn_getCode=findViewById(R.id.btn_getCode);
        btn_codeNext=findViewById(R.id.btn_forgetNext);
        tv_loginNoLogin=findViewById(R.id.tv_forgetloginNoLogin);

        tv_getUser.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
        btn_codeNext.setOnClickListener(this);
        tv_loginNoLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_gotUser:
                Intent intent=new Intent(ForgetPsdActivity.this,PhoneLoginActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ForgetPsdActivity.this).toBundle());
                timer=new Timer();
                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
                timer.schedule(timerTask,1000);
                break;
//            case R.id.btn_getCode:
//                String phone=ed_forgetPhone.getText().toString();
//                if (TextUtils.isEmpty(phone)){
//                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (phone.length()!=11){
//                    Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                testLogin(phone);
//                break;
            case R.id.btn_forgetNext:
                String vphone=ed_forgetPhone.getText().toString();
                String vcode=ed_veriCode.getText().toString();
                if (TextUtils.isEmpty(vphone) || TextUtils.isEmpty(vcode)){
                    Toast.makeText(this, "手机号和验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (vphone.length()!=11 || vcode.length()!=4){
                    Toast.makeText(this, "请输入正确的手机号和验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println("验证码---"+vcode);
                SMSSDK.submitVerificationCode("86",vphone,vcode);
                break;
            case R.id.tv_forgetloginNoLogin:
                finishAfterTransition();
                break;

        }
    }


//    private void testLogin(String phone) {
//        Api apiService = RetrofitHelper.getRetrofitHelper("https://www.zhaoapi.cn/",this).getApiService(Api.class);
//        Observable login = apiService.login(phone,"111111");
//        progressSubscriber = new ProgressSubscriber<UserInfo>(this) {
//            @Override
//            public void _Next(UserInfo o) {
//                System.out.println("成功---"+o.nickname);
//                SMSSDK.getVerificationCode("86",phone);
//            }
//            @Override
//            public void _OnError(String msg) {
//                if ("尚未注册".equals(msg)){
//                    Toast.makeText(ForgetPsdActivity.this, "改手机号"+msg, Toast.LENGTH_SHORT).show();
//                }else {
//                    SMSSDK.getVerificationCode("86",phone);
//                }
//            }
//        };
//        HttpUtils.getInstace().toSubscribe(login,progressSubscriber ,"testPhoneCache", ActivityLifeCycleEvent.PAUSE,lifecycleSubject,false,false);
//    }



    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
        if (timer!=null){
            timer.cancel();
            timerTask.cancel();
            timer=null;
            timerTask=null;
        }
        if (timer2!=null){
            timer2.cancel();
            timer2=null;
            tt2.cancel();
            tt2=null;
        }
        lifecycleSubject=null;
        if (progressSubscriber!=null){
            progressSubscriber.dismissProgressDialog();
            if (progressSubscriber.isUnsubscribed()){
                progressSubscriber.unsubscribe();
            }
            progressSubscriber=null;
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
}
