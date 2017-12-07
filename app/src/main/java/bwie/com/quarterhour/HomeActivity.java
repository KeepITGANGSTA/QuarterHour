package bwie.com.quarterhour;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.LeftItemAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BasePresent;
import bwie.com.basemodule.SharedPreferencesUtil;
import entity.UserInfo;
import fragment.EpisodeFragment;
import fragment.HomeFragment;
import fragment.VideoFragment;
import toolbar.SimpleToolBar;

public class HomeActivity extends BaseActivity {

    private List<String> title=new ArrayList<String>(){{
        add("我的关注");
        add("我的收藏");
        add("搜索好友");
        add("消息通知");
    }};
    private List<Integer> ivlist=new ArrayList<Integer>(){{
        add(R.drawable.my_notice);
        add(R.drawable.my_collection);
        add(R.drawable.search_friend);
        add(R.drawable.msg_notification);
    }};

    //@BindView(R.id.mToolbar)
    SimpleToolBar simpleToolBar;
    //@BindView(R.id.mBottomBar)
    BottomBar bottomBar;
    //@BindView(R.id.mDrawerlayout)
    DrawerLayout drawerLayout;
    //@BindView(R.id.tv_title_toolbar)
    TextView tv_title_toolbar;
    //@BindView(R.id.iv_left_toolbar)
    ImageView iv_left_toolbar;
    //@BindView(R.id.iv_right_toolbar)
    ImageView iv_right_toolbar;
    //@BindView(R.id.left_recyclerView)
    RecyclerView left_recyclerView;
    //@BindView(R.id.iv_drawerlayout_icon)
    ImageView iv_drawerlayout_icon;
    //@BindView(R.id.tv_userName)
    TextView tv_userName;
    //@BindView(R.id.iv_sex)
    ImageView iv_set;
    //@BindView(R.id.tv_idvg)
    TextView tv_idvg;
    //@BindView(R.id.myFile)
    AutoLinearLayout myFile;
    //@BindView(R.id.left_setting)
    AutoLinearLayout left_setting;

    private HomeFragment homeFragment;
    private EpisodeFragment episodeFragment;
    private VideoFragment videoFragment;

    private LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
    private LeftItemAdapter adapter;

    private Timer timer;
    private TimerTask timerTask;
    private FragmentTransaction fragmentTransaction;

    private View inflate;
    private Button choosePhoto;
    private Button takePhoto;
    private Button btn_can;
    private Dialog dialog;
    private AlertDialog.Builder adb;
    private AlertDialog show;

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo userInfo = Hawk.get("UserInfo");
        if (userInfo!=null){
            Glide.with(App.AppContext).load(userInfo.icon).into(iv_drawerlayout_icon);
            if (userInfo.nickname==null){
                tv_userName.setText(userInfo.username);
            }else {
                tv_userName.setText(userInfo.nickname);
            }
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState==null){
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home_fragment");
            episodeFragment= (EpisodeFragment) getSupportFragmentManager().findFragmentByTag("episode_fragment");
            videoFragment= (VideoFragment) getSupportFragmentManager().findFragmentByTag("video_fragment");
        }
        //bind = ButterKnife.bind(this);
        simpleToolBar=findViewById(R.id.mToolbar);
        bottomBar=findViewById(R.id.mBottomBar);
        drawerLayout=findViewById(R.id.mDrawerlayout);
        tv_title_toolbar=findViewById(R.id.tv_title_toolbar);
        iv_left_toolbar=findViewById(R.id.iv_left_toolbar);
        iv_right_toolbar=findViewById(R.id.iv_right_toolbar);
        left_recyclerView=findViewById(R.id.left_recyclerView);
        iv_drawerlayout_icon=findViewById(R.id.iv_drawerlayout_icon);
        tv_userName=findViewById(R.id.tv_userName);
        iv_set=findViewById(R.id.iv_sex);
        tv_idvg=findViewById(R.id.tv_idvg);
        myFile=findViewById(R.id.myFile);
        left_setting=findViewById(R.id.left_setting);

        iv_drawerlayout_icon.setOnClickListener(this);
        tv_idvg.setOnClickListener(this);
        myFile.setOnClickListener(this);
        left_setting.setOnClickListener(this);


