package fragment;

import android.app.ActivityOptions;
import android.content.Intent;
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

import java.util.List;

import adapter.RecyclerAdapter;

import adapter.SpacesItemDecoration;
import api.Api;
import api.Common;
import bwie.com.basemodule.RetrofitHelper;
import bwie.com.basemodule.SharedPreferencesUtil;
import bwie.com.quarterhour.App;
import bwie.com.quarterhour.R;
import bwie.com.quarterhour.VideoDetailsActivity;
import entity.VideoInfo;
import rx.Observable;
import utils.ActivityLifeCycleEvent;
import utils.HttpUtils;
import utils.ProgressSubscriber;

/**
 * Created by 李英杰 on 2017/11/25.
 * Description：
 */

public class NearbyVideoFragment extends Fragment {

    private View mRoot;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private StaggeredGridLayoutManager manager;
    private Api apiService;
    private ProgressSubscriber nearbtVideoSubscriber;
    private String Uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.video_nearby_fragment,container,false);
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
        recyclerView = mRoot.findViewById(R.id.video_nearby_recyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setPadding(8,8,8,8);
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
        recyclerView.addItemDecoration(decoration);;

    }

//    private void getVideo() {
//        String uid = SharedPreferencesUtil.getPreferencesValue("uid");
//        if (!TextUtils.isEmpty(uid)){
//            Uid=uid;
//        }
//        nearbtVideoSubscriber = new ProgressSubscriber<List<VideoInfo>>(getContext()) {
//            @Override
//            public void _Next(List<VideoInfo> o) {
//                adapter = new RecyclerAdapter(getActivity().getApplicationContext(),o);
//                recyclerView.setAdapter(adapter);
//                adapter.setOnVideoItemClick(wid -> {
//                    Intent intent=new Intent(getContext(), VideoDetailsActivity.class);
//                    intent.putExtra("videoWid",wid);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                });
//            }
//            @Override
//            public void _OnError(String msg) {
//                Toast.makeText(getActivity(), "请求超时!"+msg, Toast.LENGTH_SHORT).show();
//            }
//        };
//        Observable video = apiService.getVideos(Uid,"1",startPage+"");
//        HttpUtils.getInstace().toSubscribe(video, videoSubscriber,"videoCache_recy", ActivityLifeCycleEvent.STOP,lifecycleSubject,false,false);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRoot=null;
        recyclerView=null;
        if (adapter!=null){
            adapter.destroy();
            adapter=null;
        }
        manager=null;

    }
}
