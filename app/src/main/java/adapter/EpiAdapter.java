package adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import bwie.com.quarterhour.App;
import bwie.com.quarterhour.PictureDetailsActivity;
import bwie.com.quarterhour.R;
import entity.EpiBean;

/**
 * Created by 李英杰 on 2017/12/4.
 * Description：
 */

public class EpiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<EpiBean> list;
    private LayoutInflater layoutInflater;
    private View view;
    private EpiViewHolder holder;
    private GridLayoutManager gridLayoutManager;
    private EpiItemAdapter adapter;
    private View view0;

    public void refresh(List<EpiBean> lis){
        if (list!=null){
            list.clear();
        }
        this.list=lis;
        notifyDataSetChanged();
        System.out.println("-----------------适配器刷新----------------");
    }

    public EpiAdapter(Context context, List<EpiBean> list) {
        this.context = context;
        this.list = list;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        switch (viewType){
            case 0:
                view0 = layoutInflater.inflate(R.layout.episode_item,null);
                holder=new EpiViewHolder(view0);
                break;
            case 1:
                view0 = layoutInflater.inflate(R.layout.episode_item_one,null);
                holder=new EpiItemViewHolderOne(view0);
                break;
            case 2:
                view0= layoutInflater.inflate(R.layout.episode_item_two,null);
                holder=new EpiItemViewHolderTwo(view0);
                break;
            case 3:
                view0 = layoutInflater.inflate(R.layout.episode_item_more,null);
                holder = new EpiViewHolderMore(view0);
                break;
        }

        return holder;
    }


    @Override
    public int getItemViewType(int position) {
        String imgUrls = (String) list.get(position).imgUrls;
        if (imgUrls==null){
            return 0;
        }else{
            String[] split = imgUrls.split("\\|");
            if (split.length==1){
                return 1;
            }else if (split.length==2){
                return 2;
            }else {
                return 3;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RequestOptions options=new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        EpiBean epiBean=list.get(position);
        if (holder instanceof EpiViewHolder){
            Glide.with(context).applyDefaultRequestOptions(options).load(epiBean.user.icon).into(((EpiViewHolder) holder).iv_icon);
            ((EpiViewHolder) holder).tv_authorName.setText(epiBean.user.nickname);
            ((EpiViewHolder) holder).tv_publishTime.setText(epiBean.createTime);
            ((EpiViewHolder) holder).tv_content.setText(epiBean.content);
        }else if (holder instanceof EpiItemViewHolderOne){
            ((EpiItemViewHolderOne) holder).iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(App.AppContext, "一张图片", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,PictureDetailsActivity.class);
                    intent.putExtra("imgurlOne", (String) epiBean.imgUrls);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
                }
            });
            Glide.with(context).applyDefaultRequestOptions(options).load(epiBean.user.icon).into(((EpiItemViewHolderOne) holder).iv_icon);
            ((EpiItemViewHolderOne) holder).tv_authorName.setText(epiBean.user.nickname);
            ((EpiItemViewHolderOne) holder).tv_publishTime.setText(epiBean.createTime);
            ((EpiItemViewHolderOne) holder).tv_content.setText(epiBean.content);
            Glide.with(App.AppContext).applyDefaultRequestOptions(options).load(epiBean.imgUrls).into(((EpiItemViewHolderOne) holder).iv);
        }else if (holder instanceof EpiItemViewHolderTwo){
            ((EpiItemViewHolderTwo) holder).iv_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,PictureDetailsActivity.class);
                    intent.putExtra("imgurlTwo", (String) epiBean.imgUrls);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
                }
            });
            ((EpiItemViewHolderTwo) holder).iv_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,PictureDetailsActivity.class);
                    intent.putExtra("imgurlTwo", (String) epiBean.imgUrls);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
                }
            });
            Glide.with(context).applyDefaultRequestOptions(options).load(epiBean.user.icon).into(((EpiItemViewHolderTwo) holder).iv_icon);
            ((EpiItemViewHolderTwo) holder).tv_authorName.setText(epiBean.user.nickname);
            ((EpiItemViewHolderTwo) holder).tv_publishTime.setText(epiBean.createTime);
            ((EpiItemViewHolderTwo) holder).tv_content.setText(epiBean.content);
            String imgUrls = (String) epiBean.imgUrls;
            String[] split = imgUrls.split("\\|");
            Glide.with(context).applyDefaultRequestOptions(options).load(split[0]).into(((EpiItemViewHolderTwo) holder).iv_one);
            Glide.with(context).applyDefaultRequestOptions(options).load(split[1]).into(((EpiItemViewHolderTwo) holder).iv_two);
        }else if (holder instanceof EpiViewHolderMore){
            Glide.with(context).applyDefaultRequestOptions(options).load(epiBean.user.icon).into(((EpiViewHolderMore) holder).iv_icon);
            ((EpiViewHolderMore) holder).tv_authorName.setText(epiBean.user.nickname);
            ((EpiViewHolderMore) holder).tv_publishTime.setText(epiBean.createTime);
            ((EpiViewHolderMore) holder).tv_content.setText(epiBean.content);
            gridLayoutManager=new GridLayoutManager(context,3);
            ((EpiViewHolderMore) holder).recyclerView.setLayoutManager(gridLayoutManager);
            String imgUrls = (String) epiBean.imgUrls;
            String[] split = imgUrls.split("\\|");
            adapter = new EpiItemAdapter(context,split);
            ((EpiViewHolderMore) holder).recyclerView.setAdapter(adapter);
            adapter.setOnEpiItemClick(new EpiItemAdapter.OnEpiItemClick() {
                @Override
                public void onEpiItemClick(String[] imgs) {
                    Intent intent=new Intent(context, PictureDetailsActivity.class);
                    intent.putExtra("imgUrlMore",imgs);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                if (holder instanceof EpiViewHolder){
                    flag=0;
                }else if (holder instanceof EpiItemViewHolderOne){
                    flag=1;
                }else if (holder instanceof EpiItemViewHolderTwo){
                    flag=2;
                }else if (holder instanceof EpiViewHolderMore){
                    flag=3;
                }
                epiItemClick.onEpiItemClick(epiBean.jid,flag);
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

    static class EpiViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView iv_icon;
        private TextView tv_authorName;
        private TextView tv_publishTime;
        private TextView tv_content;
        public EpiViewHolder(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.ivEepi_icon);
            tv_authorName=itemView.findViewById(R.id.tvEpi_auth);
            tv_publishTime=itemView.findViewById(R.id.tvEpipublishTime);
            tv_content=itemView.findViewById(R.id.tvEpi_content);
        }
    }
    static class EpiViewHolderMore extends RecyclerView.ViewHolder {
        private RoundedImageView iv_icon;
        private TextView tv_authorName;
        private TextView tv_publishTime;
        private TextView tv_content;
        private RecyclerView recyclerView;
        public EpiViewHolderMore(View itemView) {
            super(itemView);
            recyclerView=itemView.findViewById(R.id.epi_recy);
            iv_icon=itemView.findViewById(R.id.ivEpi_icon_More);
            tv_authorName=itemView.findViewById(R.id.tvEpi_auth_More);
            tv_publishTime=itemView.findViewById(R.id.tvEpipublishTime_More);
            tv_content=itemView.findViewById(R.id.tvEpi_content_More);
        }
    }

    static class EpiItemViewHolderTwo extends RecyclerView.ViewHolder {
        private RoundedImageView iv_icon;
        private TextView tv_authorName;
        private TextView tv_publishTime;
        private TextView tv_content;
        private ImageView iv_one;
        private ImageView iv_two;
        public EpiItemViewHolderTwo(View itemView) {
            super(itemView);
            iv_one=itemView.findViewById(R.id.iv_moreView_one);
            iv_two=itemView.findViewById(R.id.iv_moreView_two);
            iv_icon=itemView.findViewById(R.id.ivEpi_icon_two);
            tv_authorName=itemView.findViewById(R.id.tvEpi_auth_two);
            tv_publishTime=itemView.findViewById(R.id.tvEpipublishTime_two);
            tv_content=itemView.findViewById(R.id.tvEpi_content_two);
        }
    }

    static class EpiItemViewHolderOne extends RecyclerView.ViewHolder {
        private RoundedImageView iv_icon;
        private TextView tv_authorName;
        private TextView tv_publishTime;
        private TextView tv_content;
        private ImageView iv;
        public EpiItemViewHolderOne(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.ivEpimoreView_one);
            iv_icon=itemView.findViewById(R.id.ivEpi_Iconone);
            tv_authorName=itemView.findViewById(R.id.tvEpi_auth_one);
            tv_publishTime=itemView.findViewById(R.id.tvEpipublishTime_one);
            tv_content=itemView.findViewById(R.id.tvEpi_content_one);
        }
    }

    public void destroy(){
        if (adapter!=null){
            adapter.destroy();
            adapter=null;
        }
        context=null;
        list.clear();
        view=null;
        layoutInflater=null;
        if (gridLayoutManager!=null){
            gridLayoutManager=null;
        }
        holder=null;
        view0=null;
    }


    public EpiItemClick epiItemClick;
    public void setEpiItemClick(EpiItemClick epiItemClic){
        this.epiItemClick=epiItemClic;
    }
    public interface EpiItemClick{
        void onEpiItemClick(int jid,int flag);
    };


}
