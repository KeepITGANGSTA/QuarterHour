package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bwie.com.quarterhour.R;


/**
 * Created by 李英杰 on 2017/11/28.
 * Description：
 */

public class LeftItemAdapter extends RecyclerView.Adapter<LeftItemAdapter.ItemViewHolder>{

    private Context context;
    private List<String> title;
    private List<Integer> ivlist;
    private View view;
    private ItemViewHolder holder;

    public LeftItemAdapter(Context context, List<String> title, List<Integer> ivlist) {
        this.context = context;
        this.title = title;
        this.ivlist = ivlist;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.drawerlayout_item,null);
        holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.textView.setText(title.get(position));
        holder.iv.setImageResource(ivlist.get(position));
       // holder.itemView.setOnClickListener(v -> onItemClick.onItemClick(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv_left_item);
            textView=itemView.findViewById(R.id.tv_left_item);
        }
    }

    public OnItemClick onItemClick;
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick=onItemClick;
    }
    public interface OnItemClick{
        void onItemClick(int id);
    }


    public void adapterDestroy(){
        this.context=null;
        title.clear();
        ivlist.clear();
        if (view!=null){
            view=null;
        }
        if (holder!=null){
            holder=null;
        }

    }

}
