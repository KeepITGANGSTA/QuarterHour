package bwie.com.basemodule;

import android.content.Context;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.HttpInterceptor;
import utils.MyInterceptor;

/**
 * Created by 李英杰 on 2017/11/14.
 * Description：
 */

public class RetrofitHelper {

    private static String baseUrl;
    private static Context context;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private RetrofitHelper (){
        okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(new HttpInterceptor())
                .build();
        retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static class RetrofitHolder{
        static RetrofitHelper retrofitHelper=new RetrofitHelper();
    }
    public static RetrofitHelper getRetrofitHelper(String baseUrl, Context context){
        RetrofitHelper.baseUrl=baseUrl;
        RetrofitHelper.context=context;
        return RetrofitHolder.retrofitHelper;
    }

    public <T>T getApiService(Class<T> tClass){
        return retrofit.create(tClass);
    }

}
