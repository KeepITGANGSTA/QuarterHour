package entity;

import java.util.List;

/**
 * Created by 李英杰 on 2017/12/17.
 * Description：
 */

public class VideoDetails extends BaseEntity{

    public int commentNum;
    public String cover;
    public String createTime;
    public int favoriteNum;
    public String latitude;
    public Object localUri;
    public String longitude;
    public int playNum;
    public int praiseNum;
    public int uid;
    public UserBean user;
    public String videoUrl;
    public int wid;
    public String workDesc;
    public List<CommentsBean> comments;

    public static class UserBean {

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
