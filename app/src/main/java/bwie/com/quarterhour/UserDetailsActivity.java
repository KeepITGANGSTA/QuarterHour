package bwie.com.quarterhour;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

import adapter.HotAdapter;
import adapter.UserAdapter;
import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BasePresent;
import bwie.com.basemodule.SharedPreferencesUtil;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import entity.UserInfoBean;
import glide.GlideCache;
import present.FollowPresent;
import utils.ActivityLifeCycleEvent;
import view.FollowView;

public class UserDetailsActivity extends BaseActivity<FollowPresent> implements FollowView{

    private RoundedImageView iv_icon;
    private TextView tv_title,tv_fans,tv_notice,tv_noticeOrNoticed,tv_parisedNum;
    private RecyclerView recyclerView;
    private AutoLinearLayout lin_user_notice,lin_user_parise;
    private ImageView iv_plus,iv_back;
    private boolean isNotice=false;
    private boolean isParise=false;
    private int uid;
    private UserAdapter adapter;
    private LinearLayoutManager manager;

    @Override
    protected boolean isSteepStateBar() {
        return false;
    }

    @Override
    protected FollowPresent initBasePresent() {
        return new FollowPresent(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_user_details;
    }

    @Override
    public void setDayNight() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#03A9F4"));

        tv_title=findViewById(R.id.tv_user_title);
        iv_icon=findViewById(R.id.iv_user_icon);
        tv_fans=findViewById(R.id.tv_user_fans);
        tv_notice=findViewById(R.id.tv_user_noti);
        tv_noticeOrNoticed=findViewById(R.id.tv_noticeOrNoticed);
        tv_parisedNum=findViewById(R.id.tv_userParisedNum);
        recyclerView=findViewById(R.id.recy_user_video);
        lin_user_notice=findViewById(R.id.lin_user_notice);
        lin_user_parise=findViewById(R.id.lin_user_parise);
        iv_plus=findViewById(R.id.iv_plus);
        iv_back=findViewById(R.id.iv_user_back);

        iv_back.setOnClickListener(this);
        lin_user_notice.setOnClickListener(this);
        lin_user_parise.setOnClickListener(this);


    }

    @Override
    public void initData() {
        manager=new LinearLayoutManager(this);
        uid = getIntent().getIntExtra("uid",0);
        if (uid!=0){
            basePresent.getUserInfo(uid+"",this,lifecycleSubject);
        }


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.lin_user_notice:
                if (isNotice){
                    Toast.makeText(App.AppContext, "您已关注", Toast.LENGTH_SHORT).show();
                    return;
//                    lin_user_notice.setBackgroundResource(R.drawable.user_notice);
//                    isNotice=false;
//                    iv_plus.setVisibility(View.VISIBLE);
//                    tv_noticeOrNoticed.setText("关注");
//                    tv_noticeOrNoticed.setTextColor(R.color.qqBgColor);
                }else {
                    Toast.makeText(App.AppContext, "开始关注", Toast.LENGTH_SHORT).show();
                    basePresent.follow(SharedPreferencesUtil.getPreferencesValue("uid"),uid+"",this);
                }
                break;
            case R.id.lin_user_parise:

                break;
            case R.id.iv_user_back:
                finishAfterTransition();
                break;
        }
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
    public void followSuccess(String msg) {
        lin_user_notice.setBackgroundResource(R.drawable.user_notice_soild);
        isNotice=true;
        iv_plus.setVisibility(View.GONE);
        tv_noticeOrNoticed.setText("已关注");
        tv_noticeOrNoticed.setTextColor(Color.WHITE);
        Toast.makeText(App.AppContext, "已关注", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void followFaile(String msg) {

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void getUserSuccess(UserInfoBean o) {
        boolean follow = o.user.follow;
        System.out.println("是否关注----"+follow);
        if (follow){
            lin_user_notice.setBackgroundResource(R.drawable.user_notice_soild);
            isNotice=true;
            iv_plus.setVisibility(View.GONE);
            tv_noticeOrNoticed.setText("已关注");
            tv_noticeOrNoticed.setTextColor(Color.WHITE);
        }else {
            lin_user_notice.setBackgroundResource(R.drawable.user_notice);
            isNotice=false;
            iv_plus.setVisibility(View.VISIBLE);
            tv_noticeOrNoticed.setText("关注");
            tv_noticeOrNoticed.setTextColor(R.color.qqBgColor);
        }
        Glide.with(this).applyDefaultRequestOptions(GlideCache.NoMemoryDiskCache()).load(o.user.icon).into(iv_icon);
        tv_title.setText(o.user.nickname);
        tv_fans.setText(o.user.fans == null ?o.user.fans  : 0 + "" +"   粉丝");
        tv_parisedNum.setText(o.user.praiseNum == null ?o.user.praiseNum :  0 + "");
        adapter = new UserAdapter(o,this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                JZVideoPlayer viewById = view.findViewById(R.id.user_girffe);
                if (viewById != null && JZUtils.dataSourceObjectsContainsUri(viewById.dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                    JZVideoPlayer.releaseAllVideos();
                }
            }
        });

        adapter.setOnItemClickVideo((worksEntitiesBean, position) -> {
            Intent intent=new Intent(UserDetailsActivity.this,VideoDetailsActivity.class);
            intent.putExtra("videoWid",worksEntitiesBean.wid);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this).toBundle());
        });

    }

    @Override
    public void BaseDestroy() {
        super.BaseDestroy();
        if (adapter!=null){
            adapter.destroy();
        }
        if (manager!=null){
            manager=null;
        }
    }

    @Override
    public void getUserFail(String msg) {

    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

}
