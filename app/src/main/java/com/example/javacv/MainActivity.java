package com.example.javacv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.bytedeco.javacpp.Loader;
//import org.bytedeco.javacpp.opencv_java;
//import org.bytedeco.javacpp.opencv_stitching.Stitcher;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_java;
import org.bytedeco.opencv.opencv_stitching.Stitcher;
import org.opencv.android.Utils;
//import org.opencv.core.Mat;
//import org.opencv.imgproc.Imgproc;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

public class MainActivity extends AppCompatActivity {
    static public String appPath = null;
    static public String img1Path = null;
    static public String img2Path = null;
    static public String img3Path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void init() {
        Loader.load(opencv_java.class);// 不能直接放在class开头

        // 检查权限
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int check_result = ActivityCompat.checkSelfPermission(this, permission);// `允许`返回0,`拒绝`返回-1
        if (check_result != PackageManager.PERMISSION_GRANTED) {// 没有`写`权限
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);// 获取`写`权限
        }

        // 初始化路径字符串
        appPath = getExternalFilesDir("").getAbsolutePath();
        img1Path = appPath + "/" + "img_1.png";
        img2Path = appPath + "/" + "img_2.png";
        img3Path = appPath + "/" + "img_3.png";
        infoToast(this, appPath);

        combine();
        // rgb2gray();// TODO
    }

    public void showPic() {
        ImageView imageView2 = findViewById(R.id.img_2);
        Bitmap originImg = BitmapFactory.decodeFile(img2Path);// 打开本机图片
        imageView2.setImageBitmap(originImg);

        ImageView imageView3 = findViewById(R.id.img_3);
        Bitmap img3 = BitmapFactory.decodeFile(img3Path);// 打开本机图片
        imageView3.setImageBitmap(img3);
    }

    public void rgb2gray() {
        // 初始化图片
//        ImageView img_1 = findViewById(R.id.img_1);
//        Bitmap originImg = BitmapFactory.decodeFile(img1Path);// 打开本机图片
//        img_1.setImageBitmap(originImg);

        // 修改灰度 TODO
//        Mat rgbMat = new Mat();
//        Mat grayMat = new Mat();
//        ImageView img_2 = findViewById(R.id.img_2);
//        Bitmap grayImg = Bitmap.createBitmap(originImg.getWidth(), originImg.getHeight(), Bitmap.Config.RGB_565);
//        Utils.bitmapToMat(originImg, rgbMat);
//        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
//        Utils.matToBitmap(grayMat, grayImg);
//        img_2.setImageBitmap(grayImg);
    }

    public void combine() {// 合并图片
        // 读取两幅图片
        MatVector imgs = new MatVector();
        Mat matLeft = imread(img2Path);// 左半部分
        Mat matRight = imread(img3Path);// 右半部分
        imgs.push_back(matLeft);
        imgs.push_back(matRight);
        Stitcher stic = Stitcher.create();

        // 合并
        Mat pano = new Mat();
        stic.stitch(imgs, pano);

        // 显示合并的图片
        infoLog(pano.arrayWidth() + " " + pano.arrayHeight());
//        ImageView imageView1 = findViewById(R.id.img_1);
//        Bitmap img3 = Bitmap.createBitmap;
//        Utils.bitmapToMat();
//        imageView1.setImageBitmap();
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
}