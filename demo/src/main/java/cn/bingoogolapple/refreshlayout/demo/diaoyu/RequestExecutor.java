package cn.bingoogolapple.refreshlayout.demo.diaoyu;

import android.content.Context;

import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzf on 2015/8/14.
 */
public class RequestExecutor {
    private Context mCxt;
    private String module; // 请求模型
    private String userAgent;
    private Map<String, String> params; // 请求参数
    private RequestListener listener;
    private boolean enableXauth = true; // 是否开启Xauth认证

    private String method; // 请求方法
    private HashMap<String, String> allParams = new HashMap<>(); // 所有的请求参数

    public RequestExecutor(Context context) {

        allParams.putAll(RequestConfig.commonParams());
        this.mCxt = context;
    }

    public static RequestExecutor with(Context mCxt){
        return new RequestExecutor(mCxt);
    }

    public RequestExecutor module(String module){
        this.module = module;
        return this;
    }

    // 请求类型
    public RequestExecutor requestMthod(RequestMethod requestMthod){
        switch (requestMthod){
            case GET:
                method = AsyncHttpGet.METHOD;
                break;
            case POST:
                method = AsyncHttpPost.METHOD;
                break;
        }
        return this;
    }

    // 是否启用Xauth认证，默认true
    public RequestExecutor enableXauth(boolean bool){
        enableXauth = bool;
        return this;
    }

    public RequestExecutor userAgent(String userAgent){
        this.userAgent = userAgent;
        return this;
    }

    public RequestExecutor params(Map<String, String> params){
        if(params != null) {
            allParams.putAll(params);
        }
        return this;
    }

    public RequestExecutor listener(RequestListener listener){
        this.listener = listener;
        return this;
    }

    /** 返回JsonObject数据类型 */
    public HttpTaskReultRequest asHttpTaskResult(){
        return new HttpTaskReultRequest(build(), listener);
    }

    private Builders.Any.B build(){

        if(module == null){
            throw new IllegalArgumentException("module must be not null");
        }

        if(method == null){
            throw new IllegalArgumentException("requestMethod must be not null");
        }

        String baseUrl = RequestConfig.API_HOST + module;
        String requestUrl = null;

        // XAuth参数
        if(enableXauth){
            allParams.putAll(HAXAuthSign.generateURLParams(method, baseUrl, allParams,
                    RequestConfig.getConsumerKey(mCxt), RequestConfig.getConsumerSecret(mCxt),
                    RequestConfig.ACCESS_TOKEN, RequestConfig.TOKEN_SECRET));
        }

        switch (method){
            case AsyncHttpGet.METHOD:
                // Get 请求参数
                requestUrl = baseUrl + "?" + encodeParameters(allParams, RequestConfig.PARAMS_ENCODING);
                break;
            case AsyncHttpPost.METHOD:
                requestUrl = baseUrl;
                break;
        }

        Builders.Any.B b = Ion.with(mCxt).load(method, requestUrl).setTimeout(RequestConfig.TIME_OUT);

        // 设置User-Agent
        if(userAgent != null){
            b.userAgent(userAgent);
        }

        // 设置Post请求参数
        if(method.equals(AsyncHttpPost.METHOD)){
            for(Map.Entry<String, String> entity : allParams.entrySet())
            b.setBodyParameter(entity.getKey(), entity.getValue());
        }

        if(listener != null){
            listener.beforRequest();
        }
        return b;
    }

    /**
     * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
     */
    private String encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
}
