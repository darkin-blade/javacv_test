package com.example.javacv.interfaces;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.*;

import com.example.javacv.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagerImg {
    public Context context;

    public ManagerImg(Context context) {// TODO 初始化
        this.context = context;
    }

    public boolean isImg(String imgPath) {// 判断是否是图片
        return true;// TODO
    }

    public Bitmap ThumbImg(String imgPath, int width, int height) {// 转换成缩略图
        ArrayList<HashMap<String, String>> thumbList = new ArrayList<>();// TODO 存储结果

        // 查询缩略图表单
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(// TODO
                Thumbnails.EXTERNAL_CONTENT_URI,
                new String [] {
                        Thumbnails.IMAGE_ID,
                        Thumbnails.DATA
                },
                null,
                null,
                null);

        // 有查询结果
        if (cursor.moveToFirst()) {// TODO
            int thumb_id;
            String thumb_data;
            int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
            do {
                thumb_id = cursor.getInt(image_idColumn);
                thumb_data = cursor.getString(dataColumn);

                // 存储hash表
                HashMap<String, String> thumb_hash = new HashMap<>();// TODO
                thumb_hash.put("thumb_id", thumb_id + "");
                thumb_hash.put("thumb_data", thumb_data);

                thumbList.add(thumb_hash);
            } while (cursor.moveToNext());
            cursor.close();

            // 获取图片 TODO
            File file = new File(imgPath);
            Uri uri = Uri.fromFile(file);
            MainActivity.infoLog(imgPath + ": " + uri);
        } else {
            MainActivity.infoLog(imgPath + ": null");
        }
        return null;// TODO
    }
}
