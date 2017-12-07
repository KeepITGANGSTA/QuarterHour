package adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
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
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;
import entity.VideoInfo;

/**
 * Created by 李英杰 on 2017/12/1.
 * Description：
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.HotViewHolder> {

    private List<VideoInfo> list;
    private Context context;
    private PlayerView playerView;
    private View viewp;
    private SpannableStringBuilder style;
    private HotViewHolder hotViewHolder;

    public HotAdapter(List<VideoInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<VideoInfo> t){
        if (list!=null){
            list.clear();
        }
        list=t;
        notifyDataSetChanged();
    }

    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder---------------");
        viewp = LayoutInflater.from(context).inflate(R.layout.hot_item,null);
        LinearLayout.LayoutParams layoutParams =new  LinearLayout.LayoutParams(App.screen_width, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewp.setLayoutParams(layoutParams);
        hotViewHolder = new HotViewHolder(viewp);
        return hotViewHolder;
    }
    @Override
    public void onBindViewHolder(HotViewHolder holder, int position) {
        RequestOptions options=new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        System.out.println("onBindViewHolder---------------"+holder.tv_auth);
        VideoInfo videoInfo = list.get(position);
        Glide.with(context).applyDefaultRequestOptions(options).load(videoInfo.user.icon).into(holder.iv_icon);
        holder.tv_time.setText(videoInfo.createTime);
        holder.tv_content.setText(videoInfo.workDesc);
        holder.tv_auth.setText(videoInfo.user.nickname);
        //holder.view.setVisibility(View.VISIBLE);
        style = new SpannableStringBuilder("用户一:还行吧");
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")),0,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_comment.setText(style);
        holder.tv_conmment2.setText(style);

//        holder.iv_publish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
//                if ( holder.item_menu.getVisibility()==View.GONE){
//                    holder.item_menu.setVisibility(View.VISIBLE);
//                }else {
//                    holder.item_menu.setVisibility(View.GONE);
//                }
//            }
//        });
//        String url=videoInfo.videoUrl.replace("https://www.zhaoapi.cn","http://120.27.23.105");
//        playerView=new PlayerView((Activity) context,holder.itemView)
//                .setTitle("什么")
//                .setScaleType(PlayStateParams.fitparent)
//                .hideMenu(true)
//                .forbidTouch(false)
//                .showThumbnail(new OnShowThumbnailListener() {
//                    @Override
//                    public void onShowThumbnail(ImageView ivThumbnail) {
//                        Glide.with(App.AppContext).applyDefaultRequestOptions(options)
//                                .load(videoInfo.cover)
//                                .into(ivThumbnail);
//                    }
//                }).setPlaySource(url)
//                .startPlay();

//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
//                if ( holder.item_menu.getVisibility()==View.GONE){
//                    holder.item_menu.setVisibility(View.VISIBLE);
//                }else {
//                    holder.item_menu.setVisibility(View.GONE);
//                }
//                System.out.println("ikjPlayer---"+playerView);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HotViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView iv_icon;
        private TextView tv_auth;
        private TextView tv_time;
        private TextView tv_content;
        private View view;
        private TextView tv_comment;
        private TextView tv_conmment2;
        private ImageView iv_publish;
        private ImageView iv_open;
        private LinearLayout item_menu;
        private LinearLayout lin_player;
        public HotViewHolder(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.iv_hot_icon);
            tv_auth=itemView.findViewById(R.id.tv_hot_auth);
            tv_time=itemView.findViewById(R.id.tv_publishTime);
            tv_content=itemView.findViewById(R.id.tv_hot_content);
            tv_comment=itemView.findViewById(R.id.tv_commentAuth_o);
            tv_conmment2=itemView.findViewById(R.id.tv_commentAuth_t);
            iv_publish=itemView.findViewById(R.id.iv_publish);
            iv_open=itemView.findViewById(R.id.iv_open);
          //  view=itemView.findViewById(R.id.include_play);
            item_menu=itemView.findViewById(R.id.item_menu);
           // lin_player=itemView.findViewById(R.id.lin_player);
        }
    }

    public void destroy(){
        list.clear();
        list=null;
        viewp=null;
        style=null;
        this.context=null;
        if (hotViewHolder!=null){
            hotViewHolder=null;
        }
        if (playerView!=null){
            playerView.onDestroy();
            playerView=null;
        }

    }
}
