package adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liaoinstan.springview.container.BaseHeader;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/12/1.
 * Description：
 */

public class HeadBase extends BaseHeader {


    private RoundedImageView imageView;
    private View view;
    private TextView tv_duan,tv_you,tv_chu,tv_zheng,tv_cun,tv_cao,tv_bu,tv_sheng;
    private AutoLinearLayout linearLayout;



    private int id=0;
    private Timer timer;
    private TimerTask tt;
    private ObjectAnimator rotation;
    private List<ObjectAnimator> list;
    private Context context;

    public HeadBase(Context context) {
        this.context=context;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fresh_head,viewGroup,true);
        linearLayout = view.findViewById(R.id.lin_head);
        imageView = view.findViewById(R.id.id_fresh_head);
        tv_duan=view.findViewById(R.id.tv_duan);
        tv_you=view.findViewById(R.id.tv_you);
        tv_chu=view.findViewById(R.id.tv_chu);
        tv_zheng=view.findViewById(R.id.tv_zheng);
        tv_cun=view.findViewById(R.id.tv_cun);
        tv_cao=view.findViewById(R.id.tv_cao);
        tv_bu=view.findViewById(R.id.tv_bu);
        tv_sheng=view.findViewById(R.id.tv_sheng);
        return view;
    }

    @Override
    public void onPreDrag(View rootView) {
        System.out.println("---------准备");
        ObjectAnimator duan = ObjectAnimator.ofFloat(tv_duan, "translationY", tv_duan.getTranslationY(), tv_duan.getTranslationY() - 30, tv_duan.getTranslationY());
        ObjectAnimator you = ObjectAnimator.ofFloat(tv_you, "translationY", tv_you.getTranslationY(), tv_you.getTranslationY() - 30, tv_you.getTranslationY());
        ObjectAnimator chu = ObjectAnimator.ofFloat(tv_chu, "translationY", tv_chu.getTranslationY(), tv_chu.getTranslationY() - 30, tv_chu.getTranslationY());
        ObjectAnimator zheng = ObjectAnimator.ofFloat(tv_zheng, "translationY", tv_zheng.getTranslationY(), tv_zheng.getTranslationY() - 30, tv_zheng.getTranslationY());
        ObjectAnimator cun = ObjectAnimator.ofFloat(tv_cun, "translationY", tv_cun.getTranslationY(), tv_cun.getTranslationY() - 30, tv_cun.getTranslationY());
        ObjectAnimator cao = ObjectAnimator.ofFloat(tv_cao, "translationY", tv_cao.getTranslationY(), tv_cao.getTranslationY() - 30, tv_cao.getTranslationY());
        ObjectAnimator bu = ObjectAnimator.ofFloat(tv_bu, "translationY", tv_bu.getTranslationY(), tv_bu.getTranslationY() - 30, tv_bu.getTranslationY());
        ObjectAnimator sheng = ObjectAnimator.ofFloat(tv_sheng, "translationY", tv_sheng.getTranslationY(), tv_sheng.getTranslationY() - 30, tv_sheng.getTranslationY());
        list = new ArrayList<ObjectAnimator>(){{
            add(duan);
            add(you);
            add(chu);
            add(zheng);
            add(cun);
            add(cao);
            add(bu);
            add(sheng);
        }};
    }
    @Override
    public void onDropAnim(View rootView, int dy) {
        System.out.println("---------下拉==="+dy);
        if (dy<170) {
            System.out.println("---------下拉界限==="+dy);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(dy, dy);
            imageView.setLayoutParams(ll);
//            tv_duan.setLayoutParams(ll);
//            tv_you.setLayoutParams(ll);
//            tv_chu.setLayoutParams(ll);
//            tv_zheng.setLayoutParams(ll);
//            tv_cun.setLayoutParams(ll);
//            tv_cao.setLayoutParams(ll);
//            tv_bu.setLayoutParams(ll);
//            tv_sheng.setLayoutParams(ll);
        }
    }

    @Override
    public int getDragLimitHeight(View rootView) {
        return 230;
    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
    }



    @Override
    public void onStartAnim() {
        System.out.println("-------------开始");
        rotation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotation.setDuration(1000);
        rotation.setRepeatCount(Animation.INFINITE);
        rotation.start();
        if (list!=null){
            timer = new Timer();
            tt = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("i-----d"+id);
                    if (id<8){
                        System.out.println("id----------"+id);
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ObjectAnimator objectAnimator = list.get(id);
                                objectAnimator.setDuration(300);
                                objectAnimator.start();
                                id++;
                            }
                        });
                    }
                    if (id==8){
                        id=0;
                    }
                }
            };
            timer.schedule(tt,0,300);
        }



    }

    @Override
    public void onFinishAnim() {
        if (timer!=null){
            timer.cancel();
            timer=null;
            tt.cancel();
            tt=null;
        }
        System.out.println("---------结束");
        if (rotation!=null){
            rotation.cancel();
            rotation=null;
        }
        if (list!=null){
            for (int i = 0; i < id; i++) {
                ObjectAnimator objectAnimator = list.get(i);
                objectAnimator.cancel();
                objectAnimator=null;
            }
            list.clear();
            list=null;
        }
        view=null;
        linearLayout=null;
    }

    public void setContext(){
        context=null;
    }

}
