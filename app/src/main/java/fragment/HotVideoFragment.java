package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

import adapter.HotAdapter;
import adapter.RecyclerAdapter;
import adapter.SpacesItemDecoration;
import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.basemodule.SharedPreferencesUtil;
import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;
import entity.VideoInfo;
import rx.Observable;
import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;
import view.PublishView;

/**
 * Created by 李英杰 on 2017/11/25.
 * Description：
 */

public class HotVideoFragment extends Fragment {

    private final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject=PublishSubject.create();
    private View mRoot;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private StaggeredGridLayoutManager manager;
    private ProgressSubscriber videoSubscriber;
    private Api apiService;
    private String Uid="";
    private int startPage=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.video_hot_fragment,container,false);
        }
        ViewGroup parent = (ViewGroup) mRoot.getParent();
        if (parent!=null){
            parent.removeView(mRoot);
        }
        apiService = RetrofitHelper.getRetrofitHelper(Common.BASE_URL, App.AppContext).getApiService(Api.class);
        manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = mRoot.findViewById(R.id.video_hot_recyclerView);
        recyclerView.setPadding(8,8,8,8);
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(decoration);
        getVideo();
    }

    private void getVideo() {
        String uid = SharedPreferencesUtil.getPreferencesValue("uid");
        if (!TextUtils.isEmpty(uid)){
            Uid=uid;
        }
        videoSubscriber = new ProgressSubscriber<List<VideoInfo>>(getContext()) {
            @Override
            public void _Next(List<VideoInfo> o) {
                adapter = new RecyclerAdapter(getActivity().getApplicationContext(),o);
                recyclerView.setAdapter(adapter);
                System.out.println("请求成功----"+o.size());
            }
            @Override
            public void _OnError(String msg) {
                Toast.makeText(getActivity(), "请求超时!", Toast.LENGTH_SHORT).show();
            }
        };
        Observable video = apiService.getVideos(Uid,"1",startPage+"");
        HttpUtils.getInstace().toSubscribe(video, videoSubscriber,"videoCache_recy", ActivityLifeCycleEvent.STOP,lifecycleSubject,false,false);
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }
    @Override
    public void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onDestroy();
        mRoot=null;
        if (adapter!=null){
            adapter.destroy();
            adapter=null;
        }
        manager=null;
        if (videoSubscriber!=null){
            videoSubscriber.unsubscribe();
            videoSubscriber=null;
        }

    }
}
