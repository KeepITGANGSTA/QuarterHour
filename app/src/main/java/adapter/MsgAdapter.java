package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/12/21.
 * Description：
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder> {

    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;

    public MsgAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.msg_item_layout,null);
        MsgViewHolder holder=new MsgViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MsgViewHolder holder, int position) {


        holder.itemView.setOnLongClickListener(v -> {
            msgLongClick.onMsgLongClick(position);
            return true;
        });
        holder.itemView.setOnClickListener(v -> msgItemClick.onMsgItemClick(position));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    static class MsgViewHolder extends RecyclerView.ViewHolder {

        public MsgViewHolder(View itemView) {
            super(itemView);
        }
    }

    private MsgLongClick msgLongClick;
    public void setMsgLongClick( MsgLongClick msgLongClick){
        this.msgLongClick=msgLongClick;
    }
    public interface MsgLongClick{
        void onMsgLongClick(int position);
    }

    private MsgItemClick msgItemClick;
    public void setMsgItemClick(MsgItemClick msgItemClick){
        this.msgItemClick=msgItemClick;
    }
    public interface MsgItemClick{
        void onMsgItemClick(int position);
    }

    public void destroy(){
        inflater=null;
        context=null;
        if (list!=null){
            list.clear();
        }
    }

}
