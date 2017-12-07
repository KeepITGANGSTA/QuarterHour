package api;

import java.util.List;

import entity.AdBean;
import entity.BaseEntity;
import entity.EpiBean;
import entity.UserInfo;

import entity.VideoInfo;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by 李英杰 on 2017/11/30.
 * Description：
 */

public interface Api {

    @FormUrlEncoded
    @POST("user/login")
    Observable<BaseEntity<UserInfo>> login(@Field("mobile") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST("quarter/register")
    Observable<BaseEntity<UserInfo>> resUser(@Field("mobile") String mobile,@Field("password") String password);

    @FormUrlEncoded
    @POST("quarter/getAd")
    Observable<BaseEntity<AdBean>> getAd(@Field("source") String source);

    @FormUrlEncoded
    @POST("quarter/getJokes")
    Observable<BaseEntity<List<EpiBean>>> getEpi(@Field("page") String page);

    @Multipart
    @POST("quarter/publishJoke")
    Observable<BaseEntity<Object>> publishEpi(@Part List<MultipartBody.Part> list);


    @Multipart
    @POST("quarter/publishVideo ")
    Observable<BaseEntity<Object>> publishVideo(@Part List<MultipartBody.Part> list);

    @FormUrlEncoded
    @POST("quarter/getVideos")
    Observable<BaseEntity<List<VideoInfo>>> getVideos(@Field("uid") String uid,@Field("type") String type,@Field("page") String page);


}