        Window window=this.getWindow();
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.parseColor("#03A9F4"));
        initBottombar();
        //initData();
    }

    @Override
    protected BasePresent initBasePresent() {
        return null;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    public void setDayNight() {
    }



    @Override
    public void initData() {
        simpleToolBar.setIv_leftOnClick(v -> drawerLayout.openDrawer(Gravity.LEFT));
        simpleToolBar.setIv_rightOnClick(v -> publishEpi());
        left_recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LeftItemAdapter(this,title,ivlist);
        left_recyclerView.setAdapter(adapter);
        adapter.setOnItemClick(id -> Toast.makeText(HomeActivity.this, title.get(id), Toast.LENGTH_SHORT).show());
    }

    private void publishEpi() {
        String uid = SharedPreferencesUtil.getPreferencesValue("uid");
        if (TextUtils.isEmpty(uid)){
            adb = new AlertDialog.Builder(HomeActivity.this);
            adb.setMessage("尚未登陆，还不能发表段子~");

            adb.setPositiveButton("去登陆", (dialog, which) -> {
                Intent intent=new Intent(HomeActivity.this,PhoneLoginActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                show.dismiss();
            });
            adb.setNegativeButton("做一名游侠~", (dialog, which) -> show.dismiss());
            adb.create();
            show = adb.show();
        }else {
            Intent intent=new Intent(HomeActivity.this,PublishEpiActivity.class);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
        }

    }


    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.iv_drawerlayout_icon:
                Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
                String uid = SharedPreferencesUtil.getPreferencesValue("uid");
                System.out.println("uid==="+uid);
                if ("".equals(uid)){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    timer=new Timer();
                    timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> drawerLayout.closeDrawers());
                        }
                    };
                    timer.schedule(timerTask,1000);
                }else {
                    showUserDialog();
                }
                break;
            case R.id.tv_idvg:

                break;
            case R.id.myFile:
                Toast.makeText(HomeActivity.this, "我的作品", Toast.LENGTH_SHORT).show();
                break;
            case R.id.left_setting:
                Toast.makeText(HomeActivity.this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.takePhoto:
                Toast.makeText(this, "修改昵称", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.choosePhoto:
                Toast.makeText(this, "修改个性签名", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.btn_qu:
                Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
                SharedPreferencesUtil.clearPreferences("uid");
                SharedPreferencesUtil.clearPreferences("token");
                Hawk.delete("UserInfo");
                iv_drawerlayout_icon.setImageResource(R.drawable.lefticon);
                tv_userName.setText("尚未登陆");
                dialog.dismiss();
                break;
        }
    }

    private void showUserDialog() {
        dialog = new Dialog(this,R.style.DialogSelect);
        inflate = LayoutInflater.from(this).inflate(R.layout.cancle, null);
        choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        btn_can = (Button) inflate.findViewById(R.id.btn_qu);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        btn_can.setOnClickListener(this);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 10;
        lp.width=App.screen_width-20;
        dialogWindow.setAttributes(lp);
        dialog.show();
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
        return false;
    }


    private void initBottombar() {
        setSupportActionBar(simpleToolBar);
        int i = Color.parseColor("#03A9F4");
        System.out.println("simpleToolBar---"+simpleToolBar);
        simpleToolBar.setBackgroundColor(i);

        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId){
                case R.id.tab_recommend:
                    simpleToolBar.setTv_title("推荐");
                    System.out.println("");
                    if (homeFragment==null){
                        System.out.println("-----null-----");
                        homeFragment=new HomeFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.main_frameLayout,homeFragment,"home_fragment").commit();
                    }else {
                        if (episodeFragment!=null){
                            getSupportFragmentManager().beginTransaction().hide(episodeFragment).commit();
                        }
                        if (videoFragment!=null){
                            getSupportFragmentManager().beginTransaction().hide(videoFragment).commit();
                        }
                        getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                    }
                    break;
                case R.id.tab_talk:
                    simpleToolBar.setTv_title("段子");
                    if (episodeFragment==null){
                        getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                        if (videoFragment!=null){
                            getSupportFragmentManager().beginTransaction().hide(videoFragment).commit();
                        }
                        episodeFragment=new EpisodeFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.main_frameLayout,episodeFragment,"episode_fragment").commit();
                    }else {
                        getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                        if (videoFragment!=null){
                            getSupportFragmentManager().beginTransaction().hide(videoFragment).commit();
                        }
                        getSupportFragmentManager().beginTransaction().show(episodeFragment).commit();
                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout,episodeFragment).commit();
                    break;
                case R.id.tab_video:
                    simpleToolBar.setTv_title("视频");
                    if (videoFragment==null){
                        getSupportFragmentManager().beginTransaction().hide(homeFragment);
                        if (episodeFragment!=null){
                            getSupportFragmentManager().beginTransaction().hide(episodeFragment).commit();
                        }
                        videoFragment=new VideoFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.main_frameLayout,videoFragment,"video_fragment").commit();
                    }else {
                        getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                        if (episodeFragment!=null){
                            getSupportFragmentManager().beginTransaction().hide(episodeFragment).commit();
                        }
                        getSupportFragmentManager().beginTransaction().show(videoFragment).commit();
                    }
                   //getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout,videoFragment).commit();
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linearLayoutManager=null;
        adapter.adapterDestroy();
        adapter=null;
        if (timer!=null){
            timer.cancel();
            timerTask.cancel();
            timerTask=null;
            timer=null;
        }
        episodeFragment=null;
        homeFragment=null;
        videoFragment=null;
        ivlist.clear();
        title.clear();
        if (dialog!=null){
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            dialog.cancel();
            dialog=null;
            inflate=null;
        }
        if (adb!=null){
            adb=null;
            if (show.isShowing()){
                show.dismiss();
            }
            show=null;
        }
    }

    @Override
    protected boolean isSteepStateBar() {
        return false;
    }

    private long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK==keyCode){
            if (System.currentTimeMillis()-exitTime>2000){
                Toast.makeText(this, "双击退出一刻钟", Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else {
                exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitApp() {
        Intent intent = new Intent();
        intent.setAction("net.loonggg.exitapp");
        this.sendBroadcast(intent);
    }
}
