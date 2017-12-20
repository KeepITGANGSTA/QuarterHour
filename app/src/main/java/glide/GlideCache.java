package glide;



import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.lang.annotation.Annotation;

import bwie.com.quarterhour.App;

/**
 * Created by 李英杰 on 2017/12/7.
 * Description：
 */

public class GlideCache implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskLruCacheFactory(() -> getMyCacheDir(),50*1024*1024));
    }

    private File getMyCacheDir() {
        File file=new File(App.AppContext.getCacheDir()+"/glide_cache/");
        if (!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }

    public static RequestOptions NoMemoryDiskCache(){
        RequestOptions options=new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        return options;
    }

    public static RequestOptions DiskCache(){
        RequestOptions options=new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        return options;
    }

    public static RequestOptions MemoryCache(){
        RequestOptions options=new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        return options;
    }

}
