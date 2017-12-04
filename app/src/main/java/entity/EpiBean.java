package entity;

import java.util.List;

/**
 * Created by 李英杰 on 2017/12/3.
 * Description：
 */

public class EpiBean extends BaseEntity{
    public Object commentNum;
    public String content;
    public String createTime;
    public Object imgUrls;
    public int jid;
    public Object praiseNum;
    public Object shareNum;
    public int uid;
    public UserBean user;

    public static class UserBean {

        public Object age;
        public String fans;
        public String follow;
        public String icon;
        public String nickname;
        public String praiseNum;
    }
}
