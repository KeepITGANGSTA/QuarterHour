package adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import bwie.com.quarterhour.App;
import bwie.com.quarterhour.PictureDetailsActivity;
import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/12/4.
 * Description：
 */

public class EpiItemAdapter extends RecyclerView.Adapter<EpiItemAdapter.EpiItemViewHolder> {

    private Context context;
    private String[] iv;
    private LayoutInflater inflater;
    private View view;
    private EpiItemViewHolder holder;

    public EpiItemAdapter(Context context, String[] iv) {
        this.context = context;
        this.iv = iv;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public EpiItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.epi_item_one,null);
        holder = new EpiItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EpiItemViewHolder holder, int position) {
        RequestOptions options=new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).applyDefaultRequestOptions(options).load(iv[position]).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEpiItemClick.onEpiItemClick(iv);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iv.length;
    }

    static class EpiItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public EpiItemViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_grid_item);
        }
    }

    public OnEpiItemClick onEpiItemClick;
    public void setOnEpiItemClick(OnEpiItemClick onEpiItemClick){
        this.onEpiItemClick=onEpiItemClick;
    }
    public interface OnEpiItemClick{
        void onEpiItemClick(String[] imgs);
    }


    public void destroy(){
        context=null;
        iv=null;
        inflater=null;
        holder=null;
        view=null;
    }


}
