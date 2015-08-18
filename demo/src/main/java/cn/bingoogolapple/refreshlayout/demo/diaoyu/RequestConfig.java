package cn.bingoogolapple.refreshlayout.demo.diaoyu;

import android.content.Context;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wangzf on 2015/8/14.
 */
public class RequestConfig {

    // 请求Url
    public static final String API_HOST = "http://api.diaoyu123.cc/app/";

    // 超时时间
    public static final int TIME_OUT = 30 * 1000;

    // Get请求参数编码
    public static final String PARAMS_ENCODING = "utf-8";

    // 请求公共参数
    public static final Map<String, String> commonParams(){
        Map<String, String> params = new HashMap<>();
        params.put("platform", "android");
        params.put("client_version", "1.0");
        params.put("api_version", "1.0");
        params.put("app_channel", "lchr");
        params.put("device_id", "46b99fa7e03a1672");
        return params;
    }

    // XAuth参数
    private static Properties properties;
    private static Properties getLocalProperties(Context mCxt){
        if(properties == null){
            try {
                properties = new Properties();
                InputStream in = mCxt.getAssets().open("config.properties");;
                properties.load(in);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return properties;
    }

    private static String consumer_key = null;
    public static String getConsumerKey(Context mCxt){
        if(consumer_key == null){
            consumer_key = getLocalProperties(mCxt).getProperty("CONSUMER_KEY");
        }
        return consumer_key;
    }

    private static String consumer_secret = null;
    public static String getConsumerSecret(Context mCxt){
        if(consumer_secret == null){
            consumer_secret = getLocalProperties(mCxt).getProperty("CONSUMER_SECRET");
        }
        return consumer_secret;
    }

    public static final String ACCESS_TOKEN = "";
    public static final String TOKEN_SECRET = "";
}
