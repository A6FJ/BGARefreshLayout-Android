package cn.bingoogolapple.refreshlayout.demo.ui.fragment;

import android.content.Context;

import com.koushikdutta.ion.future.ResponseFuture;

import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.demo.diaoyu.RVHttpTaskObserver;
import cn.bingoogolapple.refreshlayout.demo.diaoyu.RequestExecutor;
import cn.bingoogolapple.refreshlayout.demo.diaoyu.RequestListener;
import cn.bingoogolapple.refreshlayout.demo.diaoyu.RequestMethod;
import cn.bingoogolapple.refreshlayout.demo.diaoyu.honeyant.HttpTaskResult;

/**
 * @PACKAGE cn.bingoogolapple.refreshlayout.demo.ui.fragment
 * @DESCRIPTION: 模块处理器
 * @AUTHOR dongen_wang
 * @DATE 15/8/17 17:56
 * @VERSION V1.0
 */
public class RvModel implements RequestListener {

    /**
     * http请求状态
     */
    public static final int HttpTaskStatusAdded = (0x00000001 << 0);
    public static final int HttpTaskStatusStarted = (0x00000001 << 1);
    public static final int HttpTaskStatusSucceeded = (0x00000001 << 2);
    public static final int HttpTaskStatusFailed = (0x00000001 << 3);
    public static final int HttpTaskStatusCanceled = (0x00000001 << 4);
    public static final int HttpTaskStatusFinish = (0x00000001 << 5);

    // 模块标志
    public String rvFlag;

    //模块状态
    public int status;

    private Context ctx;

    // 默认请求方法
    private RequestMethod requestMethod = RequestMethod.GET;

    // 默认参数
    public Map<String,String> params = new HashMap<String,String>();

    // 模块名字
    private String module;

    // 请求返回Future
    public ResponseFuture responseFuture;

    // 请求返回的结果
    public HttpTaskResult httpTaskResult;

    private RvModel(Context context,String md){
        ctx = context;
        module = md;
        rvFlag = md;
    }

    public static RvModel build(Context context,String module){
        return new RvModel(context,module);
    }

    public RvModel params(Map<String,String> p){
        params = p;
        return this;
    }

    public RvModel method(RequestMethod method){
        requestMethod = method;
        return this;
    }


    //module "skill/getlist"
    public RvModel refreshData(){

        // started
        RVHttpTaskObserver.getInstance().notifyRequestStarted(this);

         responseFuture = RequestExecutor.with(ctx).module(module)
                .params(params).requestMthod(requestMethod)
                .listener(this).asHttpTaskResult().execute();

        return this;
    }

    @Override
    public void beforRequest() {

    }

    @Override
    public void onSuccess(HttpTaskResult result) {

        httpTaskResult = result;
        RVHttpTaskObserver.getInstance().notifyRequestSucceeded(this);

    }

    @Override
    public void onFailure(Exception error) {

        RVHttpTaskObserver.getInstance().notifyRequestFailed(this);

    }

    @Override
    public void finishRequest() {

        RVHttpTaskObserver.getInstance().notifyRequestFinish(this);

    }
}
