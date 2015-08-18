package cn.bingoogolapple.refreshlayout.demo.diaoyu;

import cn.bingoogolapple.refreshlayout.demo.diaoyu.honeyant.HttpTaskResult;

/**
 * Created by wangzf on 2015/8/14.
 */
public interface RequestListener{

    // 请求执行之前
    void beforRequest();

    void onSuccess(HttpTaskResult result);

    void onFailure(Exception error);

    // 在 OnSuccess、OnFailure执行之后执行
    void finishRequest();
}
