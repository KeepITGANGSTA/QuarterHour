package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.youth.banner.Banner;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import adapter.BannerImage;
import adapter.HeadBase;
import adapter.HotAdapter;
import adapter.RecyclerAdapter;
import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;
import entity.AdBean;
import entity.BaseEntity;
import rx.Observable;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;


/**
 * Created by 李英杰 on 2017/11/25.
 * Description：
 */

public class HotFragment extends Fragment {

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject=PublishSubject.create();
    private View mRoot;
    private RecyclerView recyclerView;
    private SpringView sv;
    private List<Object> list=new ArrayList<>();
    private List<Integer> banneriv=new ArrayList<Integer>(){{
        add(R.drawable.banner_one);
        add(R.drawable.banner_two);
        add(R.drawable.banner_three);
        add(R.drawable.banner_four);
    }};
    private Banner b;
    private View banner;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private HotAdapter adapter;
    private LinearLayoutManager linearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.hot_fragment,container,false);
        }
        ViewGroup parent = (ViewGroup) mRoot.getParent();
        if (parent!=null){
            parent.removeView(mRoot);
        }
        linearLayoutManager = new LinearLayoutManager(getActivity());
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
//        Observable ad = apiService.getAd("android");
//        HttpUtils.getInstace().toSubscribe(ad, new ProgressSubscriber<AdBean>(getContext()) {
//            @Override
//            public void _Next(AdBean o) {
//                System.out.println("---o");
//            }
//            @Override
//            public void _OnError(String msg) {
//                System.out.println("广告请求失败---"+msg);
//            }
//        },"adCache", ActivityLifeCycleEvent.PAUSE,lifecycleSubject,true,false);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = mRoot.findViewById(R.id.hot_recyclerView);
        sv=mRoot.findViewById(R.id.sv);
        //RecyclerAdapter adapter = new RecyclerAdapter(getActivity().getApplicationContext());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        banner = LayoutInflater.from(getContext()).inflate(R.layout.banner_head,null);
        b = banner.findViewById(R.id.mBanner);
        b.setImageLoader(new BannerImage());
        b.setImages(banneriv);
        b.start();
        adapter = new HotAdapter(list,getContext());
        headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        headerAndFooterWrapper.addHeaderView(banner);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(headerAndFooterWrapper);
        sv.setHeader(new HeadBase(getContext()));
        sv.setFooter(new DefaultFooter(getContext()));
        sv.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
            }
        });
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onDestroy();
        list.clear();
        banner=null;
        banneriv.clear();
        banneriv=null;
        b=null;
        adapter.destroy();
        headerAndFooterWrapper=null;
        adapter=null;
        sv=null;
        linearLayoutManager=null;
        mRoot=null;
    }




}
