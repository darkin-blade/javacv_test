package com.example.javacv.interfaces;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.example.javacv.MainActivity;

public class ManagerImg {
    public Context context;

    public ManagerImg(Context context) {// TODO 初始化
        this.context = context;
    }

    public boolean isImg(String imgPath) {// 判断是否是图片
        return true;// TODO
    }

    public Bitmap ThumbImg(String imgPath, int width, int height) {// 转换成缩略图
        String thumbPath = null;// 缩略图路径
        int imgID = -1;
        ContentResolver contentResolver = context.getContentResolver();// TODO
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] {// TODO 返回的列
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA,
                },
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                MainActivity.infoLog(imgPath + ": ");
                MainActivity.infoLog(cursor.getString(1));
                imgID = cursor.getInt(0);
            } while (cursor.moveToNext());
            cursor.close();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;// 不采用抖动解码()
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return MediaStore.Images.Thumbnails.getThumbnail(contentResolver, imgID, MediaStore.Images.Thumbnails.MINI_KIND, options);
        } else {
            MainActivity.infoLog(imgPath + ": null");
        }
        return null;// TODO
    }
}
