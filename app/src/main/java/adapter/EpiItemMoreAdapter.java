package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/12/4.
 * Description：
 */

public class EpiItemMoreAdapter extends RecyclerView.Adapter<EpiItemMoreAdapter.EpiItemMoreViewHolder> {

    private Context context;
    private String[] iv;
    private LayoutInflater layoutInflater;
    private View view;
    private EpiItemMoreViewHolder holder;

    public EpiItemMoreAdapter(Context context, String[] iv) {
        this.context = context;
        this.iv = iv;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public EpiItemMoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.epi_item_one,null);
        holder = new EpiItemMoreViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EpiItemMoreViewHolder holder, int position) {
        RequestOptions options=new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(App.AppContext).applyDefaultRequestOptions(options).load(iv[position]).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return iv.length;
    }

    static class EpiItemMoreViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        public EpiItemMoreViewHolder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv_itemMore);
        }
    }

    public void destroy(){
        context=null;
        iv=null;
        layoutInflater=null;
        view=null;
        holder=null;
    }
}
