package com.mingchaogui.twiggle.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.mingchaogui.twiggle.auth.WeiboAuthKeeper;
import com.mingchaogui.twiggle.controller.view_controller.MainController;
import com.mingchaogui.twiggle.controller.view_controller.SignInController;
import com.mingchaogui.twiggle.controller.view_controller.ViewController;
import com.mingchaogui.twiggle.event.SignInEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MainActivity extends BaseActivity {

    ViewController mViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        WeiboAuthKeeper weiboAuthKeeper = new WeiboAuthKeeper(getApplicationContext());
        if (weiboAuthKeeper.isEmpty()) {// 装载登录页面
            mViewController = new SignInController(this);
        } else {// 装载主页面
            mViewController = new MainController(this);
        }

        mViewController.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mViewController.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        mViewController.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mViewController.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void onEvent(SignInEvent event) {
        mViewController = new MainController(this);
        mViewController.onCreate(null);
    }
}
