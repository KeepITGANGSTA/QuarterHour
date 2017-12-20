package adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.jzvd.JZVideoPlayerStandard;
import entity.UserInfoBean;
import entity.VideoInfo;
import present.CollectPresent;
import rx.Subscription;
import utils.ShowDialog;
import view.CollectVideoView;

/**
 * Created by 李英杰 on 2017/12/1.
 * Description：
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.HotViewHolder> {

    private UserInfoBean worksEntities;
    private Context context;
    private View viewp;
    private HotViewHolder hotViewHolder;
    public UserAdapter(UserInfoBean worksEntities, Context context) {
        this.worksEntities = worksEntities;
        this.context = context;

    }

    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewp = LayoutInflater.from(context).inflate(R.layout.user_item, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(App.screen_width, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewp.setLayoutParams(layoutParams);
        hotViewHolder = new HotViewHolder(viewp);
        return hotViewHolder;
    }


    @Override
    public void onBindViewHolder(HotViewHolder holder, int position) {
        UserInfoBean.WorksEntitiesBean worksEntitiesBean = worksEntities.worksEntities.get(position);
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).applyDefaultRequestOptions(options).load(worksEntities.user.icon).into(holder.iv_icon);
        holder.tv_time.setText(worksEntitiesBean.createTime);
        holder.tv_content.setText(worksEntitiesBean.workDesc);
        holder.tv_auth.setText(worksEntities.user.nickname);

        System.out.println("videoView---"+holder.videoView);
        System.out.println("worksEntitiesBean---"+worksEntitiesBean.videoUrl);
        System.out.println("holder---"+holder);
        holder.videoView.setUp(worksEntitiesBean.videoUrl,JZVideoPlayerStandard.SCREEN_WINDOW_LIST,worksEntitiesBean.workDesc==null?" ":worksEntitiesBean.workDesc);
        ImageView thumbImageView = holder.videoView.thumbImageView;
        thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(worksEntitiesBean.cover).into(thumbImageView);
        holder.itemView.setOnClickListener(v -> onItemClickVideo.setOnItemClickVideo(worksEntitiesBean,position));
    }



    @Override
    public int getItemCount() {
        return worksEntities.worksEntities.size();
    }

    static class HotViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView iv_icon;
        private TextView tv_auth;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_comment;
        private JZVideoPlayerStandard videoView;
        private TextView tv_talkMore;

        public HotViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_user_icon_item);
            tv_auth = itemView.findViewById(R.id.tv_user_item_auth);
            tv_time = itemView.findViewById(R.id.tv_user_publishTime);
            tv_content = itemView.findViewById(R.id.tv_user_content);
            tv_comment = itemView.findViewById(R.id.tv_use_commentAuth_o);
            videoView = itemView.findViewById(R.id.user_girffe);
            tv_talkMore = itemView.findViewById(R.id.tv_commentMore);
        }
    }




    public onItemClickVideo onItemClickVideo;
    public void setOnItemClickVideo(onItemClickVideo onItemClickVideo){
        this.onItemClickVideo=onItemClickVideo;
    }
    public interface onItemClickVideo{
        void setOnItemClickVideo(UserInfoBean.WorksEntitiesBean worksEntitiesBean, int position);
    };


    public void destroy() {
        worksEntities = null;
        viewp = null;
        this.context = null;
        if (hotViewHolder != null) {
            hotViewHolder = null;
        }
        if (onItemClickVideo!=null){
            onItemClickVideo=null;
        }
    }
}
