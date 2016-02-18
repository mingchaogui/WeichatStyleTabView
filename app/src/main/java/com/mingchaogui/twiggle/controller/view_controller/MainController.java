package com.mingchaogui.twiggle.controller.view_controller;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.mingchaogui.twiggle.R;
import com.mingchaogui.twiggle.controller.BaseActivity;
import com.mingchaogui.twiggle.controller.MainPagerAdapter;
import com.mingchaogui.twiggle.controller.MainTabBar;
import com.mingchaogui.wechat_style_tab_view.WechatStyleTabView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainController extends ViewController {

    private static final String KEY_SELECTED_TAB_POSITION = "selected_tab_position";

    @Bind(R.id.vp_content)
    ViewPager mVPContent;

    MainTabBar mMainTabBar;

    public MainController(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(R.layout.content_main);
        ButterKnife.bind(this, mActivity);

        PagerAdapter pagerAdapter = new MainPagerAdapter(mActivity.getSupportFragmentManager());
        mVPContent.setOffscreenPageLimit(pagerAdapter.getCount());
        mVPContent.setAdapter(pagerAdapter);
        mVPContent.addOnPageChangeListener(new OnPageChangeListener());

        mMainTabBar = new MainTabBar(mActivity);
        mMainTabBar.setOnTabClickListener(new OnTabClickListener());
        mMainTabBar.selectTab(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SELECTED_TAB_POSITION, mVPContent.getCurrentItem());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        int tabPosition = savedInstanceState.getInt(KEY_SELECTED_TAB_POSITION, 0);
        mVPContent.setCurrentItem(tabPosition);
    }

    class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 根据偏移量设置TabView的填色透明度
            WechatStyleTabView currentTabView = mMainTabBar.getTabView(position);
            int currentAlpha = computeAlpha(1 - positionOffset);
            currentTabView.setColorAlpha(currentAlpha);

            if (positionOffset == 0) {
                // 快速滑动ViewPager的情况下，例如快速划过多个Page时，确保非当前位置的TabView透明度被正确归零
                for (int i = 0; i < mMainTabBar.getTabCount(); i++) {
                    WechatStyleTabView tabView = mMainTabBar.getTabView(i);
                    if (tabView != currentTabView) {
                        tabView.setColorAlpha(0);
                    }
                }
            } else {
                WechatStyleTabView targetTabView = mMainTabBar.getTabView(position + 1);
                int targetAlpha = computeAlpha(positionOffset);
                targetTabView.setColorAlpha(targetAlpha);
            }
        }

        @Override
        public void onPageSelected(int position) {
            mMainTabBar.selectTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            /**
             * 设置TabView的填色模式，
             * 当ViewPager不处于滑动状态时使用状态模式(根据Selected状态来填色)
             * 否则使用透明度模式（根据透明度填色）
             */
            WechatStyleTabView.FillColorMode mode;
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                mode = WechatStyleTabView.FillColorMode.STATE;
            } else {
                mode = WechatStyleTabView.FillColorMode.ALPHA;
            }
            for (int i = 0; i < mMainTabBar.getTabCount(); i++) {
                mMainTabBar.getTabView(i).setFillColorMode(mode);
            }
        }

        public int computeAlpha(float percent) {
            return (int)Math.ceil(255 * percent);
        }
    }

    class OnTabClickListener implements MainTabBar.OnTabClickListener {

        @Override
        public void onTabClick(int position) {
            mMainTabBar.selectTab(position);
            mVPContent.setCurrentItem(position, false);
        }
    }
}
