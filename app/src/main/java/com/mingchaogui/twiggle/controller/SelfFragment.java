package com.mingchaogui.twiggle.controller;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.Response;
import com.mingchaogui.twiggle.R;
import com.mingchaogui.twiggle.auth.WeiboAuth;
import com.mingchaogui.twiggle.auth.WeiboAuthKeeper;
import com.mingchaogui.twiggle.databinding.ContentInfoCardBinding;
import com.mingchaogui.twiggle.http.HttpBaseOnFragmentListener;
import com.mingchaogui.twiggle.http_param.UsersShowParam;
import com.mingchaogui.twiggle.model.UsersShow;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelfFragment extends BaseFragment {

    @Bind(R.id.recyclerview)
    XRecyclerView mRecyclerView;

    WeiboInfoAdapter mAdapter;
    ContentInfoCardBinding mInfoCardBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, getActivity());

        mAdapter = new WeiboInfoAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new LoadingListener());

        mInfoCardBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.content_info_card,
                mRecyclerView,
                false);
        mInfoCardBinding.draweeAvatar.setOnClickListener(new OnAvatarClickListener());
        mRecyclerView.addHeaderView(mInfoCardBinding.getRoot());

        getUserShow();
    }

    // 请求用户信息
    private void getUserShow() {
        // 如果存在已启动的请求，则忽略本次请求
        List<AbstractRequest> requestList = getHttpRequestList();
        if (requestList != null && !requestList.isEmpty()) {
            return;
        }

        WeiboAuth auth = new WeiboAuthKeeper(getContext()).read();
        UsersShowParam param = new UsersShowParam(auth);
        param.setHttpListener(new UsersShowHttpListener(SelfFragment.this));
        executeHttpRequestAsync(param);
    }

    class LoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            getUserShow();
        }

        @Override
        public void onLoadMore() {

        }
    }

    class UsersShowHttpListener extends HttpBaseOnFragmentListener<UsersShow> {

        public UsersShowHttpListener(BaseFragment fragment) {
            super(fragment);
        }

        @Override
        public void onSuccess(UsersShow usersShow, Response<UsersShow> response) {
            super.onSuccess(usersShow, response);

            // 绑定数据
            mInfoCardBinding.setUserShow(usersShow);
            mAdapter.setItemData(getContext(), usersShow);
        }

        @Override
        public void onEnd(Response<UsersShow> response) {
            super.onEnd(response);

            // 结束RecyclerView的刷新状态
            mRecyclerView.refreshComplete();
        }
    }

    class OnAvatarClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            UsersShow model = mInfoCardBinding.getUserShow();
            if (model != null) {
                ImageViewerActivity.start(
                        getActivity(),
                        model.avatarLarge,
                        model.avatarHd,
                        model.screenName,
                        model.description);
            }
        }
    }
}
