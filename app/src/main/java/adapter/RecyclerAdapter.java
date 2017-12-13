package adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/11/24.
 * Description：
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Myholder> {

    private Context mContext;
    private View view;
    private Myholder myholder;
    private List<Integer> mHeight;

    public RecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        mHeight=new ArrayList<>();
        Random random=new Random();
        for (int i = 0; i < 20; i++) {
            int s=random.nextInt(401)%(401-400+1)+400;
            mHeight.add(s);
            System.out.println("随机数----------"+s);
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
        System.out.println("holder.itemView-------------"+holder.itemView);
        System.out.println("layoutParams--------"+layoutParams);
        holder.itemView.setLayoutParams(layoutParams);
        holder.textView.setImageResource(R.drawable.phonelogin_bg);
    }

    @Override
    public int getItemCount() {
        return mHeight.size();
    }

    static class Myholder extends RecyclerView.ViewHolder {
        private ImageView textView;
        public Myholder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.iv_VideoHot);
        }
    }

    public void destroy(){
        mContext=null;
        if (view!=null){
            view=null;
        }
        if (myholder!=null){
            myholder=null;
        }
    }

}
