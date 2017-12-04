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
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/12/1.
 * Description：
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.HotViewHolder> {

    private List<Object> list;
    private Context context;
    private PlayerView playerView;
    private View viewp;
    private SpannableStringBuilder style;
    private HotViewHolder hotViewHolder;

    public HotAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewp = LayoutInflater.from(context).inflate(R.layout.hot_item,null);
        LinearLayout.LayoutParams layoutParams =new  LinearLayout.LayoutParams(App.screen_width, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewp.setLayoutParams(layoutParams);
        hotViewHolder = new HotViewHolder(viewp);
        return hotViewHolder;
    }

    @Override
    public void onBindViewHolder(HotViewHolder holder, int position) {
        holder.iv_icon.setImageResource(R.mipmap.nhdz);
        holder.tv_time.setText("2017.12.1");
        holder.tv_content.setText("天气美美的，感觉草草哒");
        holder.view.setVisibility(View.VISIBLE);
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
//        playerView=new PlayerView((Activity) viewp.getContext())
//                .setTitle("什么")
//                .setScaleType(PlayStateParams.fitparent)
//                .hideMenu(true)
//                .forbidTouch(false)
//                .showThumbnail(new OnShowThumbnailListener() {
//                    @Override
//                    public void onShowThumbnail(ImageView ivThumbnail) {
//                        Glide.with(context)
//                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
//                                .placeholder(R.mipmap.ic_launcher)
//                                .error(R.mipmap.ic_launcher_round)
//                                .into(ivThumbnail);
//                    }
//                }).setPlaySource("http://9890.vod.myqcloud.com/9890_9c1fa3e2aea011e59fc841df10c92278.f20.mp4")
//                .startPlay();
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                if ( holder.item_menu.getVisibility()==View.GONE){
                    holder.item_menu.setVisibility(View.VISIBLE);
                }else {
                    holder.item_menu.setVisibility(View.GONE);
                }
                System.out.println("ikjPlayer---"+playerView);
            }
        });
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
            view=itemView.findViewById(R.id.include_play);
            item_menu=itemView.findViewById(R.id.item_menu);
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

    }
}
