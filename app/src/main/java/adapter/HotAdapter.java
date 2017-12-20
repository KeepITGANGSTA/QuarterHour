package adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.basemodule.SharedPreferencesUtil;
import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;
import bwie.com.quarterhour.UserDetailsActivity;
import bwie.com.quarterhour.VideoDetailsActivity;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayerStandard;
import entity.BaseEntity;
import entity.VideoInfo;
import present.CollectPresent;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import utils.ShowDialog;
import view.CollectVideoView;

/**
 * Created by 李英杰 on 2017/12/1.
 * Description：
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.HotViewHolder> implements View.OnClickListener, CollectVideoView{

    private List<VideoInfo> list;
    private Context context;
    private View viewp;
    private SpannableStringBuilder style;
    private HotViewHolder hotViewHolder;
    private final Api apiService;
    private Subscription pariseSubscribe;
    private boolean isParise=false;
    private boolean isCollect=false;
    private final CollectPresent present;
    private ShowDialog showDialog;


    public HotAdapter(List<VideoInfo> list, Context context) {
        this.list = list;
        this.context = context;
        apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        present = new CollectPresent(this);
    }

    public void setList(List<VideoInfo> t) {
        if (list != null) {
            list.clear();
        }
        list = t;
        System.out.println("适配器视频集合：" + list.size());
        notifyDataSetChanged();
    }

    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder---------------");
        viewp = LayoutInflater.from(context).inflate(R.layout.hot_item, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(App.screen_width, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewp.setLayoutParams(layoutParams);
        hotViewHolder = new HotViewHolder(viewp);
        return hotViewHolder;
    }


    @Override
    public void onBindViewHolder(HotViewHolder holder, int position) {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        VideoInfo videoInfo = list.get(position);
        Glide.with(context).applyDefaultRequestOptions(options).load(videoInfo.user.icon).into(holder.iv_icon);
        holder.tv_time.setText(videoInfo.createTime);
        holder.tv_content.setText(videoInfo.workDesc);
        holder.tv_auth.setText(videoInfo.user.nickname);
        holder.tv_parise_num.setText(videoInfo.praiseNum+"");
        holder.tv_collect_num.setText(videoInfo.favoriteNum+"");
        holder.tv_comment_num.setText(videoInfo.commentNum+"");
        holder.videoView.setUp(videoInfo.videoUrl,JZVideoPlayerStandard.SCREEN_WINDOW_LIST,videoInfo.workDesc);
        System.out.println("视频地址：---"+videoInfo.videoUrl);
        ImageView thumbImageView = holder.videoView.thumbImageView;
        thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(videoInfo.cover).into(thumbImageView);
        setTalk(holder, position, videoInfo);
        holder.itemView.setOnClickListener(v -> onItemClickVideo.setOnItemClickVideo(videoInfo,position));
        itemOnClick(holder,position);


    }

    private void itemOnClick(HotViewHolder holder, int position) {
        holder.lin_parise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = SharedPreferencesUtil.getPreferencesValue("uid");
                if (TextUtils.isEmpty(uid)){
                    Toast.makeText(App.AppContext, "请先登录~", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isParise){
                    Toast.makeText(App.AppContext, "您已赞过~", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    present.pariseVideo(uid,list.get(position).wid+"",context);
                    holder.iv_hot_parise.setImageResource(R.drawable.praised);
                    notifyDataSetChanged();
                }

            }
        });
        holder.lin_comments.setOnClickListener(v -> {
            Intent intent=new Intent(context, VideoDetailsActivity.class);
            intent.putExtra("videoWid",list.get(position).wid);
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation( (Activity)context).toBundle() );
        });
        holder.lin_collect.setOnClickListener(v -> {
            String uid = SharedPreferencesUtil.getPreferencesValue("uid");
            if (TextUtils.isEmpty(uid)){
                Toast.makeText(App.AppContext, "请先登录~", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isCollect){
                present.cancelVideoCollect(uid,list.get(position).wid+"",context);
                holder.iv_hot_collect.setImageResource(R.drawable.collect);
            }else {
                present.collectVideo(uid,list.get(position).wid+"",context);
                holder.iv_hot_collect.setImageResource(R.drawable.collected);
            }
            notifyDataSetChanged();
        });
        holder.lin_share.setOnClickListener(v -> {
            Toast.makeText(App.AppContext, "分享", Toast.LENGTH_SHORT).show();
            if (showDialog==null){
                showDialog=new ShowDialog(context);
            }
            showDialog.showDialog();
        });
        holder.tv_talkMore.setOnClickListener(v -> {
            Intent intent=new Intent(context, VideoDetailsActivity.class);
            intent.putExtra("videoWid",list.get(position).wid);
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation( (Activity)context).toBundle() );
        });
        holder.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UserDetailsActivity.class);
                intent.putExtra("uid",list.get(position).uid);
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation( (Activity)context).toBundle() );
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    @Override
    public void onClick(View v) {

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
    public void collectSuccess(String msg) {
        //hotViewHolder.iv_hot_collect.setImageResource(R.drawable.collected);
        Toast.makeText(App.AppContext, "收藏成功~", Toast.LENGTH_SHORT).show();
        //notifyDataSetChanged();
        isCollect=true;
    }

    @Override
    public void cancelSuccess(String msg) {
        //hotViewHolder.iv_hot_collect.setImageResource(R.drawable.collect);
        Toast.makeText(App.AppContext, "取消收藏~", Toast.LENGTH_SHORT).show();
        isCollect=false;
        //notifyDataSetChanged();
    }

    @Override
    public void cancelFail(String msg) {

    }

    @Override
    public void pariseSuccess(String msg) {
        isParise=true;
        Toast.makeText(App.AppContext, "赞~", Toast.LENGTH_SHORT).show();
        //hotViewHolder.iv_hot_parise.setImageResource(R.drawable.praised);
        //notifyDataSetChanged();
    }

    @Override
    public void commentSuccess(String msg) {

    }

    @Override
    public void commentFila(String msg) {

    }

    static class HotViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView iv_icon;
        private TextView tv_auth,tv_parise_num,tv_collect_num,tv_share_num,tv_comment_num;
        private TextView tv_time;
        private TextView tv_content;
        private AutoRelativeLayout view;
        private TextView tv_comment;

        private ImageView iv_publish,iv_hot_parise,iv_hot_collect,iv_hot_share,iv_hot_comments;
        private ImageView iv_open;
        private LinearLayout item_menu;
        private LinearLayout lin_player;
        private JZVideoPlayerStandard videoView;
        private TextView tv_talkMore;
        private AutoLinearLayout lin_parise,lin_collect,lin_share,lin_comments;

        public HotViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_hot_icon);
            tv_auth = itemView.findViewById(R.id.tv_hot_auth);
            tv_time = itemView.findViewById(R.id.tv_publishTime);
            tv_content = itemView.findViewById(R.id.tv_hot_content);
            tv_comment = itemView.findViewById(R.id.tv_commentAuth_o);

            iv_publish = itemView.findViewById(R.id.iv_publish);
            iv_open = itemView.findViewById(R.id.iv_open);
            //view=itemView.findViewById(R.id.include_play);
            item_menu = itemView.findViewById(R.id.item_menu);
            // lin_player=itemView.findViewById(R.id.lin_player);
            videoView = itemView.findViewById(R.id.mGiraffe);
            tv_talkMore = itemView.findViewById(R.id.tv_commentMore);
            tv_parise_num=itemView.findViewById(R.id.tv_hot_pariseNum);
            tv_collect_num=itemView.findViewById(R.id.tv_hot_collectNum);
            tv_share_num=itemView.findViewById(R.id.tv_hot_shareNum);
            tv_comment_num=itemView.findViewById(R.id.tv_hot_commentNum);
            lin_parise=itemView.findViewById(R.id.lin_paris);
            lin_collect=itemView.findViewById(R.id.lin_collect);
            lin_share=itemView.findViewById(R.id.lin_share);
            lin_comments=itemView.findViewById(R.id.lin_comments);
            iv_hot_parise=itemView.findViewById(R.id.iv_hot_parise);
            iv_hot_collect=itemView.findViewById(R.id.iv_hot_collect);
            iv_hot_share=itemView.findViewById(R.id.iv_hot_share);
            iv_hot_comments=itemView.findViewById(R.id.iv_hot_comment);
        }
    }

    private void setTalk(HotViewHolder holderTalk, int position, VideoInfo videoInfoTalk) {
        List<VideoInfo.CommentsBean> listTalk = videoInfoTalk.comments;
        if (listTalk != null) {
            if (listTalk.size() >= 1) {
                holderTalk.tv_comment.setVisibility(View.VISIBLE);
                VideoInfo.CommentsBean commentsBean = listTalk.get(0);
                style = new SpannableStringBuilder(commentsBean.nickname + ":" + commentsBean.content);
                style.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")), 0, commentsBean.nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holderTalk.tv_comment.setText(style);
                holderTalk.tv_talkMore.setOnClickListener(this);
                holderTalk.tv_talkMore.setVisibility(View.VISIBLE);
            } else {
                holderTalk.tv_talkMore.setVisibility(View.GONE);

            }
        }
    }


    public onItemClickVideo onItemClickVideo;
    public void setOnItemClickVideo(onItemClickVideo onItemClickVideo){
        this.onItemClickVideo=onItemClickVideo;
    }
    public interface onItemClickVideo{
        void setOnItemClickVideo(VideoInfo videoInfo,int position);
    };


    public void destroy() {
        list.clear();
        list = null;
        viewp = null;
        style = null;
        this.context = null;
        if (hotViewHolder != null) {
            hotViewHolder = null;
        }
        if (onItemClickVideo!=null){
            onItemClickVideo=null;
        }
        if (present!=null){
            present.destory();
        }
        if (showDialog!=null){
            showDialog.dialogDestroy();
            showDialog=null;
        }
    }
}
