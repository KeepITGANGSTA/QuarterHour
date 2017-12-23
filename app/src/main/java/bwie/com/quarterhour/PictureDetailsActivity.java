package bwie.com.quarterhour;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import api.Common;
import bwie.com.basemodule.NetAval;
import service.ImgIntentService;

public class PictureDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private List<String> list;
    private ImageView iv_load_img,iv_back;
    private ViewPagerAdapter adapter;
    private String imgurlOne;
    private String imgurlTwo;
    private String[] imgurlMore;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_details);
        initView();


        if (NetAval.NetAvailable(App.AppContext)){
            System.out.println("------网络可用");
            imgurlOne = getIntent().getStringExtra("imgurlOne");
            imgurlTwo = getIntent().getStringExtra("imgurlTwo");
            imgurlMore=getIntent().getStringArrayExtra("imgUrlMore");
            if (!TextUtils.isEmpty(imgurlOne)){
                System.out.println("-----------一张图");
                list.add(imgurlOne);
            }else if (!TextUtils.isEmpty(imgurlTwo)){
                System.out.println("-----------二张图");
                String[] imgs= imgurlTwo.split("\\|");
                list.add(imgs[0]);
                list.add(imgs[1]);
            }else if (imgurlMore!=null && imgurlMore.length!=0){
                System.out.println("-----------三张图");
                for (String more : imgurlMore) {
                    list.add(more);
                }
            }
            adapter = new ViewPagerAdapter(this,list);
            viewPager.setAdapter(adapter);
        }else {
            System.out.println("网络不可用");
        }
    }

    private void initView() {
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#000000"));
        list=new ArrayList<>();
        viewPager=findViewById(R.id.view_pager);
        iv_load_img=findViewById(R.id.iv_ivDetails_load);
        iv_back=findViewById(R.id.iv_ivDetails_back);
        iv_back.setOnClickListener(this);
        iv_load_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_ivDetails_back:
                finishAfterTransition();
                break;
            case R.id.iv_ivDetails_load:
                if (!TextUtils.isEmpty(imgurlOne)){
                    loadImg(imgurlOne);
                }else if (!TextUtils.isEmpty(imgurlTwo)){
                    loadImg(list.get(viewPager.getCurrentItem()));
                }else if (imgurlMore!=null && imgurlMore.length!=0){
                    loadImg(list.get(viewPager.getCurrentItem()));
                }
                break;
        }
    }

    private void loadImg(String ivUrl) {
        if(intent==null){
            intent = new Intent(PictureDetailsActivity.this, ImgIntentService.class);
        }
        intent.putExtra("ivUrl",ivUrl);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter!=null){
            adapter.destroy();
            adapter=null;
        }
        if (list!=null){
            list.clear();
            list=null;
        }
        intent=null;
    }
}
