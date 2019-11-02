package com.example.javacv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_stitching.Stitcher;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class LocalRecognize extends DialogFragment {
    String appPath = MainActivity.appPath;// TODO

    @Override
    public void show(FragmentManager fragmentManager, String tag) {
        ;// TODO
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ;// TODO
        // TODO;
        init();

        return null;// TODO
    }

    public void init() {
        ;// TODO
    }

    public void selectImg() {// 添加图片 TODO
        // 读取图片
//        MatVector imgs = new MatVector();
//        org.bytedeco.opencv.opencv_core.Mat mat1 = imread(img1Path);
//        org.bytedeco.opencv.opencv_core.Mat mat2 = imread(img2Path);
//        org.bytedeco.opencv.opencv_core.Mat mat3 = imread(img3Path);
//        org.bytedeco.opencv.opencv_core.Mat mat4 = imread(img4Path);
//        imgs.push_back(mat1);
//        imgs.push_back(mat2);
//        imgs.push_back(mat3);
//        imgs.push_back(mat4);
//
//        // 合并
//        Stitcher stitcher = Stitcher.create();
//        org.bytedeco.opencv.opencv_core.Mat pano = new org.bytedeco.opencv.opencv_core.Mat();
//        infoLog(mat1.arrayWidth() + " " + mat1.arrayHeight());
//        infoLog(mat2.arrayWidth() + " " + mat2.arrayHeight());
//        infoLog(mat3.arrayWidth() + " " + mat3.arrayHeight());
//        infoLog(mat4.arrayWidth() + " " + mat4.arrayHeight());
//        int result = stitcher.stitch(imgs, pano);// 合并
//        infoLog(pano.arrayWidth() + " " + pano.arrayHeight());
//
//        // 显示合并的图片 TODO
//        if (result == 0) {// 如果成功
//            Mat matBGR = new Mat(pano.address());// TODO 颜色异常
//            Mat matRGB = new Mat();
//            Imgproc.cvtColor(matBGR, matRGB, Imgproc.COLOR_BGR2RGB);// 将opencv默认的BGR转成RGB
//            ImageView imageView1 = findViewById(R.id.img_5);// 合并之后的图片显示的位置
//            Bitmap bitmap = Bitmap.createBitmap(pano.arrayWidth(), pano.arrayHeight(), Bitmap.Config.RGB_565);
//            Utils.matToBitmap(matRGB, bitmap);
//            imageView1.setImageBitmap(bitmap);
//        }
    }

    public void combineImg() {// 合并图片
        ;// TODO
    }

}
