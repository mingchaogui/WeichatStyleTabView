package com.mingchaogui.twiggle.controller.view_controller;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.mingchaogui.twiggle.controller.BaseActivity;

public abstract class ViewController {

    BaseActivity mActivity;

    public ViewController(BaseActivity activity) {
        mActivity = activity;
    }
    public void onCreate(Bundle savedInstanceState) {}
    public void onSaveInstanceState(Bundle outState) {}
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {}
    public void onActivityResult(int requestCode, int resultCode, Intent data) {}
}
