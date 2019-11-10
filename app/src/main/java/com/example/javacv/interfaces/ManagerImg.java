package com.example.javacv.interfaces;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(imgPath)));
        context.sendBroadcast(intent);
        return true;
    }

    public Bitmap LoadImg(String imgPath, int width, int height) {// 加载图片 TODO
        return null;
    }

    public Bitmap ThumbImg(String imgPath, int width, int height) {// 转换成缩略图
        // 查询缩略图表单
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                Media._ID,
                Media.DATA
        };
//        String selection = null;
        String selection = Media.DATA + " = '" + imgPath + "'";// 相当于数据库的where
        String[] selectionArgs = null;
        String sortOrder = null;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(// TODO
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor == null || cursor.getCount() == 0) {// 没有结果
            MainActivity.infoLog(imgPath + ": null");
            return null;
        }

        // 有查询结果
        if (cursor.moveToFirst()) {// TODO
            int img_id = cursor.getInt(0);
//            do {
//                MainActivity.infoLog(cursor.getString(1));
//            } while (cursor.moveToNext());
            MainActivity.infoLog("count: " + cursor.getCount());
            cursor.close();

            // 返回缩略图
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = Thumbnails.getThumbnail(contentResolver, img_id, MediaStore.Video.Thumbnails.MINI_KIND, options);
            MainActivity.infoLog("null: " + (bitmap == null));
            return bitmap;
        } else {
            MainActivity.infoLog(imgPath + ": null");
        }
        return null;// TODO
    }
}
