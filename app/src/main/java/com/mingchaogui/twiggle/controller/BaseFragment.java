package com.mingchaogui.twiggle.controller;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.litesuits.common.assist.Toastor;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.param.HttpRichParamModel;
import com.mingchaogui.twiggle.http.HttpWrapper;

import java.util.ArrayList;
import java.util.List;


public class BaseFragment extends Fragment {

    List<AbstractRequest> mHttpRequestList;

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 取消所有的网络请求，实现与生命周期的联动
        cancelAllHttpRequest();
    }

    public BaseActivity getBaseActivity() {
        Activity activity = getActivity();

        return activity == null ? null : (BaseActivity)activity;
    }

    public Toastor getToastor() {
        BaseActivity activity = getBaseActivity();

        return activity == null? null : activity.getToastor();
    }

    public void showToast(int resId) {
        getToastor().getSingletonToast(resId).show();
    }

    public void showToast(String text) {
        getToastor().getSingletonToast(text).show();
    }

    public List<AbstractRequest> getHttpRequestList() {
        return mHttpRequestList;
    }

    /**
     * 异步执行请求，并将其加入到管理列表
     * @param param 请求参数
     * @return 请求对象
     */
    public AbstractRequest executeHttpRequestAsync(HttpRichParamModel param) {
        AbstractRequest request = HttpWrapper.getHttp().executeAsync(param);

        if (mHttpRequestList == null) {
            mHttpRequestList = new ArrayList<>();
        }
        mHttpRequestList.add(request);

        return request;
    }

    public void onHttpRequestEnd(AbstractRequest request) {
        if (mHttpRequestList != null) {
            // 将已完成的请求移出管理列表
            mHttpRequestList.remove(request);
        }
    }

    /**
     *  取消管理列表里的所有网络请求
     */
    public void cancelAllHttpRequest() {
        while (mHttpRequestList != null && !mHttpRequestList.isEmpty()) {
            int position = mHttpRequestList.size() - 1;
            mHttpRequestList.get(position).cancel();
            mHttpRequestList.remove(position);
        }
    }
}