package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/11/24.
 * Description：
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Myholder> {

    private Context mContext;
    private String[] strs = new String[100];
    private View view;
    private Myholder myholder;

    public RecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        for (int i = 0; i < 30; i++) {
            strs[i] = i + "";
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
        holder.textView.setText(strs[position]);
    }

    @Override
    public int getItemCount() {
        return strs.length;
    }

    static class Myholder extends RecyclerView.ViewHolder {
        TextView textView;
        public Myholder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tv_item);
        }
    }

    public void destroy(){
        mContext=null;
        strs=null;
        if (view!=null){
            view=null;
        }
        if (myholder!=null){
            myholder=null;
        }
    }

}
