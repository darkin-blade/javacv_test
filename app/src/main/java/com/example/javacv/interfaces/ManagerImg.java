package com.example.javacv.interfaces;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.*;

import com.example.javacv.MainActivity;

import java.io.File;

public class ManagerImg {
    public Context context;

    public ManagerImg(Context context) {// TODO 初始化
        this.context = context;
    }

    public boolean isImg(String imgPath) {// 判断是否是图片 TODO
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// TODO
        bitmap = BitmapFactory.decodeFile(imgPath, options);
        if (options.outHeight == 0 || options.outWidth == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Bitmap LoadImg(String imgPath, int width, int height) {// 加载图片 TODO
        return null;
    }

    public Bitmap LoadThumb(final String imgPath, final int width, final int height) {// 加载缩略图
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// TODO 此时decode的bitmap为null
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        options.inJustDecodeBounds = false;// TODO

        // 缩放
        int h_rate = options.outHeight / height;
        int w_rate = options.outWidth / width;
        int rate = 1;
        if (h_rate < w_rate) {
            rate = h_rate;
        } else {
            rate = w_rate;
        }
        options.inSampleSize = rate;
        bitmap = BitmapFactory.decodeFile(imgPath, options);

        return bitmap;// TODO
    }
}
