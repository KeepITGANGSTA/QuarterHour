package entity;

import java.util.List;

/**
 * Created by 李英杰 on 2017/12/7.
 * Description：
 */

public class VideoInfo extends BaseEntity {

    /**
     * commentNum : 0
     * comments : []
     * cover : https://www.zhaoapi.cn/images/quarter/1512629690803firstFrame.jpg
     * createTime : 2017-12-07T14:54:50
     * favoriteNum : 0
     * latitude : 40.040464
     * localUri : null
     * longitude : 116.300061
     * playNum : null
     * praiseNum : 0
     * uid : 88
     * user : {"age":null,"fans":"null","follow":"null","icon":"https://www.zhaoapi.cn/images/88.jpg","nickname":"张贺岗","praiseNum":"null"}
     * videoUrl : https://www.zhaoapi.cn/images/quarter/1512629690803video_1512629663564.mp4
     * wid : 183
     * workDesc : 咳咳
     */

    public int commentNum;
    public String cover;
    public String createTime;
    public int favoriteNum;
    public String latitude;
    public Object localUri;
    public String longitude;
    public Object playNum;
    public int praiseNum;
    public int uid;
    public UserBean user;
    public String videoUrl;
    public int wid;
    public String workDesc;
    public List<?> comments;

    public static class UserBean {
        /**
         * age : null
         * fans : null
         * follow : null
         * icon : https://www.zhaoapi.cn/images/88.jpg
         * nickname : 张贺岗
         * praiseNum : null
         */

        public Object age;
        public String fans;
        public String follow;
        public String icon;
        public String nickname;
        public String praiseNum;
    }
}
