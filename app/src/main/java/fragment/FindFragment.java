package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zhy.autolayout.AutoLinearLayout;

import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/12/21.
 * Description：
 */

public class FindFragment extends Fragment {

    private View mRoot;
    private AutoLinearLayout lin_msg,lin_link,lin_activeMsg;
    private BottomBar bottomBar;
    private MessageFragment msgFragment;
    private LinkFragment linkFragment;
    private ActiveFragment activeFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.find_layout,container,false);
        }
        ViewGroup parent= (ViewGroup) mRoot.getParent();
        if (parent!=null){
            parent.removeView(mRoot);
        }
        initView();
        return mRoot;
    }

    private void initView() {
        bottomBar=mRoot.findViewById(R.id.find_bottombar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState==null){
            msgFragment= (MessageFragment) getChildFragmentManager().findFragmentByTag("msg_fragment");
            linkFragment=(LinkFragment) getChildFragmentManager().findFragmentByTag("link_fragment");
            activeFragment=(ActiveFragment) getChildFragmentManager().findFragmentByTag("active_fragment");
        }
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId){
                case R.id.tab_msg:
                    if (msgFragment==null){
                        msgFragment=new MessageFragment();
                        add(R.id.msg_framelayout,msgFragment,"msg_fragment");
                    }else {
                        if (linkFragment!=null){
                            hide(linkFragment);
                        }
                        if (activeFragment!=null){
                            hide(activeFragment);
                        }
                        show(msgFragment);
                    }
                    break;
                case R.id.tab_link:
                    if (linkFragment==null){
                        hide(msgFragment);
                        if (activeFragment!=null){
                            hide(activeFragment);
                        }
                        linkFragment=new LinkFragment();
                        add(R.id.msg_framelayout,linkFragment,"link_fragment");
                    }else {
                        if (msgFragment!=null){
                            hide(msgFragment);
                        }
                        if (activeFragment!=null){
                            hide(activeFragment);
                        }
                        show(linkFragment);
                    }
                    break;
                case R.id.tab_active:
                    if (activeFragment==null){
                        hide(msgFragment);
                        if (linkFragment!=null){
                            hide(linkFragment);
                        }
                        activeFragment=new ActiveFragment();
                        add(R.id.msg_framelayout,activeFragment,"active_fragment");
                    }else {
                        hide(msgFragment);
                        if (linkFragment!=null){
                            hide(linkFragment);
                        }
                        show(activeFragment);
                    }
                    break;
            }
        });
    }


    private void add(int frameLayout,Fragment fragment,String tag){
        getChildFragmentManager().beginTransaction().add(frameLayout,fragment,tag).commit();
    }

    private void hide(Fragment fragment){
        getChildFragmentManager().beginTransaction().hide(fragment).commit();
    }

    private void show(Fragment fragment){
        getChildFragmentManager().beginTransaction().show(fragment).commit();
    }
}
