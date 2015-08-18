package cn.bingoogolapple.refreshlayout.demo.diaoyu.honeyant;

import com.google.gson.JsonObject;

/**
 * Created by dongjianbo on 15-4-8.
 */
public class ApiCommonData {
    public boolean isnew;
    public int recommand;
    public int sms;
    public int posts;
    public int sys;

    private static ApiCommonData instance = null;

    protected ApiCommonData() {

    }

    public static synchronized ApiCommonData getInstance() {
        if (instance == null) {
            instance = new ApiCommonData();
        }

        return instance;
    }

    public void parseJson(JsonObject jsonObject) {
        if (jsonObject != null) {
            this.recommand = jsonObject.get("recommand").getAsCharacter();
            this.sms = jsonObject.get("sms").getAsInt();
            this.posts = jsonObject.get("posts").getAsInt();
            this.sys = jsonObject.get("sys").getAsInt();

            this.isnew = (this.recommand + this.sms + this.posts + this.sys) > 0;

//            HANotificationCenter.getInstance().postNotification(NotificationConst.NOTI_MESSAGE_COUNT, null);
        }
    }
}
