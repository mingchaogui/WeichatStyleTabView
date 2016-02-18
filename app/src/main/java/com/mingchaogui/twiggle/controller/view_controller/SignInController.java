package com.mingchaogui.twiggle.controller.view_controller;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mingchaogui.twiggle.R;
import com.mingchaogui.twiggle.auth.Constants;
import com.mingchaogui.twiggle.auth.WeiboAuth;
import com.mingchaogui.twiggle.auth.WeiboAuthKeeper;
import com.mingchaogui.twiggle.controller.BaseActivity;
import com.mingchaogui.twiggle.event.SignInEvent;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignInController extends ViewController {

    @Bind(R.id.btn_sign_in)
    Button mBtnSignIn;

    SsoHandler mSsoHandler;
    AuthListener mAuthListener;

    public SignInController(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(R.layout.content_sign_in);
        ButterKnife.bind(this, mActivity);

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorize();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 请求获取微博授权信息
     */
    private void authorize() {
        if (mSsoHandler == null) {
            AuthInfo authInfo = new AuthInfo(mActivity.getApplicationContext(), Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
            mSsoHandler = new SsoHandler(mActivity, authInfo);
        }
        if (mAuthListener == null) {
            mAuthListener = new AuthListener();
        }
        mSsoHandler.authorize(mAuthListener);
    }

    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle bundle) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);// 从 Bundle 中解析 Token
            if (accessToken.isSessionValid()) {
                WeiboAuth auth = new WeiboAuth();
                auth.setUid(accessToken.getUid());
                auth.setAccessToken(accessToken.getToken());
                auth.setRefreshToken(accessToken.getRefreshToken());
                auth.setExpiresTime(accessToken.getExpiresTime());
                auth.setPhoneNum(accessToken.getPhoneNum());

                // 保存微博授权信息
                WeiboAuthKeeper keeper = new WeiboAuthKeeper(mActivity.getApplicationContext());
                keeper.write(auth);
                // 发送已登录事件
                EventBus.getDefault().post(new SignInEvent(auth));
            } else {// 当注册的应用程序签名不正确时,就会收到错误Code,请确保签名正确
                String code = bundle.getString("code", "");
                String text = mActivity.getString(R.string.tag_exception_code, code);
                mActivity.showToast(text);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mActivity.showToast(e.getLocalizedMessage());
        }

        @Override
        public void onCancel() {

        }
    }
}

