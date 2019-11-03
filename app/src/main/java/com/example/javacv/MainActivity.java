package com.example.javacv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    static public int window_num;
    static public String appPath = null;// app路径
    public int isExit;

    static public final int MAIN = 0;
    static public final int LOCAL_RECOGNIZE = 1;// 本地选取图片
    static public final int TAKE_PICTURES = 2;// 拍照获取图片
    static public final int SELECT_IMG = 3;// 本地文件管理器

    static LocalRecognize localRecognize;// 本地选取
    static TakePicutures takePicutures;// 拍照获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initApp();
        initBtn();
    }

    public void initApp() {
        Loader.load(opencv_java.class);// openCV初始化,不能直接放在class开头

        // 初始化变量
        window_num = MAIN;
        isExit = 0;

        // 检查权限
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int check_result = ActivityCompat.checkSelfPermission(this, permission);// `允许`返回0,`拒绝`返回-1
        if (check_result != PackageManager.PERMISSION_GRANTED) {// 没有`写`权限
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);// 获取`写`权限
        }

        // 初始化路径字符串
        appPath = getExternalFilesDir("").getAbsolutePath();
        infoToast(this, appPath);

        // 初始化窗口
        localRecognize = new LocalRecognize();
        takePicutures = new TakePicutures();
    }

    public void initBtn() {// 初始化按钮
        // 本地识别
        Button btnLocal = findViewById(R.id.local_recognize);
        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localRecognize.show(getSupportFragmentManager(), "local recognize");
            }
        });

        // 一键退出
        Button btnExit = findViewById(R.id.exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExit = 1;
                onBackPressed();
            }
        });
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
    public void onBackPressed() {
        if (isExit == 1) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        infoLog("window num: " + window_num);
        switch (window_num) {// TODO
            case SELECT_IMG:
                localRecognize.showImg();
                break;
        }
    }
}
