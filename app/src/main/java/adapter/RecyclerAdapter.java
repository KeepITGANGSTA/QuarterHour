package adapter;

import android.content.Context;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;
import entity.VideoInfo;

/**
 * Created by 李英杰 on 2017/11/24.
 * Description：
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Myholder> {

    private Context mContext;
    private View view;
    private Myholder myholder;
    private List<Integer> mHeight;
    private List<VideoInfo> list;

    public RecyclerAdapter(Context mContext, List<VideoInfo> list) {
        this.mContext = mContext;
        this.list=list;
        mHeight=new ArrayList<>();
        Random random=new Random();
        for (int i = 0; i < 20; i++) {
            int s=random.nextInt(300)%(300-250+1)+300;
            int i1 = dip2px(mContext, s);
            mHeight.add(i1);
            System.out.println("随机数----------"+i1);
        }
    }


    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, null);
        myholder = new Myholder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(App.screen_width/2,mHeight.get(position));
        holder.itemView.setLayoutParams(layoutParams);
        holder.textView.setImageResource(R.drawable.phonelogin_bg);

        VideoInfo videoInfo = list.get(position);
        String coverImg = videoInfo.cover;
        Glide.with(mContext).load(coverImg).into(holder.textView);
        System.out.println("视频标题---"+videoInfo.workDesc);
        holder.tv_title.setText(videoInfo.workDesc);
        //getRingDuring(videoInfo.videoUrl);

    }

    public static String getRingDuring(String mUri){
        System.out.println("视频地址----"+mUri);
        String duration=null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();

        try {
            if (mUri != null) {
                HashMap<String, String> headers=null;
                if (headers == null) {
                    headers = new HashMap<String, String>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                }
                mmr.setDataSource(mUri, headers);
            }

            duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
        } finally {
            mmr.release();
        }
        //LogUtil.e("ryan","duration "+duration);
        System.out.println("ryan,duration---"+duration);
        return duration;
    }

/*    private String getVideoTotalTime(String videoUrl) {
//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        System.out.println("视频地址！！！！！！！！！！！！！！"+videoUrl);
//        mmr.setDataSource(videoUrl);
//        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);


        return format1;
    }*/

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Myholder extends RecyclerView.ViewHolder {
        private ImageView textView;
        private ImageView iv_centerPlay;
        private TextView tv_title;
        private TextView tv_TotalTime;
        public Myholder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.iv_VideoHot);
            iv_centerPlay=itemView.findViewById(R.id.iv_centerPlay);
            tv_title=itemView.findViewById(R.id.tv_videoTitle);
            tv_TotalTime=itemView.findViewById(R.id.tv_totalTime);
        }
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void destroy(){
        mContext=null;
        if (view!=null){
            view=null;
        }
        if (myholder!=null){
            myholder=null;
        }
        if (mHeight!=null){
            mHeight.clear();
        }
    }

}
