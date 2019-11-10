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
//        options.inJustDecodeBounds = true;// TODO
        bitmap = BitmapFactory.decodeFile(imgPath, options);
        // TODO
        return true;
    }

    public Bitmap LoadImg(String imgPath, int width, int height) {// 加载图片 TODO
        return null;
    }

    public Bitmap LoadThumb(final String imgPath, final int width, final int height) {// 加载缩略图
        class ImgAsync extends AsyncTask<Void, Void, Void> {
            Bitmap bitmap = null;

            @Override
            protected Void doInBackground(Void... voids) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;// TODO 此时decode的bitmap为null
                bitmap = BitmapFactory.decodeFile(imgPath, options);
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

                return null;
            }

            public Bitmap getBitmap() {
                return bitmap;
            }
        }
        ImgAsync imgAsync = new ImgAsync();
        imgAsync.execute();
        MainActivity.infoLog("null: " + (imgAsync.getBitmap() == null));
        return imgAsync.getBitmap();// TODO
    }
}
