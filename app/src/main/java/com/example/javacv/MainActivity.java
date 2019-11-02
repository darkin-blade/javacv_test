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
    static public String img1Path = null;
    static public String img2Path = null;
    static public String img3Path = null;
    static public String img4Path = null;
    static public String img5Path = null;

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
//        img1Path = appPath + "/" + "img_1.png";
//        img2Path = appPath + "/" + "img_2.png";
//        img3Path = appPath + "/" + "img_3.png";
//        img4Path = appPath + "/" + "img_4.png";// TODO
        img1Path = appPath + "/img_11.png";
        img2Path = appPath + "/img_12.png";
        img3Path = appPath + "/img_13.png";
        img4Path = appPath + "/img_14.png";
        img5Path = appPath + "/img_10.jpg";// TODO
        infoToast(this, appPath);
    }

    public void showImg() {
        ImageView imageView = findViewById(R.id.img_1);
        Bitmap img = BitmapFactory.decodeFile(img1Path);// 打开本机图片
        imageView.setImageBitmap(img);

        imageView = findViewById(R.id.img_2);
        img = BitmapFactory.decodeFile(img2Path);
        imageView.setImageBitmap(img);

        imageView = findViewById(R.id.img_3);
        img = BitmapFactory.decodeFile(img3Path);
        imageView.setImageBitmap(img);

        imageView = findViewById(R.id.img_4);
        img = BitmapFactory.decodeFile(img4Path);
        imageView.setImageBitmap(img);
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

    public void combine2() {// TODO 上下拼接
        // 读取图片
        MatVector imgs = new MatVector();
        org.bytedeco.opencv.opencv_core.Mat mat1 = imread(img1Path);
        org.bytedeco.opencv.opencv_core.Mat mat2 = imread(img2Path);
        imgs.push_back(mat1);
        imgs.push_back(mat2);

        // 合并
        Stitcher stitcher = Stitcher.create();
        org.bytedeco.opencv.opencv_core.Mat pano = new org.bytedeco.opencv.opencv_core.Mat();
        infoLog(mat1.arrayWidth() + " " + mat1.arrayHeight());
        infoLog(mat2.arrayWidth() + " " + mat2.arrayHeight());
        int result = stitcher.stitch(imgs, pano);// 合并
        infoLog(pano.arrayWidth() + " " + pano.arrayHeight());

        // 显示合并的图片 TODO
        if (result == 0) {// 如果成功
            Mat matBGR = new Mat(pano.address());// TODO 颜色异常
            Mat matRGB = new Mat();
            Imgproc.cvtColor(matBGR, matRGB, Imgproc.COLOR_BGR2RGB);// 将opencv默认的BGR转成RGB
            ImageView imageView1 = findViewById(R.id.img_3);// 合并之后的图片显示的位置
            Bitmap bitmap = Bitmap.createBitmap(pano.arrayWidth(), pano.arrayHeight(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(matRGB, bitmap);
            imageView1.setImageBitmap(bitmap);
        }
    }

    public void combine1() {// 合并图片
        // 读取图片
        MatVector imgs = new MatVector();
        org.bytedeco.opencv.opencv_core.Mat mat1 = imread(img1Path);
        org.bytedeco.opencv.opencv_core.Mat mat2 = imread(img2Path);
        org.bytedeco.opencv.opencv_core.Mat mat3 = imread(img3Path);
        org.bytedeco.opencv.opencv_core.Mat mat4 = imread(img4Path);
        imgs.push_back(mat1);
        imgs.push_back(mat2);
        imgs.push_back(mat3);
        imgs.push_back(mat4);

        // 合并
        Stitcher stitcher = Stitcher.create();
        org.bytedeco.opencv.opencv_core.Mat pano = new org.bytedeco.opencv.opencv_core.Mat();
        infoLog(mat1.arrayWidth() + " " + mat1.arrayHeight());
        infoLog(mat2.arrayWidth() + " " + mat2.arrayHeight());
        infoLog(mat3.arrayWidth() + " " + mat3.arrayHeight());
        infoLog(mat4.arrayWidth() + " " + mat4.arrayHeight());
        int result = stitcher.stitch(imgs, pano);// 合并
        infoLog(pano.arrayWidth() + " " + pano.arrayHeight());

        // 显示合并的图片 TODO
        if (result == 0) {// 如果成功
            Mat matBGR = new Mat(pano.address());// TODO 颜色异常
            Mat matRGB = new Mat();
            Imgproc.cvtColor(matBGR, matRGB, Imgproc.COLOR_BGR2RGB);// 将opencv默认的BGR转成RGB
            ImageView imageView1 = findViewById(R.id.img_5);// 合并之后的图片显示的位置
            Bitmap bitmap = Bitmap.createBitmap(pano.arrayWidth(), pano.arrayHeight(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(matRGB, bitmap);
            imageView1.setImageBitmap(bitmap);
        }
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
