package entity;

import java.util.List;

/**
 * Created by 李英杰 on 2017/12/19.
 * Description：
 */

public class UserInfoBean extends BaseEntity{

    /**
     * user : {"age":null,"fans":"null","follow":false,"icon":"https://www.zhaoapi.cn/images/1513246264287cropped_1513246262263.jpg","nickname":"小狼","praiseNum":"null"}
     * worksEntities : [{"commentNum":0,"cover":"https://www.zhaoapi.cn/images/quarter/151256642330325047591810E300F44300132EDF80669B4879B9B71.jpg","createTime":"2017-12-17T19:20:44","favoriteNum":0,"latitude":"0.0","localUri":null,"longitude":"0.0","playNum":2,"praiseNum":0,"uid":150,"videoUrl":"https://www.zhaoapi.cn/images/quarter/15125664233031511709213842.mp4","wid":119,"workDesc":""},{"commentNum":0,"cover":"https://www.zhaoapi.cn/images/quarter/15126090957711511879908298.jpg","createTime":"2017-12-17T19:20:44","favoriteNum":0,"latitude":"0.0","localUri":null,"longitude":"0.0","playNum":0,"praiseNum":0,"uid":150,"videoUrl":"https://www.zhaoapi.cn/images/quarter/15126090957711511879626537.mp4","wid":156,"workDesc":"骚旭出征，寸草不生"},{"commentNum":0,"cover":"https://www.zhaoapi.cn/images/quarter/151261635916225047591810E300F44300132EDF80669B4879B9B71.jpg","createTime":"2017-12-17T19:20:44","favoriteNum":0,"latitude":"0.0","localUri":null,"longitude":"0.0","playNum":0,"praiseNum":0,"uid":150,"videoUrl":"https://www.zhaoapi.cn/images/quarter/15126163591621511711131303.mp4","wid":164,"workDesc":"彪彪"},{"commentNum":0,"cover":"https://www.zhaoapi.cn/images/quarter/151264646563120171206_200443.jpg","createTime":"2017-12-17T19:20:44","favoriteNum":0,"latitude":"0.0","localUri":null,"longitude":"0.0","playNum":0,"praiseNum":0,"uid":150,"videoUrl":"https://www.zhaoapi.cn/images/quarter/15126464656311511675418552.mp4","wid":185,"workDesc":"彪彪"},{"commentNum":0,"cover":"https://www.zhaoapi.cn/images/quarter/15129591508500.jpg","createTime":"2017-12-17T19:20:44","favoriteNum":0,"latitude":"0.0","localUri":null,"longitude":"0.0","playNum":0,"praiseNum":0,"uid":150,"videoUrl":"https://www.zhaoapi.cn/images/quarter/15129591508501512892442603.mp4","wid":206,"workDesc":"流浪"},{"commentNum":0,"cover":"https://www.zhaoapi.cn/images/quarter/15133878221070ffff64b399bee9d0413aee42c886f7e.jpg","createTime":"2017-12-17T19:20:44","favoriteNum":0,"latitude":"0.0","localUri":null,"longitude":"0.0","playNum":11,"praiseNum":0,"uid":150,"videoUrl":"https://www.zhaoapi.cn/images/quarter/15133878221071512894754287.mp4","wid":222,"workDesc":"卡丁车"},{"commentNum":null,"cover":"https://www.zhaoapi.cn/images/quarter/151358372249720170919_202309_1506213685322.jpg","createTime":"2017-12-18T15:55:22","favoriteNum":null,"latitude":"0.0","localUri":null,"longitude":"0.0","playNum":74,"praiseNum":null,"uid":150,"videoUrl":"https://www.zhaoapi.cn/images/quarter/151358372249720170919_194626.mp4","wid":225,"workDesc":"瞎搞"}]
     */

    public UserBean user;
    public List<WorksEntitiesBean> worksEntities;

    public static class UserBean {
        /**
         * age : null
         * fans : null
         * follow : false
         * icon : https://www.zhaoapi.cn/images/1513246264287cropped_1513246262263.jpg
         * nickname : 小狼
         * praiseNum : null
         */

        public Object age;
        public String fans;
        public boolean follow;
        public String icon;
        public String nickname;
        public String praiseNum;
    }

    public static class WorksEntitiesBean {
        /**
         * commentNum : 0
         * cover : https://www.zhaoapi.cn/images/quarter/151256642330325047591810E300F44300132EDF80669B4879B9B71.jpg
         * createTime : 2017-12-17T19:20:44
         * favoriteNum : 0
         * latitude : 0.0
         * localUri : null
         * longitude : 0.0
         * playNum : 2
         * praiseNum : 0
         * uid : 150
         * videoUrl : https://www.zhaoapi.cn/images/quarter/15125664233031511709213842.mp4
         * wid : 119
         * workDesc :
         */

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
        public String videoUrl;
        public int wid;
        public String workDesc;
    }
}
