package api;

import java.util.List;

import entity.AdBean;
import entity.BaseEntity;
import entity.EpiBean;
import entity.UserInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
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



}