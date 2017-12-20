package utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.zhy.autolayout.AutoLinearLayout;

import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/12/18.
 * Description：
 */

public class ShowDialog implements View.OnClickListener{
    private  View inflate;
    private  AutoLinearLayout lin_share_qq,lin_share_qqZone,lin_share_friend,lin_share_weixin;
    private  Button btn_cancel_share;
    private  Dialog dialog;
    private Context context;

    public ShowDialog(Context context) {
        this.context=context;
    }

    public void showDialog(){

            dialog = new Dialog(context, R.style.DialogSelect);
            inflate = LayoutInflater.from(context).inflate(R.layout.hot_share_layout, null);
        lin_share_qq = inflate.findViewById(R.id.lin_share_qq);
        lin_share_qqZone =  inflate.findViewById(R.id.lin_share_qqZone);
        lin_share_friend =inflate.findViewById(R.id.lin_share_friend);
        lin_share_weixin=inflate.findViewById(R.id.lin_share_weixin);
        btn_cancel_share=inflate.findViewById(R.id.btn_cancel_share);

        lin_share_qq.setOnClickListener(this);
        lin_share_qqZone.setOnClickListener(this);
        lin_share_friend.setOnClickListener(this);
        lin_share_weixin.setOnClickListener(this);
        btn_cancel_share.setOnClickListener(this);

            dialog.setContentView(inflate);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity( Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = 10;
            lp.width= App.screen_width-20;
            dialogWindow.setAttributes(lp);
            dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_share_qq:
                Toast.makeText(App.AppContext, "分享至QQ好友", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.lin_share_qqZone:
                Toast.makeText(App.AppContext, "分享至QQ空间", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.lin_share_friend:
                Toast.makeText(App.AppContext, "分享至朋友圈", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.lin_share_weixin:
                Toast.makeText(App.AppContext, "分享至微信", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            case R.id.btn_cancel_share:
                dialog.dismiss();
                break;
        }
    }

    public void dialogDestroy(){
        context=null;
        if (inflate!=null){
            inflate=null;
        }
        if (dialog!=null){
            dialog.dismiss();
            dialog.cancel();
            dialog=null;
        }
    }

}
