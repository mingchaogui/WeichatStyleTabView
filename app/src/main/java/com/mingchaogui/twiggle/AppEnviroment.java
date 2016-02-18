package com.mingchaogui.twiggle;


import android.os.Environment;

import java.io.File;

public class AppEnviroment {

    private static final String APP_NAME = "twiggle";

    public static File getSavedPictureDirectory() {
        File envFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(envFile.getPath() + "/" + APP_NAME);
        file.mkdirs();

        return file;
    }
}
