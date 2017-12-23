package entity;

/**
 * Created by 李英杰 on 2017/12/20.
 * Description：
 */

public class EpiDetails extends BaseEntity {

    /**
     * commentNum : null
     * content :
     * createTime : 2017-12-20T08:37:37
     * imgUrls : https://www.zhaoapi.cn/images/quarter/151373025709484c74644af5bf70632445842626e4dad.jpg|https://www.zhaoapi.cn/images/quarter/151373025709485f049cb15ef8b61e7bb55f0a298f101.jpg
     * jid : 977
     * praiseNum : null
     * shareNum : null
     * uid : 154
     * user : {"age":null,"fans":"null","follow":false,"icon":"https://www.zhaoapi.cn/images/15136653175981513592154181.jpg","nickname":"笑出腹肌的男人","praiseNum":"null"}
     */

    public Object commentNum;
    public String content;
    public String createTime;
    public String imgUrls;
    public int jid;
    public Object praiseNum;
    public Object shareNum;
    public int uid;
    public UserBean user;

    public static class UserBean {
        /**
         * age : null
         * fans : null
         * follow : false
         * icon : https://www.zhaoapi.cn/images/15136653175981513592154181.jpg
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
}
