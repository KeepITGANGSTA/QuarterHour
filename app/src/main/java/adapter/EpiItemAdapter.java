package adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import bwie.com.quarterhour.App;
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
        Glide.with(context).load(iv[position]).into(holder.imageView);
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




    public void destroy(){
        context=null;
        iv=null;
        inflater=null;
    }


}
