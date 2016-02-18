package com.mingchaogui.twiggle.event;


import com.mingchaogui.twiggle.auth.WeiboAuth;

public class SignInEvent {

    public WeiboAuth auth;

    public SignInEvent(WeiboAuth auth) {
        this.auth = auth;
    }
}
