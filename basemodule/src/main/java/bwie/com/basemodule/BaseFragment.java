package bwie.com.basemodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.subjects.PublishSubject;
import utils.ActivityLifeCycleEvent;

/**
 * Created by 李英杰 on 2017/12/2.
 * Description：
 */

public abstract class BaseFragment extends Fragment {

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject=PublishSubject.create();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return layoutView();
    }

    public abstract View layoutView();
    public abstract void onActivityCreate();
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onActivityCreate();
    }
}
