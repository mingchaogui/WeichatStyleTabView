package com.mingchaogui.twiggle.controller;


import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.mingchaogui.twiggle.R;
import com.mingchaogui.wechat_style_tab_view.WechatStyleTabView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainTabBar {


    @Bind(R.id.txt_title)
    TextView mTxtTitle;

    @Bind(R.id.tab_tweet)
    WechatStyleTabView mTabTweet;
    @Bind(R.id.tab_focus)
    WechatStyleTabView mTabFocus;
    @Bind(R.id.tab_self)
    WechatStyleTabView mTabSelf;

    String[] mTabNames;
    List<WechatStyleTabView> mTabViews;
    OnTabClickListener mOnTabClickListener;

    public MainTabBar(Activity activity) {
        ButterKnife.bind(this, activity);

        mTabNames = new String[]{
                activity.getString(R.string.tab_tweet),
                activity.getString(R.string.tab_focus),
                activity.getString(R.string.tab_self)
        };

        WechatStyleTabView[] tabViewArr = new WechatStyleTabView[]{mTabTweet, mTabFocus, mTabSelf};
        mTabViews = Arrays.asList(tabViewArr);
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        mOnTabClickListener = listener;
        InnerTabClickListener innerListener = null;
        if (listener != null) {
            innerListener = new InnerTabClickListener();
        }
        for (View tab : mTabViews) {
            tab.setOnClickListener(innerListener);
        }
    }

    public void selectTab(int position) {
        for (int i = 0; i < mTabViews.size(); i++) {
            mTabViews.get(i).setSelected(i == position);
        }

        mTxtTitle.setText(getTabName(position));
    }

    public int getTabCount() {
        return mTabViews.size();
    }

    public String getTabName(int position) {
        return mTabNames[position];
    }

    public WechatStyleTabView getTabView(int position) {
        return mTabViews.get(position);
    }

    public interface OnTabClickListener {
        void onTabClick(int position);
    }

    class InnerTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = mTabViews.indexOf(v);
            mOnTabClickListener.onTabClick(position);
        }
    }
}
