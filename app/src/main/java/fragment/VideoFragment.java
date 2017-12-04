package fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import adapter.MyViewPagerAdapter;
import bwie.com.quarterhour.R;


/**
 * Created by 李英杰 on 2017/11/25.
 * Description：
 */

public class VideoFragment extends Fragment {

    private View mRoot;
    private ViewPager video_viewPager;
    private TabLayout video_tablayout;
    private MyViewPagerAdapter adapter;
    private HotVideoFragment hotVideoFragment;
    private NearbyVideoFragment nearbyVideoFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.video_view,container,false);
        }
        ViewGroup parent= (ViewGroup) mRoot.getParent();
        if (parent!=null){
            parent.removeView(mRoot);
        }
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hotVideoFragment=new HotVideoFragment();
        nearbyVideoFragment=new NearbyVideoFragment();
        video_tablayout=mRoot.findViewById(R.id.video_tabLayout);
        video_viewPager=mRoot.findViewById(R.id.video_viewPager);
        video_tablayout.addTab(video_tablayout.newTab().setText("one_"));
        video_tablayout.addTab(video_tablayout.newTab().setText("two_"));
        adapter = new MyViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(hotVideoFragment,"热门");
        adapter.addFragment(nearbyVideoFragment,"附近");
        video_viewPager.setAdapter(adapter);
        video_tablayout.setupWithViewPager(video_viewPager);
        initTablayout(video_tablayout,40,40);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.destroy();
        adapter=null;
        mRoot=null;
        video_viewPager.clearOnPageChangeListeners();
        video_viewPager=null;
        video_tablayout.clearOnTabSelectedListeners();
        video_tablayout=null;
        hotVideoFragment=null;
        nearbyVideoFragment=null;
    }

    private void initTablayout(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
