package com.mingchaogui.twiggle.controller;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.litesuits.http.data.NameValuePair;
import com.mingchaogui.twiggle.R;
import com.mingchaogui.twiggle.databinding.ListitemInfoBinding;
import com.mingchaogui.twiggle.model.UsersShow;

import java.util.ArrayList;
import java.util.List;

public class WeiboInfoAdapter extends RecyclerView.Adapter<WeiboInfoAdapter.InfoItemViewHodler> {

    List<NameValuePair> mItemData;

    @Override
    public InfoItemViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListitemInfoBinding binding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.listitem_info,
                parent,
                false);

        return new InfoItemViewHodler(binding, binding.getRoot());
    }

    @Override
    public void onBindViewHolder(InfoItemViewHodler holder, int position) {
        holder.bind(mItemData.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemData == null ? 0 : mItemData.size();
    }

    public void setItemData(Context context, UsersShow usersShow) {
        if (usersShow == null) {
            mItemData = null;
        } else {
            if (mItemData == null) {
                mItemData = new ArrayList<>();
            } else {
                mItemData.clear();
            }
            mItemData.add(new NameValuePair(context.getString(R.string.txt_online_status), usersShow.getDisplayOnlineStatus(context)));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_uid), usersShow.uidStr));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_screen_name), usersShow.screenName));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_description), usersShow.description));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_created_at), usersShow.getDisplayCreateTime()));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_weihao), usersShow.weihao));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_gender), usersShow.getDisplayGender(context)));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_location), usersShow.location));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_blog_url), usersShow.blogUrl));
            mItemData.add(new NameValuePair(context.getString(R.string.txt_weibo_domain), TextUtils.isEmpty(usersShow.domain) ? "weibo.com/" + usersShow.profileUrl : usersShow.domain));
        }

        notifyDataSetChanged();
    }

    class InfoItemViewHodler extends RecyclerView.ViewHolder {

        ListitemInfoBinding mBinding;

        public InfoItemViewHodler(ListitemInfoBinding binding, View itemView) {
            super(itemView);

            mBinding = binding;
        }

        public void bind(NameValuePair pair) {
            mBinding.setPair(pair);
        }
    }
}
