package com.mingchaogui.twiggle.controller;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.litesuits.common.assist.Toastor;

public class BaseActivity extends AppCompatActivity {

    Toastor mToastor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToastor = new Toastor(getApplicationContext());
    }

    public Toastor getToastor() {
        return mToastor;
    }

    public void showToast(int resId) {
        mToastor.getSingletonToast(resId).show();
    }

    public void showToast(String text) {
        mToastor.getSingletonToast(text).show();
    }
}
