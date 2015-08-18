package cn.bingoogolapple.refreshlayout.demo.diaoyu;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.future.ResponseFuture;

import cn.bingoogolapple.refreshlayout.demo.diaoyu.honeyant.HttpTaskResult;

/**
 * 返回Gson-JsonObject类型请求
 * Created by wangzf on 2015/8/15.
 */
public class HttpTaskReultRequest {

    Builders.Any.B b;
    RequestListener listener;

    public HttpTaskReultRequest(Builders.Any.B b, RequestListener listener) {
        this.b = b;
        this.listener = listener;
    }

    public ResponseFuture<HttpTaskResult> execute(){
        ResponseFuture<HttpTaskResult> future = b.as(new HttpTaskResultParser());
        if(listener != null){
            future.setCallback(new FutureCallback<HttpTaskResult>() {
                @Override
                public void onCompleted(Exception e, HttpTaskResult result) {
                    // 请求失败
                    if(e != null){
                        listener.onFailure(e);
                        listener.finishRequest();
                        return;
                    }

                    // 请求成功
                    listener.onSuccess(result);
                    listener.finishRequest();
                }
            });
        }
        return future;
    }
}
