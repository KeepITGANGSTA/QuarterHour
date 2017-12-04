package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李英杰 on 2017/11/24.
 * Description：
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private  List<Fragment> mFragments = new ArrayList<>();
    private  List<String> fragmentTitles = new ArrayList<>();
    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fragment, String title){
        mFragments.add(fragment);
        fragmentTitles.add(title);
        System.out.println("标题---"+title);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        //这里返回的标题就是TabLayout的标题
        return fragmentTitles.get(position);
    }

    public void destroy(){
        mFragments.clear();
        mFragments=null;
        fragmentTitles.clear();
        fragmentTitles=null;
    }

}
