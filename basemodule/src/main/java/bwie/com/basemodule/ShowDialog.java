package bwie.com.basemodule;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by 李英杰 on 2017/11/14.
 * Description：
 */

public class ShowDialog {
    private ShowDialog(){
    }
    private ProgressDialog progressDialog;
    private static class DialogUtilsHolder{
        static ShowDialog DialogUtils = new ShowDialog();
    }
    public static ShowDialog getInstance(){
        return  DialogUtilsHolder.DialogUtils;
    }

    public void show(Context context, String msg){
        close();
        createProgressDialog(context,msg);
        if (progressDialog!=null&&!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    private void createProgressDialog(Context context,String msg) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(msg);

    }

    public void close(){

        if (progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }
}
