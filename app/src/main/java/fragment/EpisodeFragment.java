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
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.EpiAdapter;
import adapter.HeadBase;
import adapter.RecyclerAdapter;
import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;
import entity.BaseEntity;
import entity.EpiBean;
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

public class EpisodeFragment extends Fragment {

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject=PublishSubject.create();
    private View mRoot;
    private RecyclerView episode_recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ProgressSubscriber progressSubscriber;
    private EpiAdapter adaptr;
    private SpringView sv;
    private Timer timer;
    private TimerTask tt;
    private int startPage=1;
    private List<EpiBean> epiBeanList;
    private Subscription subscribe;
    private HeadBase headBase;
    private DefaultFooter footer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.episode_view,container,false);
        }
        ViewGroup parent= (ViewGroup) mRoot.getParent();
        if (parent!=null){
            parent.removeView(mRoot);
        }
        linearLayoutManager = new LinearLayoutManager(getActivity());
        return mRoot;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sv=mRoot.findViewById(R.id.sv_epi);
        headBase=new HeadBase(getContext());
        footer = new DefaultFooter(getContext());
        sv.setHeader(headBase);
        sv.setFooter(footer);
        sv.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                timer = new Timer();
                tt = new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(() -> sv.onFinishFreshAndLoad());
                    }
                };
                timer.schedule(tt,1500);
            }

            @Override
            public void onLoadmore() {
                Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
                subscribe = apiService.getEpi((startPage + 1) + "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BaseEntity<List<EpiBean>>>() {
                            @Override
                            public void onCompleted() {
                                sv.onFinishFreshAndLoad();
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("分也失败-----"+e.toString());
                            }

                            @Override
                            public void onNext(BaseEntity<List<EpiBean>> listBaseEntity) {
                                List<EpiBean> data = listBaseEntity.data;
                                for (EpiBean datum : data) {
                                    epiBeanList.add(datum);
                                }
                                adaptr.notifyDataSetChanged();
                                System.out.println("分页成功---"+data.size());
                            }
                        });
            }
        });
        episode_recyclerView=mRoot.findViewById(R.id.episode_recyclerView);
        Api apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        Observable epi = apiService.getEpi(startPage+"");
        System.out.println("----------------epi"+epi);
        progressSubscriber = new ProgressSubscriber<List<EpiBean>>(getActivity()) {
            @Override
            public void _Next(List<EpiBean> epiBean) {
                EpisodeFragment.this.epiBeanList=epiBean;
                System.out.println("段子成功---"+epiBean);
                adaptr = new EpiAdapter(getActivity(),epiBeanList);
                episode_recyclerView.setLayoutManager(linearLayoutManager);
                episode_recyclerView.setAdapter(adaptr);
            }

            @Override
            public void _OnError(String msg) {
                System.out.println("段子失败---"+msg);
            }
        };
        HttpUtils.getInstace().toSubscribe(epi, progressSubscriber,"epiCache", ActivityLifeCycleEvent.DESTROY,lifecycleSubject,false,false);
//        episode_recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new RecyclerAdapter(getActivity());
//        episode_recyclerView.setAdapter(adapter);
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
    public void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onDestroy();
        linearLayoutManager=null;
        episode_recyclerView=null;
        mRoot=null;
        progressSubscriber.dismissProgressDialog();
        if (!progressSubscriber.isUnsubscribed()){
            progressSubscriber.unsubscribe();
        }
        progressSubscriber=null;
        if (adaptr!=null){
            adaptr.destroy();
            adaptr=null;
        }
        epiBeanList.clear();
        startPage=1;
        subscribe.unsubscribe();
        subscribe=null;
        if (timer!=null){
            timer.cancel();
            timer=null;
            tt.cancel();
            tt=null;
        }
        headBase=null;
        footer=null;
    }
}