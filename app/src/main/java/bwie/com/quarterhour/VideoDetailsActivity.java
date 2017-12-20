package bwie.com.quarterhour;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.CommentsAdapter;
import api.Api;
import api.Common;
import bwie.com.basemodule.BaseActivity;
import bwie.com.basemodule.BasePresent;
import bwie.com.basemodule.BaseSwipeActivity;
import bwie.com.basemodule.NetAval;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.basemodule.SharedPreferencesUtil;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;
import entity.BaseEntity;
import entity.VideoDetails;
import glide.GlideCache;
import present.CollectPresent;
import rx.Observable;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;
import view.CollectVideoView;

public class VideoDetailsActivity extends BaseActivity<CollectPresent> implements CollectVideoView{

    private static final String SUM="次播放";
    private ImageView iv_back,iv_collect,iv_share;
    private EditText ed_comments;
    private Button btn_comments;
    private ProgressSubscriber videoDetails,refreshC;
    private JZVideoPlayerStandard details_video;
    private TextView tv_playNum,tv_time,tv_title,tv_createTime;
    private SpannableStringBuilder style;
    private AutoLinearLayout no_comment;
    private RecyclerView details_comments;
    private static MediaPlayer mediaPlayer;
    private static SimpleDateFormat simpleDateFormat;
    private static DetailsHandler detailsHandler;
    private MTask task;
    private Timer timer;
    private TimerTask tt;
    private LinearLayoutManager linearLayoutManager;
    private CommentsAdapter adapter;
    private int videoWid;
    private boolean isCollect=false;


    @Override
    public void ShowLoading() {

    }

    @Override
    public void HideLoading() {

    }

    @Override
    public void Fail(String msg) {
        System.out.println("收藏视频失败----"+msg);
    }

    @Override
    public void collectSuccess(String msg) {
        iv_collect.setImageResource(R.drawable.praised);
        Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
        isCollect=true;
    }

    @Override
    public void cancelSuccess(String msg) {
        isCollect=false;
        iv_collect.setImageResource(R.drawable.my_notice);
        Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancelFail(String msg) {

    }

    @Override
    public void pariseSuccess(String msg) {

    }

    @Override
    public void commentSuccess(String msg) {
        Toast.makeText(App.AppContext, "评论成功", Toast.LENGTH_SHORT).show();
        refreshComments();

    }

    @Override
    public void commentFila(String msg) {
        Toast.makeText(App.AppContext, "评论失败---"+msg, Toast.LENGTH_SHORT).show();
    }

    private static class DetailsHandler extends Handler{
        WeakReference<Activity> weakReference;

        public DetailsHandler(Activity activity) {
            weakReference=new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final VideoDetailsActivity activity= (VideoDetailsActivity) weakReference.get();
            System.out.println("------------------message---"+activity);
            if (activity!=null){
                String time = (String) msg.obj;
                activity.runOnUiThread(() -> activity.tv_time.setText(time));

                System.out.println("----------------赋值："+time);
            }
        }
    }



    @Override
    public boolean nullis() {
        return true;
    }

    @Override
    public boolean AllowFullScreen() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_video_details;
    }

    @Override
    public void setDayNight() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager(VideoDetailsActivity.this);
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#03A9F4"));
        iv_back=findViewById(R.id.iv_VideoDetails_back);
        iv_collect= findViewById(R.id.iv_VideoDetails_collect);
        iv_share= findViewById(R.id.iv_VideoDetails_share);
        details_video=  findViewById(R.id.details_video);
        tv_playNum=  findViewById(R.id.tv_details_sum);
        tv_time=  findViewById(R.id.tv_details_time);
        tv_title= findViewById(R.id.tv_details_title);
        tv_createTime= findViewById(R.id.tv_details_time_sum);
        no_comment=  findViewById(R.id.no_comments);
        details_comments=  findViewById(R.id.details_comments);
        ed_comments=findViewById(R.id.ed_comment);
        btn_comments=findViewById(R.id.btn_comments);

