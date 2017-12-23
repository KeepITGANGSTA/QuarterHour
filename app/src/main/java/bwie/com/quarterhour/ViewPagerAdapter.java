package bwie.com.quarterhour;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import glide.GlideCache;

/**
 * Created by 李英杰 on 2017/12/20.
 * Description：
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> list;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View inflate = inflater.inflate(R.layout.photo_layout, null);
        PhotoView photoView=inflate.findViewById(R.id.photo_view);
        photoView.setAllowParentInterceptOnEdge(true);
        photoView.setZoomable(true);
        Glide.with(context).applyDefaultRequestOptions(GlideCache.DiskCache()).load(list.get(position)).into(photoView);
        System.out.println("当前展示图片的地址----"+list.get(position));
        container.addView(inflate);
        return inflate;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public void destroy(){
        inflater=null;
        context=null;
        list.clear();
    }

}
