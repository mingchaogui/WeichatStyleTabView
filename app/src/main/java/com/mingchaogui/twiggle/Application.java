package com.mingchaogui.twiggle;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.mingchaogui.twiggle.http.HttpWrapper;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        HttpWrapper.init(this);
    }
}
