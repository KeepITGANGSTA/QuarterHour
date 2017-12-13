package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adapter.RecyclerAdapter;
import adapter.SpacesItemDecoration;
import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/11/25.
 * Description：
 */

public class HotVideoFragment extends Fragment {

    private View mRoot;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private StaggeredGridLayoutManager manager;

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
        manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = mRoot.findViewById(R.id.video_hot_recyclerView);
        recyclerView.setPadding(8,8,8,8);
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
        adapter = new RecyclerAdapter(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRoot=null;
        adapter.destroy();
        adapter=null;
        manager=null;
    }
}
