package utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import java.lang.ref.WeakReference;

import bwie.com.basemodule.R;


/**
 * Created by 李英杰 on 2017/11/18.
 * Description：
 */

public class SimpleLoadDialog extends Handler {
    private Dialog dialog;
    private Context context;
    public static final int SHOW_PROGRESS_DIALOG=1;
    public static final int DISMISS_PROGRESS_DIALOG=2;
    private ProgressCancelListener progressCancelListener;
    private boolean cancelable;
    private WeakReference<Context> reference;

    public SimpleLoadDialog(Context context, ProgressCancelListener progressCancelListener, boolean cancelable){
        super();
        this.reference=new WeakReference<Context>(context);
        this.progressCancelListener=progressCancelListener;
        this.cancelable=cancelable;
    }


    private void creat(){
        System.out.println("对话框---"+dialog);
        if (dialog==null){
            context=reference.get();
            System.out.println("对话框Context---"+context);
            dialog=new Dialog(context, R.style.loadstyle);
            View dialogView= LayoutInflater.from(context).inflate(R.layout.custom_sload,null);
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(dialogView);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (progressCancelListener!=null){
                        progressCancelListener.onCancelProgress();
                    }
                }
            });
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        if (context!=null){
            dialog.show();
        }
    }

    public void show(){
        creat();
    }

    public void dismiss(){
        context=reference.get();
        if (dialog!=null && dialog.isShowing() && !((Activity)context).isFinishing()){
            String name = Thread.currentThread().getName();
            dialog.dismiss();
            dialog=null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        int what = msg.what;
        System.out.println("what---"+what);
        switch (what){
            case SHOW_PROGRESS_DIALOG:
                creat();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismiss();
                break;
        }
    }
}
