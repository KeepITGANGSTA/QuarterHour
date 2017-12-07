package bwie.com.quarterhour;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Timer;
import java.util.TimerTask;

import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BasePresent;
import bwie.com.basemodule.SharedPreferencesUtil;
import present.PublishPublish;
import view.PublishView;

public class PublishEpiActivity extends AppCompatActivity implements View.OnClickListener{


    private RoundedImageView iv_publish_video,iv_publish_epi;
    private TextView tv_publish_back;
    private Timer timer;
    private TimerTask tt;
    private Timer timer2;
    private TimerTask tt2;
    private Intent intent;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=this.getWindow();
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.parseColor("#03A9F4"));
        setContentView(R.layout.activity_publish_epi);
        initView();
    }
    public void initView() {
        tv_publish_back=findViewById(R.id.tv_publish_back);
        iv_publish_epi=findViewById(R.id.iv_publish_epi);
        iv_publish_video=findViewById(R.id.iv_publish_video);

        tv_publish_back.setOnClickListener(this);
        iv_publish_video.setOnClickListener(this);
        iv_publish_epi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_publish_back:
                finishAfterTransition();
                break;
            case R.id.iv_publish_video:
                Toast.makeText(this, "发布视频", Toast.LENGTH_SHORT).show();
                Intent inten=new Intent(PublishEpiActivity.this,PublishEpisodeActivity.class);
                startActivity(inten,ActivityOptions.makeSceneTransitionAnimation(PublishEpiActivity.this).toBundle());
                timer2=new Timer();
                tt2=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
                timer2.schedule(tt2,1000);
                break;
            case R.id.iv_publish_epi:
                    intent = new Intent(PublishEpiActivity.this,PublishVideoActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(PublishEpiActivity.this).toBundle());
                    timer=new Timer();
                    tt=new TimerTask() {
                        @Override
                        public void run() {
                            finishAfterTransition();
                        }
                    };
                    timer.schedule(tt,1000);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
            tt.cancel();
            timer=null;
            tt=null;
        }
        if (intent!=null){
            intent=null;
        }
    }
}