        iv_back.setOnClickListener(this);
        iv_collect.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        btn_comments.setOnClickListener(this);
    }



    @Override
    public void initData() {

        videoWid = getIntent().getIntExtra("videoWid",0);
        timer = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> getDcetails(videoWid));
            }
        };
        timer.schedule(tt,500);
    }

    private void getDcetails(int videoWid){
        boolean netAvailable = NetAval.NetAvailable(App.AppContext);
        System.out.println("netAvailable---"+netAvailable);
        if (videoWid!=0 && NetAval.NetAvailable(App.AppContext)){
            Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
            videoDetails = new ProgressSubscriber<VideoDetails>(this) {
                @Override
                public void _Next(VideoDetails o) {
                    System.out.println("成功---");
                    Glide.with(VideoDetailsActivity.this).applyDefaultRequestOptions(GlideCache.NoMemoryDiskCache()).load(o.cover).into(details_video.thumbImageView);
                    String s = o.playNum + "";
                    style = new SpannableStringBuilder(s+SUM);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_playNum.setText(s+SUM);
                    details_video.setUp(o.videoUrl,JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL," ");
                    if (detailsHandler==null){
                        detailsHandler = new DetailsHandler(VideoDetailsActivity.this);
                    }
                    task = new MTask(o);
                    task.start();
                    tv_title.setText(o.workDesc);
                    tv_createTime.setText(o.playNum+"次播放"+"  "+o.createTime);
                    List<VideoDetails.CommentsBean> comments = o.comments;
                    if (comments!=null && comments.size()!=0){
                        no_comment.setVisibility(View.GONE);
                        details_comments.setVisibility(View.VISIBLE);
                        details_comments.setLayoutManager(linearLayoutManager);
                        adapter = new CommentsAdapter(VideoDetailsActivity.this,comments);
                        details_comments.setAdapter(adapter);
                    }else {
                        no_comment.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void _OnError(String msg) {
                    System.out.println("失败---");
                }
            };
            Observable apiServiceVideoDetails = apiService.getVideoDetails(videoWid + "");
            HttpUtils.getInstace().toSubscribe(apiServiceVideoDetails, videoDetails,"videoDetailsCache", ActivityLifeCycleEvent.PAUSE,lifecycleSubject,false,false);
        }else {
            Toast.makeText(this, "网络不可用，请检查网络~", Toast.LENGTH_SHORT).show();
        }
    }


    private void refreshComments(){
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        refreshC = new ProgressSubscriber<VideoDetails>(this) {
            @Override
            public void _Next(VideoDetails o) {
                ed_comments.setText("");
                List<VideoDetails.CommentsBean> comments = o.comments;
                if (comments!=null && comments.size()!=0){
                    if (adapter==null){
                        no_comment.setVisibility(View.GONE);
                        details_comments.setVisibility(View.VISIBLE);
                        details_comments.setLayoutManager(linearLayoutManager);
                        adapter = new CommentsAdapter(VideoDetailsActivity.this,comments);
                        details_comments.setAdapter(adapter);
                    }else {
                        adapter.refreshComments(comments);
                    }
                }else {
                    Toast.makeText(App.AppContext, "评论为空~", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void _OnError(String msg) {
                System.out.println("失败---");
            }
        };
        Observable apiServiceVideoDetails = apiService.getVideoDetails(videoWid + "");
        HttpUtils.getInstace().toSubscribe(apiServiceVideoDetails, videoDetails,"videoDetailsCache", ActivityLifeCycleEvent.PAUSE,lifecycleSubject,false,false);
    }



    private static class MTask extends Thread{
        private VideoDetails o;
        public MTask(VideoDetails o) {
            this.o = o;
        }
        @Override
        public void run() {
            if (mediaPlayer==null){
                System.out.println("MediaPlayer为空-----new出来");
                mediaPlayer = new MediaPlayer();
            }
            System.out.println("MediaPlayer不为空----输出："+mediaPlayer);
            try {
                mediaPlayer.setDataSource(o.videoUrl);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int duration = mediaPlayer.getDuration();
            if (simpleDateFormat==null){
                System.out.println("SimpleDateFormat为空----new出来");
                simpleDateFormat = new SimpleDateFormat("mm:ss");
            }
            System.out.println("SimpleDateFormat不为空----输出："+simpleDateFormat);
            String format = simpleDateFormat.format(duration);
            Message message=Message.obtain();
            message.obj=format;
            detailsHandler.handleMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("详情销毁--");
        if (videoDetails!=null){
            videoDetails.dismissProgressDialog();
            videoDetails.unsubscribe();
            videoDetails=null;
        }
        if (refreshC!=null){
            refreshC.unsubscribe();
            refreshC=null;
        }
        if (details_video!=null){
            details_video=null;
        }
        if (style!=null){
            style.clear();
            style.clearSpans();
            style=null;
        }
        if (mediaPlayer!=null){
            mediaPlayer.release();
            System.out.println("MediaPlayer清空资源");
            mediaPlayer=null;
        }
        if (simpleDateFormat!=null){
            simpleDateFormat=null;
        }
        if (detailsHandler!=null){
            detailsHandler.removeCallbacksAndMessages(null);
            detailsHandler=null;
        }
        if (task!=null){
            task=null;
        }
        if (timer!=null){
            timer.cancel();
            tt.cancel();
            timer=null;
            tt=null;
        }
        if (linearLayoutManager!=null){
            linearLayoutManager=null;
        }
        if (adapter!=null){
            adapter.destroy();
            adapter=null;
        }
    }

    @Override
    protected boolean isSteepStateBar() {
        return false;
    }

    @Override
    protected CollectPresent initBasePresent() {
        return new CollectPresent(this);
    }

    @Override
    public void BaseOnClick(View view) {
        switch (view.getId()){
            case R.id.iv_VideoDetails_back:
                finishAfterTransition();
                break;
            case R.id.iv_VideoDetails_collect:
                String uid = SharedPreferencesUtil.getPreferencesValue("uid");
                if (TextUtils.isEmpty(uid)){
                    Toast.makeText(this, "请先登录~", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isCollect){
                    basePresent.cancelVideoCollect(uid,videoWid+"",VideoDetailsActivity.this);
                }else {
                    basePresent.collectVideo(uid,videoWid+"",VideoDetailsActivity.this);
                }

                break;
            case R.id.iv_VideoDetails_share:

                break;
            case R.id.btn_comments:
                String ui = SharedPreferencesUtil.getPreferencesValue("uid");
                if (TextUtils.isEmpty(ui)){
                    Toast.makeText(App.AppContext, "登录后才能评论~", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content=ed_comments.getText().toString();
                if (TextUtils.isEmpty(content)){
                    Toast.makeText(App.AppContext, "请输入评论", Toast.LENGTH_SHORT).show();
                    return;
                }
                basePresent.commentVideo(ui,videoWid+"",content,VideoDetailsActivity.this);
                break;
        }
    }

    @Override
    public void initIntentAnim() {

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
        System.out.println("详情暂停--");
        JZVideoPlayer.releaseAllVideos();
    }
}
