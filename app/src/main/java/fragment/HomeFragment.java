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

import adapter.HotAdapter;
import adapter.MyViewPagerAdapter;

import bwie.com.quarterhour.R;

/**
 * Created by 李英杰 on 2017/11/25.
 * Description：
 */

public class HomeFragment extends Fragment {

    private View mRoot;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;
    private HotFragment fragment;
    private InterestFragment interestFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot==null){
            mRoot=inflater.inflate(R.layout.home_fragment,container,false);
        }
        ViewGroup parent = (ViewGroup) mRoot.getParent();
        if (parent!=null){
            parent.removeView(mRoot);
        }
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabLayout=mRoot.findViewById(R.id.tab_layout);
        viewPager=mRoot.findViewById(R.id.view_page);
        fragment=new HotFragment();
        interestFragment=new InterestFragment();
        tabLayout.addTab(tabLayout.newTab().setText("one_"));
        tabLayout.addTab(tabLayout.newTab().setText("two_"));
        adapter = new MyViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(fragment,"热门");
        adapter.addFragment(interestFragment,"关注");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        initTablayout(tabLayout,40,40);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.clearOnPageChangeListeners();
        tabLayout.clearOnTabSelectedListeners();
        adapter.destroy();
        adapter=null;
        tabLayout=null;
        viewPager=null;
        mRoot=null;
        fragment=null;
        interestFragment=null;
    }
}
