package com.example.javacv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.bytedeco.javacpp.Loader;
//import org.bytedeco.javacpp.opencv_java;
//import org.bytedeco.javacpp.opencv_stitching.Stitcher;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_java;
import org.bytedeco.opencv.opencv_stitching.Stitcher;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
//import org.opencv.imgproc.Imgproc;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    static public int window_num;
    static public String appPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        Loader.load(opencv_java.class);// 初始化,不能直接放在class开头

        // 检查权限
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int check_result = ActivityCompat.checkSelfPermission(this, permission);// `允许`返回0,`拒绝`返回-1
        if (check_result != PackageManager.PERMISSION_GRANTED) {// 没有`写`权限
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);// 获取`写`权限
        }

        // 初始化路径字符串
        appPath = getExternalFilesDir("").getAbsolutePath();
        infoToast(this, appPath);
    }

    static public void infoLog(String log) {
        Log.i("fuck", log);
    }

    static public void infoToast(Context context, String log) {
        Toast toast =  Toast.makeText(context, log, Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView textView = view.findViewById(android.R.id.message);
        textView.setTextColor(Color.rgb(0x00, 0x00, 0x00));
        toast.show();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        infoLog("window num: " + window_num);
    }
}
