/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zige.robot.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zige.robot.base.BaseActivity;
import com.zige.robot.utils.PhotoUtil;
import com.zige.robot.utils.ToastUtils;
import com.zige.robot.view.clip.ClipImageLayout;
import com.zige.robot.view.crop.CropUtil;
import com.zige.robot.view.crop.RotateBitmap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.zige.robot.R;

/**
 * 类描述:用户头像截图界面
 * 创建人: Shirley
 * 创建时间: 2016年8月30日
 */
public class SetPortraitActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SetPortraitActivity";

    public static final String PATH = "PATH";

    private ClipImageLayout mClipImageLayout;

    private RotateBitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
//        setContentView(R.layout.activity_portrait);
        setTitleName("裁剪");
        setTvActionText("完成");
        setTvActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clip();
            }
        });
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadInput();
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.crop_image);
        setImage();
    }

    private void loadInput() {
        Uri sourceUri = Uri.fromFile(new File(getIntent().getStringExtra(PATH)));
        if (sourceUri != null) {
            int exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(this, getContentResolver(), sourceUri));

            InputStream is = null;
            try {
                int sampleSize = CropUtil.calculateBitmapSampleSize(this, sourceUri);
                is = getContentResolver().openInputStream(sourceUri);
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = sampleSize;
                mBitmap = new RotateBitmap(BitmapFactory.decodeStream(is, null, option), exifRotation);
            } catch (IOException e) {
                Log.e(TAG, "Error reading image: " + e.getMessage(), e);
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "OOM reading image: " + e.getMessage(), e);
            } finally {
                CropUtil.closeSilently(is);
            }
        }
    }

    private void setImage() {
        if (mBitmap != null) {
            mClipImageLayout.setImage(mBitmap.getBitmap());
        }
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_portrait;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseBitmap();
    }

    private void clip() {
        File file;
        try {
            Bitmap bitmap = mClipImageLayout.clip();
            file = PhotoUtil.generatePicturePath();
            if (file == null) {
                ToastUtils.showToastLong(this, R.string.portrait_clip_fail);
                return;
            }
            System.gc();
            PhotoUtil.compressAndSaveBitmap(bitmap,file.getAbsolutePath(),100,50);
//            PhotoUtil.saveBitmaps(this, bitmap, file);
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToastLong(this, R.string.portrait_clip_fail);
            return;
        }
        //finish activity and return the clip file path.

        Intent i = new Intent();
        i.putExtra(PATH, file.getAbsolutePath());
        setResult(RESULT_OK, i);
        finish();
    }

    private void releaseBitmap() {
        mClipImageLayout.setImage(null);
        if (mBitmap != null) {
            mBitmap.getBitmap().recycle();
        }
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rl_back_return:
//                finish();
//                break;
//            default:
//                break;
//        }
    }
}
