package utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import bwie.com.basemodule.SharedPreferencesUtil;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by 李英杰 on 2017/11/17.
 * Description：
 */

public class MyInterceptor implements Interceptor {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private Context context;
    private int versionCode;
    private Gson gson;

    public MyInterceptor(Context context) {
        this.context=context;
        gson=new Gson();
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
//        Request.Builder builder = chain.request().newBuilder();
//        Request accept = builder
//                .addHeader("Content-Type", "application/json;charset=utf-8")
//                .addHeader("Accept", "*/*")
//                .build();
        Request request = chain.request();
        HashMap<String,Object> commonParms=new HashMap<>();
        String token = SharedPreferencesUtil.getPreferencesValue("token");
        System.out.println("token值---"+token);
        commonParms.put("token",token );
        commonParms.put("source","android");
        System.out.println("版本号---"+versionCode);
        commonParms.put("appVersion",versionCode);

        FormBody.Builder builder=new FormBody.Builder();
        HashMap<String, Object> rootMap = new HashMap<>();
        RequestBody body = request.body();
        if (body instanceof FormBody){
            for (int i = 0; i < ((FormBody)body).size() ; i++) {
                String key = ((FormBody) body).encodedName(i);
                String value = ((FormBody) body).encodedValue(i);
                System.out.println("key值---"+key+"---value值---"+value);
                rootMap.put(key,value);
                builder.add(key,value);
            }
            rootMap.put("token",token);
            rootMap.put("source","android");
            rootMap.put("appVersion",versionCode);

            builder.add("token",token);
            builder.add("source","android");
            builder.add("appVersion",versionCode+"");
            request=request.newBuilder().url(request.url().toString()).post(builder.build()).build();
            System.out.println("FormBody---");
        }else {
            Buffer buffer=new Buffer();
            body.writeTo(buffer);
            String oldJsonParms=buffer.readUtf8();
            rootMap=gson.fromJson(oldJsonParms,HashMap.class);
            System.out.println("rootMap----"+rootMap);
            rootMap.put("publicParams",commonParms);
            String newJsonParms=gson.toJson(rootMap);
            request=request.newBuilder().post(RequestBody.create(JSON,newJsonParms)).build();
            System.out.println("Buffer---");
        }
        return chain.proceed(request);
    }
}