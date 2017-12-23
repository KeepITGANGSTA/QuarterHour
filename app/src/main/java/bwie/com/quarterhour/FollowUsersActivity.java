package bwie.com.quarterhour;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.FollowAdapter;
import adapter.RecyclerViewDivide;
import api.Api;
import api.Common;
import bwie.com.basemodule.NetAval;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.basemodule.SharedPreferencesUtil;
import entity.BaseEntity;
import entity.EpiDetails;
import entity.FollowBean;
import rx.Observable;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;

public class FollowUsersActivity extends AppCompatActivity implements View.OnClickListener{

    private final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject=PublishSubject.create();
    private RecyclerView recyclerView;
    private View include;
    private ProgressSubscriber subscriber;
    private LinearLayoutManager manager;
    private FollowAdapter adapter;
    private Button btn_follow;
    private Timer timer;
    private TimerTask tt;
    private ImageView iv_back;
    private RecyclerViewDivide divide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_users);
        manager=new LinearLayoutManager(this);
        divide = new RecyclerViewDivide(this, LinearLayoutManager.VERTICAL);
        initView();
        if (NetAval.NetAvailable(App.AppContext)){
            System.out.println("NetAval----"+NetAval.NetAvailable(App.AppContext));
            getFollowUser();
        }else {
            System.out.println("NetAval----"+NetAval.NetAvailable(App.AppContext));
            recyclerView.setVisibility(View.GONE);
            include.setVisibility(View.VISIBLE);
        }
    }

    private void getFollowUser() {
        String uid = SharedPreferencesUtil.getPreferencesValue("uid");
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        Observable followUser = apiService.getFollowUser(uid);
        subscriber = new ProgressSubscriber<List<FollowBean>>(this) {

            @Override
            public void _Next(List<FollowBean> o) {
                include.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new FollowAdapter(FollowUsersActivity.this,o);
                recyclerView.setAdapter(adapter);
                adapter.setFollowItemClick(uid1 -> {
                    Intent intent=new Intent(FollowUsersActivity.this,UserDetailsActivity.class);
                    intent.putExtra("uid",uid1);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(FollowUsersActivity.this).toBundle());
                });
            }
            @Override
            public void _OnError(String msg) {
                Toast.makeText(App.AppContext, "登录超时，请重新登录~", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FollowUsersActivity.this,PhoneLoginActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(FollowUsersActivity.this).toBundle());
                timer=new Timer();
                tt=new TimerTask() {
                    @Override
                    public void run() {
                        finishAfterTransition();
                    }
                };
                timer.schedule(tt,1000);
            }
        };
        HttpUtils.getInstace().toSubscribe(followUser,subscriber,"followCache",ActivityLifeCycleEvent.PAUSE,lifecycleSubject,false,false);
    }

    private void initView() {
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#03A9F4"));
        recyclerView=findViewById(R.id.recy_follow_users);
        include=findViewById(R.id.follow_users_noNet);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(divide);
        btn_follow = findViewById(R.id.btn_epiDetails);
        iv_back=findViewById(R.id.iv_follow_back);
        iv_back.setOnClickListener(this);
        btn_follow.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        super.onDestroy();
        if (subscriber!=null){
            subscriber.dismissProgressDialog();
            subscriber.unsubscribe();
            subscriber=null;
        }
        manager=null;
        if (adapter!=null){
            adapter.destroy();
            adapter=null;
        }
        if (timer!=null){
            timer.cancel();
            tt.cancel();
            timer=null;
            tt=null;
        }
        divide=null;
    }


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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_epiDetails:
                if (NetAval.NetAvailable(App.AppContext)){
                    getFollowUser();
                }else {
                    Toast.makeText(App.AppContext, "网络不可用", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_follow_back:
                finishAfterTransition();
                break;
        }
    }
}