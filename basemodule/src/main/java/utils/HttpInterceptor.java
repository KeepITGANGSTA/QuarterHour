package utils;

import android.provider.SyncStateContract;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bwie.com.basemodule.SharedPreferencesUtil;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author:wangcaiwen
 * Time:2017/11/27.
 * Description:
 */

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = SharedPreferencesUtil.getPreferencesValue("token");
        System.out.println("Token==="+token);
        //获取到request
        Request request = chain.request();
        //获取方法
        String method = request.method();
        //公共参数
        Map<String,Object> hashmap = new HashMap<>();
        hashmap.put("source","android");
        hashmap.put("appVersion",101);
        hashmap.put("token",token+"" );


        //get请求的封装
        if(method.equals("GET")){
            //获取到请求地址api
            HttpUrl httpUrlurl = request.url();
            HashMap<String, Object> rootMap = new HashMap<>();
            //通过请求地址(最初始的请求地址)获取到参数列表
            Set<String> parameterNames = httpUrlurl.queryParameterNames();
            String url = httpUrlurl.toString();
            System.out.println("拦截器修改前url=="+url);
            String newUrl=url;
            for (Map.Entry<String, Object> entry : hashmap.entrySet()) {
              newUrl+= entry.getKey()+"="+entry.getValue()+"&&";
            }
            System.out.println("拦截后的url=="+newUrl);
            request = request.newBuilder().url(newUrl).build();  //重新构建请求

        }else if (method.equals("POST")){
            FormBody.Builder builder = new FormBody.Builder();
            if(request.body() instanceof FormBody){
            FormBody formBody = (FormBody) request.body();//初始body

                for (int i = 0; i < formBody.size() ; i++) {
                    builder.add(formBody.encodedName(i),formBody.encodedValue(i));
                }
                 builder.add("source", "android")
                        .add("appVersion", 1+"")
                        .add("token", token+"");
                request = request.newBuilder().post(builder.build()).build();
            }else if(request.body() instanceof MultipartBody){
                    MultipartBody body = (MultipartBody) request.body();
                    MultipartBody.Builder builder1 = new MultipartBody.Builder().setType(MultipartBody.FORM);
                builder1.addFormDataPart("source","android")
                        .addFormDataPart("appVersion",1+"")
                        .addFormDataPart("token",token+"");

                List<MultipartBody.Part>  parts = body.parts();
                for (MultipartBody.Part part : parts) {
                    builder1.addPart(part);
                }

                request = request.newBuilder().post(builder1.build()).build();
            }



        }

        return chain.proceed(request);
    }
}
