package com.mingchaogui.twiggle.http;


import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.Response;
import com.mingchaogui.twiggle.controller.BaseFragment;

public class HttpBaseOnFragmentListener<T> extends HttpListener<T> {

    BaseFragment mFragment;

    public HttpBaseOnFragmentListener(BaseFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public boolean disableListener() {
        // 以下情况时取消请求
        return mFragment == null || mFragment.isRemoving() || mFragment.isDetached();
    }

    @Override
    public void onFailure(HttpException e, Response<T> response) {
        super.onFailure(e, response);

        // 处理异常
        HttpExceptHandler handler = new HttpExceptHandler(mFragment);
        handler.handleException(e);
        handler.release();
    }

    @Override
    public void onEnd(Response<T> response) {
        super.onEnd(response);

        // 结束请求时回调
        mFragment.onHttpRequestEnd(response.<AbstractRequest<T>>getRequest());
    }
}
