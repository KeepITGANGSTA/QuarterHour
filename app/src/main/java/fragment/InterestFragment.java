package fragment;

import android.app.ActivityOptions;
import android.content.Intent;
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
import android.widget.Button;

import com.zhy.autolayout.AutoLinearLayout;

import adapter.RecyclerAdapter;
import bwie.com.basemodule.SharedPreferencesUtil;
import bwie.com.quarterhour.LoginActivity;
import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/11/25.
 * Description：
 */

public class InterestFragment extends Fragment {
    private View mRoot;
    private RecyclerView interest_recyclerView;
    private RecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Button btn_login;
    private String uid;
    private AutoLinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.interest_view,container,false);
        }
        ViewGroup parent = (ViewGroup) mRoot.getParent();
        if (parent!=null){
            parent.removeView(mRoot);
        }
        linearLayoutManager = new LinearLayoutManager(getActivity());
        interest_recyclerView=mRoot.findViewById(R.id.interest_recyclerView);
        btn_login=mRoot.findViewById(R.id.interest_btnLogin);
        linearLayout=mRoot.findViewById(R.id.line_inter_login);
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        uid = SharedPreferencesUtil.getPreferencesValue("uid");
        if (TextUtils.isEmpty(uid)){
            interest_recyclerView.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TextUtils.isEmpty(uid)){
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }
            });
        }else {
            adapter = new RecyclerAdapter(getActivity().getApplicationContext());
            interest_recyclerView.setLayoutManager(linearLayoutManager);
            interest_recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRoot=null;
        if (!TextUtils.isEmpty(uid)){
            if (adapter!=null){
                adapter.destroy();
                adapter=null;
            }
            interest_recyclerView=null;
        }
        linearLayoutManager=null;
    }
}