package com.mingchaogui.twiggle.controller;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.mingchaogui.twiggle.AppEnviroment;
import com.mingchaogui.twiggle.R;
import com.mingchaogui.twiggle.util.FrescoUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageViewerActivity extends BaseActivity {

    private static String LOW_IMAGE_URI = "low_image";
    private static String IMAGE_URI = "image";
    private static String TITLE = "title";
    private static String DESCRIPTION = "description";

    @Bind(R.id.drawee_avatar)
    SimpleDraweeView mDrawee;
    @Bind(R.id.btn_save_to_album)
    Button mBtnSaveToAlbum;
    @Bind(R.id.btn_cancel)
    Button mBtnCancel;

    private String mLowImageUri;
    private String mImageUri;
    private String mTitle;
    private String mDescription;

    public static void start(Activity activity,
                             String lowImage, String image,
                             String title, String description) {
        Intent intent = new Intent();
        intent.setClass(activity.getApplicationContext(), ImageViewerActivity.class);
        intent.putExtra(LOW_IMAGE_URI, lowImage);
        intent.putExtra(IMAGE_URI, image);
        intent.putExtra(TITLE, title);
        intent.putExtra(DESCRIPTION, description);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mLowImageUri = intent.getStringExtra(LOW_IMAGE_URI);
        mImageUri = intent.getStringExtra(IMAGE_URI);
        mTitle = intent.getStringExtra(TITLE);
        mDescription = intent.getStringExtra(DESCRIPTION);

        setContentView(R.layout.content_image_viewer);
        ButterKnife.bind(this);

        if (TextUtils.isEmpty(mImageUri)) {
            showToast(R.string.msg_image_uri_is_empty);
        } else {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setOldController(mDrawee.getController())
                    .setControllerListener(new DraweeControllerListener())
                    .setLowResImageRequest(ImageRequest.fromUri(mLowImageUri))
                    .setImageRequest(ImageRequest.fromUri(mImageUri))
                    .setAutoPlayAnimations(true)
                    .setTapToRetryEnabled(true)
                    .build();
            mDrawee.setController(draweeController);
        }

        OnViewClickListener onClickListener = new OnViewClickListener();
        mBtnSaveToAlbum.setOnClickListener(onClickListener);
        mBtnCancel.setOnClickListener(onClickListener);
    }

    private void saveImageToAlnum(FileBinaryResource res) {
        if (res == null) {
            return;
        }

        // 从uri中取得文件名
        String[] split = mImageUri.split("/");
        String filename = split[split.length - 1];
        // 得到源文件、输出文件的File对象
        File srcFile = res.getFile();
        File outFile = new File(
                AppEnviroment.getSavedPictureDirectory(),
                filename
        );
        // 保存到本地
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChanel = null;
        FileChannel outChanel = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(outFile);
            inChanel = fis.getChannel();
            outChanel = fos.getChannel();
            inChanel.transferTo(0, inChanel.size(), outChanel);
        } catch (IOException e) {
            e.printStackTrace();
            // 提示保存失败
            showToast(R.string.msg_save_image_failure);
            return;
        } finally {
            try {
                if (outChanel != null) outChanel.close();
                if (inChanel != null) inChanel.close();
                if (fos != null) fos.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 更新到图库
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.ImageColumns.DATA, outFile.getPath());
        values.put(MediaStore.Images.ImageColumns.TITLE, mTitle);
        values.put(MediaStore.Images.ImageColumns.DESCRIPTION, mDescription);
        values.put(MediaStore.Images.ImageColumns.SIZE, outFile.length());
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // 提示保存成功
        String msg = getString(R.string.msg_save_image_to);
        showToast(String.format(msg, outFile.getPath()));
    }

    class DraweeControllerListener extends BaseControllerListener<ImageInfo> {

        @Override
        public void onFailure(String id, Throwable throwable) {
            showToast(R.string.image_except_load_failure);
        }
    }

    class OnViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_to_album:
                    if (!TextUtils.isEmpty(mImageUri)) {
                        saveImageToAlnum(FrescoUtil.getFileFromDiskCache(mImageUri));
                    };

                    break;

                case R.id.btn_cancel:
                    finish();

                    break;
            }
        }
    }
}
