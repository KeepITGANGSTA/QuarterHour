package api;

import java.io.File;
import java.util.List;

import entity.AdBean;
import entity.BaseEntity;
import entity.EpiBean;
import entity.EpiDetails;
import entity.FollowBean;
import entity.UserInfo;

import entity.UserInfoBean;
import entity.VideoDetails;
import entity.VideoInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
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


    @GET("quarter/getJokes")
    Observable<BaseEntity<List<EpiBean>>> getEpi(@Query("page") String page);

    @Multipart
    @POST("quarter/publishJoke")
    Observable<BaseEntity<Object>> publishEpi(@Part List<MultipartBody.Part> list);


    @Multipart
    @POST("quarter/publishVideo ")
    Observable<BaseEntity<Object>> publishVideo(@Part List<MultipartBody.Part> list);


    @GET("quarter/getVideos")
    Observable<BaseEntity<List<VideoInfo>>> getVideos(@Query("uid") String uid,@Query("type") String type,@Query("page") String page);

    @FormUrlEncoded
    @POST("quarter/getVideoDetail")
    Observable<BaseEntity<VideoDetails>> getVideoDetails(@Field("wid") String wid);

    @FormUrlEncoded
    @POST("quarter/addFavorite")
    Observable<BaseEntity<Object>> collectVideo(@Field("uid") String uid , @Field("wid") String wid);

    @FormUrlEncoded
    @POST("quarter/praise")
    Observable<BaseEntity<Object>> pariseVideo(@Field("uid") String uid , @Field("wid") String wid);

    @FormUrlEncoded
    @POST("quarter/cancelFavorite")
    Observable<BaseEntity<Object>> cancelVideoCollect(@Field("uid") String uid , @Field("wid") String wid);

    @FormUrlEncoded
    @POST("quarter/comment")
    Observable<BaseEntity<Object>> commentsVideo(@Field("uid") String uid , @Field("wid") String wid,@Field("content") String content);


    @FormUrlEncoded
    @POST("quarter/getWorkInfo")
    Observable<BaseEntity<UserInfoBean>> getUserInfo(@Field("uid") String uid );

    @FormUrlEncoded
    @POST("quarter/follow")
    Observable<BaseEntity<Object>> follow(@Field("uid") String uid , @Field("followId") String followId);


    @Multipart
    @POST("file/upload")
    Observable<BaseEntity<Object>> updateIcon(@Part List<MultipartBody.Part> list);

    @FormUrlEncoded
    @POST("user/getUserInfo")
    Observable<BaseEntity<UserInfo>> UserInfo(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("quarter/getJokeDetail")
    Observable<BaseEntity<EpiDetails>> getEpiDetails(@Field("jid") String jid);


    @FormUrlEncoded
    @POST("quarter/getFollowUsers")
    Observable<BaseEntity<List<FollowBean>>> getFollowUser(@Field("uid") String uid);

}