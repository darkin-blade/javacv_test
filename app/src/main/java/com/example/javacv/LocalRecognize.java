package com.example.javacv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class LocalRecognize extends DialogFragment {
    String appPath = MainActivity.appPath;// TODO

    public int img_height = 400;
    public int img_margin = 20;

    public SelectImg selectImg;// 选取图片

    public Button btnAdd;// 添加本地图片
    public Button btnDel;// 删除添加的图片
    public Button btnWork;// 生成->保存合并的图片
    public Button btnBack;// 返回主菜单

    static public View myView;
    static public FragmentManager fragmentManager;

    public ArrayList<String> imgList;// TODO 所有添加的图片
    public ArrayList<String> delList;// TODO 所有要删除的图片

    public LinearLayout imgLayout;// 图片显示列表

    @Override
    public void show(FragmentManager fragmentManager, String tag) {
        super.show(fragmentManager, tag);
        MainActivity.window_num = MainActivity.LOCAL_RECOGNIZE;

        this.fragmentManager = fragmentManager;
        selectImg = new SelectImg();// 初始化文件浏览器
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme);// 关闭背景(点击外部不能取消)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.local_recognize, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));// 背景透明

        initData();
        initBtn();

        return myView;
    }

    public void initData() {
        imgList = new ArrayList<String>();
        imgLayout = myView.findViewById(R.id.img_list);
    }

    public void initBtn() {
        btnAdd = myView.findViewById(R.id.button_1);
        btnDel = myView.findViewById(R.id.button_2);
        btnWork = myView.findViewById(R.id.button_3);
        btnBack = myView.findViewById(R.id.button_4);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg.show(fragmentManager, "select");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showImg() {// 实时更新图片显示 TODO
        // TODO 先清除所有图片
        imgLayout.removeAllViews();

        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, img_height);
        ImageView imageView = new ImageView(getContext());// 重复使用
        imageView.setLayoutParams(imgParam);
        Bitmap bitmap;// 重复使用

        for (int i = 0; i < imgList.size(); i ++) {
            bitmap = BitmapFactory.decodeFile(imgList.get(i));
            imageView.setImageBitmap(bitmap);
            imgLayout.addView(imageView);
        }
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
