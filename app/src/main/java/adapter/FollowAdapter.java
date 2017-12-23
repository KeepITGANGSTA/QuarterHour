package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import bwie.com.quarterhour.R;
import entity.FollowBean;
import glide.GlideCache;

/**
 * Created by 李英杰 on 2017/12/20.
 * Description：
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {

    private Context context;
    private List<FollowBean> list;
    private LayoutInflater inflater;

    public FollowAdapter(Context context, List<FollowBean> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.follow_item,null);
        FollowViewHolder holder=new FollowViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FollowViewHolder holder, int position) {
        FollowBean followBean=list.get(position);
        Glide.with(context).applyDefaultRequestOptions(GlideCache.NoMemoryDiskCache()).load(followBean.icon).into(holder.iv_icon);
        holder.tv_nickName.setText(followBean.nickname);
        holder.tv_create_time.setText(followBean.createtime);
        holder.itemView.setOnClickListener(v -> followItemClick.onFollowItemClick(followBean.uid));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class FollowViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView iv_icon;
        private TextView tv_nickName,tv_create_time;
        public FollowViewHolder(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.iv_follow_icon);
            tv_nickName=itemView.findViewById(R.id.tv_follow_nickName);
            tv_create_time=itemView.findViewById(R.id.tv_follow_createTime);
        }
    }

    public FollowItemClick followItemClick;
    public void setFollowItemClick(FollowItemClick followItemClick){
        this.followItemClick=followItemClick;
    }
    public interface FollowItemClick{
        void onFollowItemClick(int uid);
    }

    public void destroy(){
        inflater=null;
        if (list!=null){
            list.size();
        }
    }
}
