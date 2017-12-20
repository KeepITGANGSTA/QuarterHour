package entity;

import java.util.List;

/**
 * Created by 李英杰 on 2017/12/7.
 * Description：
 */

public class VideoInfo extends BaseEntity {

    /**
     * commentNum : 1
     * comments : [{"cid":50,"content":"亚麻得","createTime":"2017-12-15T14:52:37","jid":null,"mvp":null,"nickname":"小狼","praiseNum":0,"uid":150,"wid":218}]
     * cover : https://www.zhaoapi.cn/images/quarter/1513248624521cover.jpg
     * createTime : 2017-12-14T18:50:24
     * favoriteNum : 1
     * latitude : 101
     * localUri : null
     * longitude : 102
     * playNum : null
     * praiseNum : 3
     * uid : 154
     * user : {"age":null,"fans":"null","follow":false,"icon":"https://www.zhaoapi.cn/images/154.jpg","nickname":"笑出腹肌的男人","praiseNum":"null"}
     * videoUrl : https://www.zhaoapi.cn/images/quarter/1513248624521PictureSelector_20171214_184937.mp4
     * wid : 218
     * workDesc : 111
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
    public List<CommentsBean> comments;

    public static class UserBean {
        /**
         * age : null
         * fans : null
         * follow : false
         * icon : https://www.zhaoapi.cn/images/154.jpg
         * nickname : 笑出腹肌的男人
         * praiseNum : null
         */

        public Object age;
        public String fans;
        public boolean follow;
        public String icon;
        public String nickname;
        public String praiseNum;
    }

    public static class CommentsBean {


        public int cid;
        public String content;
        public String createTime;
        public Object jid;
        public Object mvp;
        public String nickname;
        public int praiseNum;
        public int uid;
        public int wid;
    }
}
