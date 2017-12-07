package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.orhanobut.hawk.Hawk;
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
import bwie.com.basemodule.SharedPreferencesUtil;
import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;
import entity.AdBean;
import entity.BaseEntity;
import entity.EpiBean;
import entity.VideoInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
    private String uid="";
    private int startPage=1;
    private ProgressSubscriber videoSubscriber;
    private List<VideoInfo> videoCache;
    private List<VideoInfo> refreshList;
    private Subscription videoSub;
    private Api apiService;
    private Subscription moreSub;

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
        apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
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

        String SpIid = SharedPreferencesUtil.getPreferencesValue("uid");
        if (!TextUtils.isEmpty(uid)){
            uid=SpIid;
        }
        recyclerView = mRoot.findViewById(R.id.hot_recyclerView);
        sv=mRoot.findViewById(R.id.sv);
        banner = LayoutInflater.from(getContext()).inflate(R.layout.banner_head,null);
        b = banner.findViewById(R.id.mBanner);
        b.setImageLoader(new BannerImage());
        b.setImages(banneriv);
        b.start();
        sv.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                videoSub = apiService.getVideos(uid, "1", 1 + "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BaseEntity<List<VideoInfo>>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("刷新失败---" + e.toString());
                            }

                            @Override
                            public void onNext(BaseEntity<List<VideoInfo>> listBaseEntity) {
                                System.out.println("刷新成功---" + listBaseEntity.data.size());
                                if (refreshList != null) {
                                    refreshList.clear();
                                }
                                refreshList = listBaseEntity.data;
                                adapter.setList(refreshList);
                                sv.onFinishFreshAndLoad();
                            }
                        });
            }
            @Override
            public void onLoadmore() {
                startPage++;
                moreSub = apiService.getVideos(uid, "1", startPage + "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BaseEntity<List<VideoInfo>>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("刷新失败---" + e.toString());
                            }

                            @Override
                            public void onNext(BaseEntity<List<VideoInfo>> listBaseEntity) {
                                System.out.println("刷新成功---" + listBaseEntity.data.size());
                                adapter.setList(refreshList);
                                for (VideoInfo datum : listBaseEntity.data) {
                                    refreshList.add(datum);
                                }
                                adapter.notifyDataSetChanged();
                                sv.onFinishFreshAndLoad();
                            }
                        });
            }
        });
        refreshList = Hawk.get("videoCache");
        if (videoCache==null || videoCache.size()==0){
            getVideos();
        }else {
            adapter = new HotAdapter(refreshList,getContext());
            headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
            headerAndFooterWrapper.addHeaderView(banner);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(headerAndFooterWrapper);
            sv.setHeader(new HeadBase(getContext()));
            sv.setFooter(new DefaultFooter(getContext()));
        }
        System.out.println("视频缓存数据-----"+ videoCache);
    }

    private void getVideos() {
        videoSubscriber = new ProgressSubscriber<List<VideoInfo>>(getContext()) {
            @Override
            public void _Next(List<VideoInfo> o) {
                refreshList=o;
                adapter = new HotAdapter(refreshList,getContext());
                headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
                headerAndFooterWrapper.addHeaderView(banner);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(headerAndFooterWrapper);
                sv.setHeader(new HeadBase(getContext()));
                sv.setFooter(new DefaultFooter(getContext()));
            }
            @Override
            public void _OnError(String msg) {
                System.out.println("视频请求失败---"+msg);
            }
        };
        Observable video = apiService.getVideos(uid,"1",startPage+"");
        HttpUtils.getInstace().toSubscribe(video, videoSubscriber,"videoCache", ActivityLifeCycleEvent.STOP,lifecycleSubject,true,true);
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onDestroy();
        if (videoSubscriber!=null){
            videoSubscriber.unsubscribe();
            videoSubscriber=null;
        }
        if (videoCache!=null){
            videoCache.clear();
        }
        banner=null;
        banneriv.clear();
        banneriv=null;
        b=null;
        if (adapter!=null){
            adapter.destroy();
        }
        headerAndFooterWrapper=null;
        adapter=null;
        sv=null;
        linearLayoutManager=null;
        mRoot=null;
        if (videoSub!=null){
            videoSub.unsubscribe();
            videoSub=null;
        }
        apiService=null;
        if (moreSub!=null){
            moreSub.unsubscribe();
            moreSub=null;
        }

    }




}
